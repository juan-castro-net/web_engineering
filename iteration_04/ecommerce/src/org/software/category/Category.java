package org.software.category;

import java.sql.Statement;

public class Category {
	private long id;
	private int published;
	private String name;
	private String icon;

	public Category() {
		super();
	}

	public Category(long id, int published, String name, String icon) {
		super();
		this.id = id;
		this.published = published;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
