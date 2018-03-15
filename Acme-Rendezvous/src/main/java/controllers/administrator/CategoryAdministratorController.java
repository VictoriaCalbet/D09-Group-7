package controllers.administrator;

import java.util.ArrayList;
import java.util.Collection;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import domain.Category;

import domain.form.CategoryForm;

import services.CategoryService;
import services.form.CategoryFormService;


@Controller
@RequestMapping("/category/administrator")
public class CategoryAdministratorController extends AbstractController{
	
	//Constructor

		public CategoryAdministratorController() {

			super();
		}
		
		//Supporting services

		@Autowired
		private CategoryService categoryService;
		
		@Autowired
		private CategoryFormService categoryFormService;
		
		//Methods

		@RequestMapping(value = "/list", method = RequestMethod.GET)
		public ModelAndView list(@RequestParam(required=false) final Integer categoryId) {
			ModelAndView result;
			Collection<Category> categories = new ArrayList<Category>();
			
			
			try {
				
				if(categoryId==null){
				
					categories = this.categoryService.getRootCategories();
					
				}else{
					
					Assert.notNull(this.categoryService.findOne(categoryId),"message.error.category.null");
					
					categories = this.categoryService.getCategoriesByParent(categoryId);
				}
				
				result = new ModelAndView("category/administrator/list");
				result.addObject("categories",categories);

			}catch(Throwable oops){
				
				String messageError = "category.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = new ModelAndView("category/administrator/list");
				categories = this.categoryService.getRootCategories();
				result.addObject("categories",categories);
				result.addObject("message", messageError);
			}
			return result;
			}
		

		@RequestMapping(value = "/create", method = RequestMethod.GET)
		public ModelAndView create() {
			final ModelAndView result;
			CategoryForm categoryForm;

			categoryForm = this.categoryFormService.create();
			result = this.createModelAndView(categoryForm);
			return result;

		}
		
		@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
		public ModelAndView create(@ModelAttribute("categoryForm") @Valid final CategoryForm categoryForm, final BindingResult binding) {

			ModelAndView result;

			if (binding.hasErrors())
				result = this.createModelAndView(categoryForm);
			else
				try {
					this.categoryFormService.saveFromCreate(categoryForm);
					result = new ModelAndView("redirect:/category/administrator/list.do");
				} catch (final Throwable oops) {
					String messageError = "category.commit.error";
					if (oops.getMessage().contains("message.error"))
						messageError = oops.getMessage();
					result = this.createModelAndView(categoryForm, messageError);
				}

			return result;
		}

		// Edit ----------------------------------------------------------------

		@RequestMapping(value = "/edit", method = RequestMethod.GET)
		public ModelAndView edit(@RequestParam final int categoryId) {
			ModelAndView result;
			CategoryForm categoryForm;

			try {
				categoryForm = this.categoryFormService.create(categoryId);
				result = this.editModelAndView(categoryForm);

			} catch (final Throwable oops) {
				result = new ModelAndView("redirect:/category/administrator/list.do");
				result.addObject("message", oops.getMessage());
			}

			return result;

		}
		
		@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
		public ModelAndView edit(@ModelAttribute("categoryForm") @Valid final CategoryForm categoryForm, final BindingResult binding) {
			
			ModelAndView result;

			if (binding.hasErrors())
				result = this.editModelAndView(categoryForm);
			else
				try {
					this.categoryFormService.saveFromEdit(categoryForm);
					result = new ModelAndView("redirect:/category/administrator/list.do");
				} catch (final Throwable oops) {
					String messageError = "category.commit.error";
					if (oops.getMessage().contains("message.error"))
						messageError = oops.getMessage();
					result = this.editModelAndView(categoryForm, messageError);
				}

			return result;
		}

		//Delete
		@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
		public ModelAndView delete(@RequestParam final int categoryId) {
			ModelAndView result;
			try {
				this.categoryService.delete(this.categoryService.findOne(categoryId));
				result = new ModelAndView("redirect:/category/administrator/list.do");
			} catch (final Throwable oops) {
				String messageError = "category.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = new ModelAndView("redirect:/category/administrator/list.do");
				result.addObject("message", messageError);
			}
			return result;
		}

		// Ancillaty methods
		protected ModelAndView createModelAndView(final CategoryForm categoryForm) {
			ModelAndView result;

			result = this.createModelAndView(categoryForm, null);

			return result;
		}

		protected ModelAndView createModelAndView(final CategoryForm categoryForm, final String message) {
			ModelAndView result;
			

			result = new ModelAndView("category/administrator/create");
			result.addObject("categoryForm", categoryForm);
			result.addObject("message", message);
			Collection<Category> categories = this.categoryService.findAll();
			Map<Integer,String> map = this.categoryService.createCategoryLabels(categories);
			result.addObject("categories", categories);
			result.addObject("map",map);

			return result;
		}

		protected ModelAndView editModelAndView(final CategoryForm categoryForm) {
			ModelAndView result;

			result = this.editModelAndView(categoryForm, null);

			return result;
		}

		protected ModelAndView editModelAndView(final CategoryForm categoryForm, final String message) {
			ModelAndView result;


			result = new ModelAndView("category/administrator/edit");
			result.addObject("categoryForm", categoryForm);
			result.addObject("message", message);
			Collection<Category> categories = this.categoryService.findAll();
			Map<Integer,String> map = this.categoryService.createCategoryLabels(categories);
			result.addObject("categories", categories);
			result.addObject("map",map);

			return result;
		}

		
}
