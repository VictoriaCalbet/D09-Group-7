
package services.form;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


import domain.Administrator;
import domain.Category;

import domain.form.CategoryForm;
import services.AdministratorService;
import services.CategoryService;

@Service
@Transactional
public class CategoryFormService {

	// Managed repository -----------------------------------------------------

	// Supporting services ----------------------------------------------------

	@Autowired
	private CategoryService	categoryService;

	@Autowired
	private AdministratorService	administratorService;
	// Constructors -----------------------------------------------------------

	public CategoryFormService() {
		super();
	}
	
	public CategoryForm create() {
		CategoryForm result;

		result = new CategoryForm();

		return result;
	}
	
	public CategoryForm create(int categoryId) {
		
		Assert.notNull(this.categoryService.findOne(categoryId));
		
		CategoryForm result;
		final Category category = this.categoryService.findOne(categoryId);

		result = new CategoryForm();
		
		result.setCategoryId(category.getId());
		result.setName(category.getName());
		result.setDescription(category.getDescription());
		result.setParent(category.getParent());

		return result;
	}
	
	public Category saveFromCreate(final CategoryForm categoryForm) {
		Assert.notNull(categoryForm, "message.error.categoryForm.null");
		Assert.notNull(categoryForm.getName(), "message.error.categoryForm.name.null");
		Assert.notNull(categoryForm.getDescription(), "message.error.categoryForm.description.null");
		Assert.isTrue(categoryForm.getCategoryId() == 0, "message.error.categoryForm.id");
		
		final Administrator principal = this.administratorService.findByPrincipal();
		Assert.notNull(principal,"message.error.categoryForm.notAnAdmin");
		
		
		final Category result;
		final Category category;
		
		category = this.categoryService.create();
		
		category.setParent(categoryForm.getParent());
		category.setName(categoryForm.getName());
		category.setDescription(categoryForm.getDescription());

		result = this.categoryService.saveFromCreate(category);

		return result;
	}

	public Category saveFromEdit(final CategoryForm categoryForm) {
		Assert.notNull(categoryForm);
		Assert.notNull(categoryForm.getName(), "message.error.categoryForm.name.null");
		if(categoryForm.getParent()!=null){
		Assert.isTrue(categoryForm.getCategoryId()!=categoryForm.getParent().getId(),"message.error.category.InvalidParent");
		}
		final Administrator principal = this.administratorService.findByPrincipal();
		
		Assert.notNull(principal,"message.error.categoryForm.notAnAdmin");

		final Category result;
		final Category category;

		category = this.categoryService.findOne(categoryForm.getCategoryId());

		category.setName(categoryForm.getName());
		category.setDescription(categoryForm.getDescription());
		category.setParent(categoryForm.getParent());
		
		result = this.categoryService.saveFromEdit(category);

		return result;
	}
	
}
