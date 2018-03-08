package controllers.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;

import controllers.AbstractController;


@Controller
@RequestMapping("/comment/administrator")
public class CommentAdministratorController extends AbstractController{
	
	//Constructor
	
	public CommentAdministratorController(){
		
		super();
	}
	
	//Supporting services
	@Autowired
	private CommentService commentService;
	
//	@Autowired
//	private CommentFormService commentFormService;

	
	
	//Delete
		@RequestMapping(value = "/delete", method = RequestMethod.GET)
		public ModelAndView delete(@RequestParam final int commentId) {
			ModelAndView result;
			
			try {
				//Comment comment = this.commentService.findOne(commentId);
				Assert.notNull(this.commentService.findOne(commentId),"message.error.comment.badId");
				this.commentService.delete(commentId);
				result = new ModelAndView("redirect:/rendezvous/administrator/list.do");
			} catch (final Throwable oops) {
				String messageError = "comment.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = new ModelAndView("redirect:/rendezvous/list.do");
				result.addObject("message", messageError);
			}
			return result;
		}
		
}
