
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

	@Autowired
	private ServiceService	serviceService;
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
		Assert.notNull(category,"message.error.category.null");
		
		Category result = this.categoryRepository.save(category);

		return result;

	}

	public Category saveFromCreate(final Category category) {
		
		Assert.notNull(category,"message.error.category.null");
		Assert.isTrue(category.getId()==0,"message.error.category.id");
		Assert.notNull(category.getDescription(),"message.error.category.description.null");
		Assert.notNull(category.getName(),"message.error.category.name.null");
		Category result = this.categoryRepository.save(category);
		
		return result;
		
		
	}

	public Category saveFromEdit(final Category category) {
		Assert.isTrue(category.getId()>0,"message.error.category.id");
		Assert.notNull(category,"message.error.category.null");
		Assert.notNull(category.getDescription(),"message.error.category.description.null");
		Assert.notNull(category.getName(),"message.error.category.name.null");
		Category result;
		result = this.categoryRepository.save(category);
		
		return result;
	}
	
	public void delete(Category category){
		
		Assert.notNull(category,"message.error.category.null");
		
		Collection<Service> services = category.getServices();
		
		for(Service ser:services){
			
			ser.getCategories().remove(category);
			this.serviceService.save(ser);
		}
		
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
	
	public Map<Integer,String> createCategoryLabels(Collection<Category> categories){
		
		Map<Integer,String> map = new HashMap<Integer,String>();
		
		String label="";
		
		for(Category ca:categories){
			int id=ca.getId();
			label=ca.getName();
			Category parent=ca.getParent();
			
			while(parent!=null){

				label=parent.getName()+">"+label;
				ca=parent;
				parent=ca.getParent();
				
			}
			
			map.put(id, label);
			
			}

		
		
		return map;
	}


	public Integer getRatioOfServicesPerEachCategory(){
		
		Integer ratio = this.categoryRepository.getRatioOfServicesPerEachCategory();
		
		return ratio;
	}
}
