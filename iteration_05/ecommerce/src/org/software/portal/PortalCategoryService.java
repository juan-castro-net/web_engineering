package org.software.portal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.software.category.Category;
import org.software.category.CategoryList;
import org.software.util.DataBase;

@Path("/portal")
public class PortalCategoryService {
	@GET
	@Path("/categories")
	@Produces("application/json")
	public CategoryList read() {
		ArrayList<Category> categoryList = new ArrayList<Category>();
		DataBase database = new DataBase();
		Connection connection1 = null;
		Statement statement1 = null;
		ResultSet rs1 = null;
		String sql = "";
		try {
			connection1 = database.getConnection("guest");
			statement1 = connection1.createStatement();
			sql = "select * from categories where published = 1";
			sql += " order by name";
			rs1 = statement1.executeQuery(sql);
			while (rs1.next()) {
				int id = rs1.getInt("id");
				int published = rs1.getInt("published");
				String name = rs1.getString("name");
				String icon = rs1.getString("icon");
				Category category = new Category();
				category.setId(id);
				category.setPublished(published);
				category.setName(name);
				category.setIcon(icon);
				categoryList.add(category);
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		} finally {
			database.closeObject(rs1);
			database.closeObject(statement1);
			database.closeObject(connection1);

		}
		return new CategoryList(categoryList);
	}
}
