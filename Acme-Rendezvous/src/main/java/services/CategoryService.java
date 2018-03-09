
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CategoryRepository;
import domain.Category;

@Service
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
		return null;
	}

	public Category save(final Category category) {
		Assert.notNull(category);
		Category result;

		result = this.categoryRepository.save(category);

		return result;

	}

	public Category saveFromCreate(final Category category) {
		return null;
	}

	public Category saveFromEdit(final Category category) {
		return null;
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
}
