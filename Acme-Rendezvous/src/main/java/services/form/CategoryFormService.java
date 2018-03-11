
package services.form;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import services.CategoryService;

@Service
@Transactional
public class CategoryFormService {

	// Managed repository -----------------------------------------------------

	// Supporting services ----------------------------------------------------

	@Autowired
	private CategoryService	categoryService;


	// Constructors -----------------------------------------------------------

	public CategoryFormService() {
		super();
	}

}
