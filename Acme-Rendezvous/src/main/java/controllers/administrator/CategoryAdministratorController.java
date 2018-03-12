package controllers.administrator;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Category;

import services.CategoryService;


@Controller
@RequestMapping("/category/administrator")
public class CategoryAdministratorController {
	
	//Constructor

		public CategoryAdministratorController() {

			super();
		}
		
		//Supporting services

		@Autowired
		private CategoryService categoryService;
		
		//Methods

		@RequestMapping(value = "/list", method = RequestMethod.GET)
		public ModelAndView list(@RequestParam final int categoryId) {
			ModelAndView result;
			Collection<Category> categories = new ArrayList<Category>();
			
			try {
				
				Category cat = this.categoryService.findOne(categoryId);
				
				if(cat==null){
				
					categories = this.categoryService.getRootCategories();
					
				}else{
					
					categories = this.categoryService.getCategoriesByParent(categoryId);
				}
				
				result = new ModelAndView("category/administrator/list");
				result.addObject("categories",categories);
				
				
				
			}catch(Throwable oops){
				
				String messageError = "category.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = new ModelAndView("redirect:/category/administrator/list.do");
				
				result.addObject("message", messageError);
			}
			return result;
			}

}
