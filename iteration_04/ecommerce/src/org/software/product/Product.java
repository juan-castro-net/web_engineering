package org.software.product;

public class Product {
	long id;
	int published;
	int category_id;
	String name;
	double pricing;
	String short_description;
	String long_description;
	String icon;

	public Product() {
		super();
	}

	public Product(long id, int published, int category_id, String name, double pricing, String short_description,
			String long_description, String icon) {
		super();
		this.id = id;
		this.published = published;
		this.category_id = category_id;
		this.name = name;
		this.pricing = pricing;
		this.short_description = short_description;
		this.long_description = long_description;
		this.icon = icon;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getPublished() {
		return published;
	}

	public void setPublished(int published) {
		this.published = published;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPricing() {
		return pricing;
	}

	public void setPricing(double pricing) {
		this.pricing = pricing;
	}

	public String getShort_description() {
		return short_description;
	}

	public void setShort_description(String short_description) {
		this.short_description = short_description;
	}

	public String getLong_description() {
		return long_description;
	}

	public void setLong_description(String long_description) {
		this.long_description = long_description;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
