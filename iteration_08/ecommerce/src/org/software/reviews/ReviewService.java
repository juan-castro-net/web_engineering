package org.software.reviews;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.software.purchase.PurchaseService;
import org.software.util.DataBase;

@Path("/review")
public class ReviewService {
	
	@POST
	@Path("/add")
	@Produces("application/json")
	public Response addReview(@Context HttpServletRequest request, Review review) {
		
		PurchaseService purchaseService = new PurchaseService();
		long user_id = purchaseService.getUserId(request);
		
		DataBase database = new DataBase();
		Connection connection1 = null;
		PreparedStatement preparedStatement1 = null;
		String sql = "";
		String message = "";
		int inserteds = 0;
		
		try {
			connection1 = database.getConnection("client");
						
			sql = "INSERT INTO reviews (product_id, user_id, rating, comment)";
			sql += " VALUES (?, ?, ?, ?)";
			
			preparedStatement1 = connection1.prepareStatement(sql); 
			preparedStatement1.setLong(1, review.getProduct_id()); 
			preparedStatement1.setLong(2, user_id);
			preparedStatement1.setDouble(3, review.getRating());
			preparedStatement1.setString(4, review.getComment());
			
			inserteds = preparedStatement1.executeUpdate();
		} 
		catch (Exception e) {
			System.out.println("Error: " + e.toString());
		}
		finally {
			database.closeObject(preparedStatement1);
			database.closeObject(connection1);
		}
		
		if(inserteds > 0){ 
			message = "{\"success\":\"1\", \"message\":\"Review inserted successfully\"}"; 
			return Response.status(200).entity(message).build(); 
		} 
		else{ 
			message = "{\"success\":\"0\", \"message\":\"Error inserting review\"}"; 
			return Response.status(400).entity(message).build(); 
		}
		
	}

	
	@GET
	@Path("/list/{product_id}")
	@Produces("application/json")
	public ReviewList getReviews(@PathParam(value = "product_id") int product_id) {
		ArrayList reviewList = new ArrayList();
		DataBase database = new DataBase();
		Connection connection1 = null;
		Statement statement1 = null;
		ResultSet rs1 = null;
		String sql = "";
		try {
			connection1 = database.getConnection("admin");
			statement1 = connection1.createStatement();
			
			sql = "select * from reviews, users where reviews.user_id = users.id";
			sql = sql + " and reviews.product_id = " + product_id;
			sql = sql + " order by reviews.created_at DESC";
			
			rs1 = statement1.executeQuery(sql);
			while (rs1.next()) {
				long id = rs1.getLong("id");
				long user_id = rs1.getLong("user_id");
				String username = rs1.getString("username");
				//long product_id = rs1.getLong("product_id");
				double rating = rs1.getDouble("rating");
				String comment = rs1.getString("comment");
				Timestamp created_at = rs1.getTimestamp("created_at");
				
				String created_at_text = new SimpleDateFormat("dd/MM/yyyy").format(created_at.getTime());
				
				Review review = new Review();
				review.setId(id);
				review.setUser_id(user_id);
				review.setUsername(username);
				review.setComment(comment);
				review.setRating(rating);
				review.setCreated_at(created_at);
				review.setCreated_at_text(created_at_text);
				reviewList.add(review);
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		}
		finally {
			database.closeObject(rs1);
			database.closeObject(statement1);
			database.closeObject(connection1);
		}
		
		return new ReviewList(reviewList);
	}
}
