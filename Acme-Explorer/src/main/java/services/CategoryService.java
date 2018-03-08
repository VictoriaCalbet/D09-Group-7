
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CategoryRepository;
import domain.Administrator;
import domain.Category;
import domain.Trip;

@Service
@Transactional
public class CategoryService {

	// Managed Repository
	@Autowired
	private CategoryRepository		categoryRepository;

	// Supporting Services
	@Autowired
	private MessageService			messageService;

	@Autowired
	private AdministratorService	administratorService;


	// Constructor

	public CategoryService() {
		super();
	}

	// Simple CRUD methods

	public Collection<Category> findAll() {

		final Collection<Category> cat = this.categoryRepository.findAll();

		return cat;
	}

	public Category findById(final Integer id) {

		final Category c = this.categoryRepository.findOne(id);
		return c;
	}

	public Category create() {
		final Administrator admin = this.administratorService.findByPrincipal();
		Assert.notNull(admin, "message.error.category.login");
		final Category c = new Category();
		final Collection<Trip> trips = new ArrayList<Trip>();
		c.setTrips(trips);

		return c;
	}

	public Category save(final Category c) {
		final Administrator admin = this.administratorService.findByPrincipal();
		Boolean isSuspicious;

		Assert.notNull(admin, "message.error.category.login");

		Assert.notNull(c, "message.error.category.null");
		Assert.notNull(c.getName(), "message.error.category.name");
		
		if(c.getParent()!=null){
		Assert.isTrue(c.getId()!=c.getParent().getId(),"message.error.category.sameIdParentChild");
		}
		isSuspicious = admin.getIsSuspicious();
		isSuspicious = isSuspicious || this.messageService.checkSpam(c.getName());

		if (c.getParent() != null)
			isSuspicious = isSuspicious || this.messageService.checkSpam(c.getParent().getName());
		admin.setIsSuspicious(isSuspicious);

		this.administratorService.saveFromEdit(admin);

		final Category parent = c.getParent();

		if (parent != null) {
			final Collection<Category> allCat = this.categoryRepository.findAllChildren(parent.getId());

			for (final Category cat : allCat)
				Assert.isTrue(!cat.getParent().equals(c.getParent()) || !cat.getName().equals(c.getName()), "message.error.category.parent");
		}
		final Category saved = this.categoryRepository.save(c);
		return saved;
	}

	public void delete(final Category c) {
		Assert.notNull(c, "message.error.category.null");
		final Administrator admin = this.administratorService.findByPrincipal();
		Assert.notNull(admin, "message.error.category.login");
		this.categoryRepository.delete(c);

	}

	// Other business methods

	public Collection<Trip> browseTripsByCategory(final Category c) {

		final Collection<Trip> trips = this.categoryRepository.browseTripsByCategory(c.getId());

		return trips;
	}

	public Collection<Category> browseChildCategories(final int categoryId) {

		final Collection<Category> categories = this.categoryRepository.findAllChildren(categoryId);

		return categories;
	}

	public Collection<Category> findAllWithOutCATEGORY() {
		Collection<Category> result;

		result = this.categoryRepository.findAllWithOutCATEGORY();

		return result;
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

}
