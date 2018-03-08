// Tag Controller

package controllers;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import domain.Category;

@Controller
@RequestMapping("/category")
public class CategoryController extends AbstractController {

	//Services
	@Autowired
	private CategoryService	categoryService;


	//Constructor
	public CategoryController() {

		super();
	}

	//Listing as a non-admin actor
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final Integer categoryId) {

		final ModelAndView result = new ModelAndView("category/list");

		Collection<Category> categories = new HashSet<Category>();

		if (categoryId == null)
			categories = this.categoryService.browseChildCategories(this.categoryService.findAll().iterator().next().getId());
		else
			try {
				Assert.notNull(this.categoryService.findById(categoryId),"message.error.category.null");
				categories = this.categoryService.browseChildCategories(categoryId); // TODO Try-catch
			} catch (final Throwable oops) {
				String messageError = "category.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				categories = this.categoryService.browseChildCategories(this.categoryService.findAll().iterator().next().getId());
				result.addObject("message", messageError);
			}
		
		result.addObject("categories", categories);
		result.addObject("requestURI", "category/list.do");

		return result;
	}

	// Ancillaty methods
	protected ModelAndView createEditModelAndView(final Category category) {

		ModelAndView result;

		result = this.createEditModelAndView(category, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Category category, final String message) {
		ModelAndView result;

		result = new ModelAndView("category/edit");
		result.addObject("category", category);
		result.addObject("message", message);

		return result;
	}

}
