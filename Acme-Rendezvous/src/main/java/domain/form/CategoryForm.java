
package domain.form;

import domain.Category;

public class CategoryForm {

	private int	categoryId;
	private String name;
	private String description;
	private Category parent;


	public int getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(final int categoryId) {
		this.categoryId = categoryId;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}
	
	public Category getParent() {
		return this.parent;
	}

	public void setParent(final Category parent) {
		this.parent = parent;
	}

}
