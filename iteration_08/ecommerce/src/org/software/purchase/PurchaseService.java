package org.software.purchase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.software.cart.Item;
import org.software.cart.ItemList;
import org.software.util.DataBase;

@Path("/purchase")
public class PurchaseService {

	@GET
	@Path("/items/{purchase_id}")
	@Produces("application/json")
	public ItemList getItems(@Context HttpServletRequest request, @PathParam(value = "purchase_id") int purchase_id) {
		ArrayList itemList = new ArrayList();

		DataBase database = new DataBase();
		Connection connection1 = null;
		Statement statement1 = null;
		ResultSet rs1 = null;
		String sql = "";

		try {
			connection1 = database.getConnection("client");
			statement1 = connection1.createStatement();

			HttpSession session = request.getSession();
			//long user_id = (long) session.getAttribute("user_id");
			long user_id = getUserId(request);
			
			if (purchase_id == 0) {
				sql = "select id from purchases";
				sql += " where user_id = " + user_id;
				sql += " order by created_at DESC limit 1";
				rs1 = statement1.executeQuery(sql);
				if (rs1.next()) {
					purchase_id = rs1.getInt("id");
				}
			}

			sql = "select purchase_items.id as item_id, * from purchases, purchase_items, products";
			sql += " where purchases.id = purchase_items.purchase_id";
			sql += " and purchase_items.product_id = products.id";
			sql += " and purchase_id = " + purchase_id;

			rs1 = statement1.executeQuery(sql);
			while (rs1.next()) {
				long id = rs1.getInt("item_id");
				double quantity = rs1.getDouble("quantity");
				double price = rs1.getDouble("price");
				long product_id = rs1.getLong("product_id");
				String product_name = rs1.getString("name");
				String short_description = rs1.getString("short_description");
				String product_icon = rs1.getString("icon");

				Item item = new Item();
				item.setId(id);
				item.setQuantity(quantity);
				item.setPrice(price);
				item.setProduct_id(product_id);
				item.setProduct_name(product_name);
				item.setProduct_description(short_description);
				item.setProduct_icon(product_icon);

				itemList.add(item);
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		} finally {
			database.closeObject(rs1);
			database.closeObject(statement1);
			database.closeObject(connection1);
		}

		return new ItemList(itemList);
	}

	@GET
	@Path("/list")
	@Produces("application/json")
	public PurchaseList getPurchases(@Context HttpServletRequest request) {
		ArrayList purchaseList = new ArrayList();

		HttpSession session = request.getSession();
//		long user_id = (long) session.getAttribute("user_id");
		long user_id = getUserId(request);
		
		DataBase database = new DataBase();
		Connection connection1 = null;
		Statement statement1 = null;
		ResultSet rs1 = null;
		String sql = "";

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			connection1 = database.getConnection("client");
			statement1 = connection1.createStatement();

			sql = "select * from purchases";
			sql += " where user_id = " + user_id;
			sql += " order by purchases.created_at DESC";

			rs1 = statement1.executeQuery(sql);
			while (rs1.next()) {
				long id = rs1.getInt("id");
				Timestamp created_at = rs1.getTimestamp("created_at");
				Timestamp updated_at = rs1.getTimestamp("updated_at");

				// String created_at_text = new
				// SimpleDateFormat("dd/MM/yyyy").format(created_at.getTime());
				String created_at_text = dateFormat.format(created_at.getTime());

				Purchase purchase = new Purchase();
				purchase.setId(id);
				purchase.setUser_id(user_id);
				purchase.setCreated_at(created_at);
				purchase.setCreated_at_text(created_at_text);

				purchaseList.add(purchase);
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		} finally {
			database.closeObject(rs1);
			database.closeObject(statement1);
			database.closeObject(connection1);
		}

		return new PurchaseList(purchaseList);
	}

	public long getUserId(@Context HttpServletRequest request) {
		long user_id = -1;

		DataBase database = new DataBase();
		Connection connection1 = null;
		Statement statement1 = null;
		ResultSet rs1 = null;
		String sql = "";

		String username = "";
		try {
			username = request.getUserPrincipal().getName();
		} catch (Exception e) {
			username = "";
		}

		if (username.length() > 0) {

			try {
				connection1 = database.getConnection("admin");
				statement1 = connection1.createStatement();
				
				sql = "select id from users where username = '" + username + "'";
				
				System.out.println(sql);
				
				rs1 = statement1.executeQuery(sql);
				
				if (rs1.next()) {
					user_id = rs1.getLong("id");
				}

			} catch (Exception e) {
				System.out.println("Error: " + e.toString());
			} finally {
				database.closeObject(rs1);
				database.closeObject(statement1);
				database.closeObject(connection1);
			}
		}
		
		System.out.println("getUserId(): " + user_id);
		
		return user_id;
	}

	public int insertPurchase(long user_id) {
		int purchase_id = 0;

		DataBase database = new DataBase();
		Connection connection1 = null;
		//Statement statement1 = null;
		ResultSet rs1 = null;
		PreparedStatement preparedStatement1 = null;
		String sql = "";

		try {
			connection1 = database.getConnection("client");

			sql = "INSERT INTO purchases (user_id)";
			sql = sql + " VALUES (?) RETURNING id";

			preparedStatement1 = connection1.prepareStatement(sql);
			preparedStatement1.setLong(1, user_id);
			rs1 = preparedStatement1.executeQuery();

			if (rs1.next()) {
				purchase_id = rs1.getInt("id");
			}

		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		} finally {
			database.closeObject(rs1);
			//database.closeObject(statement1);
			database.closeObject(preparedStatement1);
			database.closeObject(connection1);
		}

		return purchase_id;
	}

	@GET
	@Path("/checkout")
	@Produces("application/json")
	public Response updateItems(@Context HttpServletRequest request) {
		String message = "";
		int inserteds = 0;

		HttpSession session = request.getSession();
		
//		long user_id = (long) session.getAttribute("user_id");
//		String username = (String) session.getAttribute("username");
		
		long user_id = getUserId(request);
		
		System.out.println("(00)" + user_id);
		
		ArrayList cart = (ArrayList) session.getAttribute("cart");

		if (cart == null) {
			cart = new ArrayList();
		}

		System.out.println("updateItems()(01) " + cart.size());
		
		if (cart.size() > 0) {
			long purchase_id = insertPurchase(user_id);
			
			System.out.println("(00)" + purchase_id);
			
			
			DataBase database = new DataBase();
			Connection connection1 = null;
			Statement statement1 = null;
			//ResultSet rs1 = null;
			String sql = "";
			
			try {
				connection1 = database.getConnection("client");
				statement1 = connection1.createStatement();
				
				//sql = "INSERT INTO purchase_items (purchase_id, product_id, price, quantity) VALUES ";
				//String separator = "";
				for (int i = 0; i < cart.size(); i = i + 1) {
					Item item = (Item) cart.get(i);
					long product_id = item.getProduct_id();
					double quantity = item.getQuantity();
					double price = item.getPrice();
	
					//sql = sql + separator + "(" + purchase_id + "," + product_id + "," + price + "," + quantity + ")";
					//separator = ", ";
					
					sql = "INSERT INTO purchase_items (purchase_id, product_id, price, quantity)";
					sql += " VALUES (" + purchase_id + "," + product_id + "," + price + "," + quantity + ")";
					
					System.out.println(sql);
					
					inserteds += statement1.executeUpdate(sql);
					
				}
		
			} catch (Exception e) {
				System.out.println("Error: " + e.toString());
			} finally {
				//database.closeObject(rs1);
				database.closeObject(statement1);
				database.closeObject(connection1);
			}
		}

		cart = new ArrayList();
		session.setAttribute("cart", cart);

		/*
		 * long items = cart.size(); String result = "{\"items\":" + items + "}"; return
		 * Response.status(201).entity(result).build();
		 */

		if (inserteds > 0) {
			message = "{\"success\":\"1\",\"message\":\"Adicionar OK\"}";
			return Response.status(200).entity(message).build();
		} else {
			message = "{\"success\":\"0\",\"message\":\"Error al adicionar\"}";
			return Response.status(400).entity(message).build();
		}
	}

}
