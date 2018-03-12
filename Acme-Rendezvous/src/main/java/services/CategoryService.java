
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CategoryRepository;
import domain.Category;
import domain.Service;

@org.springframework.stereotype.Service
@Transactional
public class CategoryService {

	// Managed Repository -----------------------------------------------------

	@Autowired
	private CategoryRepository	categoryRepository;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------

	public CategoryService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Category create() {
		
		Category cat = new Category();
		
		Collection<Service> services = new ArrayList<Service>();
		
		cat.setServices(services);
		
		
		return cat;
	}

	public Category save(final Category category) {
		Assert.notNull(category);
		
		Category result = this.categoryRepository.save(category);

		return result;

	}

	public Category saveFromCreate(final Category category) {
		
		Assert.notNull(category);
		Assert.isTrue(category.getId()==0);
		Assert.notNull(category.getDescription());
		Assert.notNull(category.getName());
		Category result = this.categoryRepository.save(category);
		
		return result;
		
		
	}

	public Category saveFromEdit(final Category category) {
		Assert.isTrue(category.getId()>0);
		Assert.notNull(category);
		Assert.notNull(category.getDescription());
		Assert.notNull(category.getName());
		Category result;
		result = this.categoryRepository.save(category);
		
		return result;
	}
	
	public void delete(Category category){
		
		Assert.notNull(category);
		
		this.categoryRepository.delete(category);
	}

	// Other business methods -------------------------------------------------

	public Collection<Category> findAll() {
		Collection<Category> result = null;
		result = this.categoryRepository.findAll();
		return result;
	}

	public Category findOne(final int categoryId) {
		Category result = null;
		result = this.categoryRepository.findOne(categoryId);
		return result;
	}
	
	public Collection<Category> getCategoriesByParent(int categoryId){
		
		Collection<Category> cate = this.categoryRepository.getCategoriesByParent(categoryId);
		
		return cate;
		
	}
	
public Collection<Category> getRootCategories(){
		
		Collection<Category> cate = this.categoryRepository.getRootCategories();
		
		return cate;
		
	} 
	
//	public Integer getAverageNumberOfCategoriesPerRendezvous(){
//		
//		Integer aver = this.categoryRepository.getAverageNumberOfCategoriesPerRendezvous();
//		
//		return aver;
//	}
	
	public Integer getRatioOfServicesPerEachCategory(){
		
		Integer ratio = this.categoryRepository.getRatioOfServicesPerEachCategory();
		
		return ratio;
	}
}
