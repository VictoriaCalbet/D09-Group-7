//Tag Controller

package controllers.administrator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;

import controllers.AbstractController;
import domain.Category;

@Controller
@RequestMapping("/category/administrator")
public class CategoryAdministratorController extends AbstractController{

	//Services
	@Autowired
	private CategoryService categoryService;
	
	//Constructor
	public CategoryAdministratorController(){
		
		super();
	}
		
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final Integer categoryId){
		
		ModelAndView result = new ModelAndView("category/list");
		
		Collection<Category> categories = new HashSet<Category>();
		
		if (categoryId == null) {
			categories = this.categoryService.browseChildCategories(this.categoryService.findAll().iterator().next().getId());
		} else
			try {
				Assert.notNull(this.categoryService.findById(categoryId),"message.error.category.null");
				categories = this.categoryService.browseChildCategories(categoryId); // TODO Try-catch
			}catch (final Throwable oops) {
				String messageError = "category.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result.addObject("message", messageError);
				categories = this.categoryService.browseChildCategories(this.categoryService.findAll().iterator().next().getId());
				
			}
	
		result.addObject("categories",categories);
		result.addObject("requestURI", "category/administrator/list.do");
		
		return result;
	}
	
	//Create a category
	
	@RequestMapping(value="/create", method=RequestMethod.GET)
	public ModelAndView create(){
		
		ModelAndView result;
		Category category;
		
		category = this.categoryService.create();
		result = this.createEditModelAndView(category);
		
		return result;
		
	}
	
	//Edit a category
	
	@RequestMapping(value="/edit", method=RequestMethod.GET)
	public ModelAndView editCategory(@RequestParam final int categoryId){
		
		ModelAndView result;
		try{
			
			Category category = this.categoryService.findById(categoryId);
			Assert.notNull(category,"message.error.category.null");
			result = createEditModelAndView(category);
		}catch(Throwable oops){
			
			String messageError = "message.error.category.null";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			
			result = new ModelAndView("category/list");
			
			Collection<Category> categories = this.categoryService.browseChildCategories(this.categoryService.findAll().iterator().next().getId());
			result.addObject("categories",categories);
			result.addObject("requestURI", "category/administrator/list.do");
			result.addObject("message",messageError);
			
		}
		//Checks that the found category isn't null. This way, if an id which isn't a category's id is given in the url, it'll raise an exception
		
		
		return result;
	}
	
	//Save a category
	@RequestMapping(value="/edit", method=RequestMethod.POST, params="save")
	public ModelAndView save(@Valid Category category, BindingResult binding){
		
		ModelAndView result;
		
		if(binding.hasErrors()){
			
			result = this.createEditModelAndView(category);
		}else{
			
			try{
				
				this.categoryService.save(category);
				result = new ModelAndView("redirect:list.do");
			}catch(final Throwable oops){
				
				String messageError = "category.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(category, messageError);
				
			}
		}
	return result;	
	}
	
	//Delete a category
		@RequestMapping(value="/edit", method=RequestMethod.POST, params="delete")
		public ModelAndView delete(Category category, BindingResult binding){
			
			ModelAndView result;
			
			if(binding.hasErrors()){
				
				result = this.createEditModelAndView(category);
			}else{
				
				try{
					
					this.categoryService.delete(category);
					result = new ModelAndView("redirect:list.do");
				}catch(final Throwable oops){
					
					String messageError = "category.commit.error";
					if (oops.getMessage().contains("message.error"))
						messageError = oops.getMessage();
					result = this.createEditModelAndView(category, messageError);
					
				}
			}
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

			result = new ModelAndView("category/administrator/edit");
			result.addObject("category", category);
			result.addObject("message", message);
			Collection<Category> categories = this.categoryService.findAll();
			Map<Integer,String> map = this.categoryService.createCategoryLabels(categories);
			result.addObject("categories", categories);
			result.addObject("map",map);

			return result;
		}
	
}
