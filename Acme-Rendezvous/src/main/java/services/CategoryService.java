
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
	private ServiceService		serviceService;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------

	public CategoryService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Category create() {

		final Category cat = new Category();

		final Collection<Service> services = new ArrayList<Service>();

		cat.setServices(services);

		return cat;
	}

	public Category save(final Category category) {
		Assert.notNull(category, "message.error.category.null");

		final Category result = this.categoryRepository.save(category);

		return result;

	}

	public Category saveFromCreate(final Category category) {

		Assert.notNull(category, "message.error.category.null");
		Assert.isTrue(category.getId() == 0, "message.error.category.id");
		Assert.notNull(category.getDescription(), "message.error.category.description.null");
		Assert.notNull(category.getName(), "message.error.category.name.null");
		
		final Category result = this.categoryRepository.save(category);

		return result;

	}

	public Category saveFromEdit(final Category category) {
		Assert.isTrue(category.getId() > 0, "message.error.category.id");
		Assert.notNull(category, "message.error.category.null");
		Assert.notNull(category.getDescription(), "message.error.category.description.null");
		Assert.notNull(category.getName(), "message.error.category.name.null");
		Category result;
		result = this.categoryRepository.save(category);

		return result;
	}

	public void delete(final Category category) {

		Assert.notNull(category, "message.error.category.null");

		final Collection<Service> services = category.getServices();

		for (final Service ser : services) {

			ser.getCategories().remove(category);
			this.serviceService.save(ser);

		}
		final Collection<Category> categories = this.replaceParentCategories(category.getId());

		for (final Category cat : categories) {

			cat.setParent(null);
			this.categoryRepository.save(cat);
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

	public Collection<Category> getCategoriesByParent(final int categoryId) {

		final Collection<Category> cate = this.categoryRepository.getCategoriesByParent(categoryId);

		return cate;

	}

	public Collection<Category> replaceParentCategories(final int categoryId) {

		final Collection<Category> result = this.categoryRepository.replaceParentCategories(categoryId);

		return result;

	}

	public Collection<Category> getRootCategories() {

		final Collection<Category> cate = this.categoryRepository.getRootCategories();

		return cate;

	}

	public Map<Integer, String> createCategoryLabels(final Collection<Category> categories) {

		final Map<Integer, String> map = new HashMap<Integer, String>();

		String label = "";

		for (Category ca : categories) {
			final int id = ca.getId();
			label = ca.getName();
			Category parent = ca.getParent();

			while (parent != null) {

				label = parent.getName() + ">" + label;
				ca = parent;
				parent = ca.getParent();

			}

			map.put(id, label);

		}

		return map;
	}

	// Dashboard services ------------------------------------------------------

	// Acme-Rendezvous 2.0 - Requisito 11.2.1
	public Double findAvgCategoriesCreatedPerRendezvous() {
		return this.categoryRepository.findAvgCategoriesCreatedPerRendezvous();
	}

	// Acme-Rendezvous 2.0 - Requisito 11.2.2
	public Double getAvgOfServicesPerEachCategory() {
		return this.categoryRepository.getAvgOfServicesPerEachCategory();
	}

	public Double getRatioOfServicesPerEachCategory() {
		return this.categoryRepository.getRatioOfServicesPerEachCategory();
	}
	
	public void flush(){
		
		this.categoryRepository.flush();
	}
}
