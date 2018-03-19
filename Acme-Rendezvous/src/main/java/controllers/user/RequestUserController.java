
package controllers.user;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.RendezvousService;
import services.ServiceService;
import services.UserService;
import services.form.RequestFormService;
import controllers.AbstractController;
import domain.Rendezvous;
import domain.Service;
import domain.User;
import domain.form.RequestForm;

@Controller
@RequestMapping("/request/user")
public class RequestUserController extends AbstractController {

	@Autowired
	private RequestFormService	requestFormService;

	@Autowired
	private ServiceService		serviceService;

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private UserService			userService;


	// Constructors -----------------------------------------------------------

	public RequestUserController() {
		super();
	}

	//Creating 
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(final HttpServletRequest request, @RequestParam final int rendezvousId) {
		ModelAndView result;
		RequestForm requestForm;
		requestForm = this.requestFormService.create();
		final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);
		requestForm.setRendezvous(rendezvous);
		result = this.createEditModelAndView(requestForm);
		final User principal = this.userService.findByPrincipal();
		final Collection<Rendezvous> rendezvousesCreated = this.rendezvousService.findAllAvailableRendezvousesCreatedByUserId(principal.getId());
		final Collection<Service> availableServices = this.serviceService.findAvailableServicesToRequest(rendezvousId);

		try {
			Assert.notEmpty(this.serviceService.findAvailableServicesToRequest(rendezvous.getId()), "message.error.noAvailableServices");

		} catch (final Throwable oops) {
			String messageError = "message.error.noAvailableServices";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result = this.listModelAndView("redirect:/rendezvous/user/list.do", messageError);

		}

		result.addObject("rendezvous", rendezvous);
		result.addObject("availableServices", availableServices);
		result.addObject("rendezvousesCreated", rendezvousesCreated);
		return this.readCookies(request, result);

	}
	//EDITIONS
	//Editing
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(final HttpServletRequest request, @RequestParam final int requestId) {
		final ModelAndView result;
		RequestForm requestForm;
		requestForm = this.requestFormService.create(requestId);

		result = this.createEditModelAndView(requestForm);
		result.addObject(requestId);

		return this.readCookies(request, result);
	}

	//Saving
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final HttpServletResponse response, @Valid final RequestForm requestForm, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(requestForm);
		else
			try {
				User user;
				user = this.userService.findByPrincipal();
				this.requestFormService.saveFromCreate(requestForm);
				result = new ModelAndView("redirect:/rendezvous/user/list.do");
				response.addCookie(new Cookie("userCookie" + (this.encrypt(new Integer(user.getId()).toString(), user.getId())), this.encrypt(new Integer(user.getId()).toString(), user.getId())));
				response.addCookie(new Cookie("brandCookie" + (this.encrypt(new Integer(user.getId()).toString(), user.getId())), this.encrypt(requestForm.getCreditCard().getBrandName(), user.getId())));
				response.addCookie(new Cookie("holderCookie" + (this.encrypt(new Integer(user.getId()).toString(), user.getId())), this.encrypt(requestForm.getCreditCard().getHolderName(), user.getId())));
				response.addCookie(new Cookie("numberCookie" + (this.encrypt(new Integer(user.getId()).toString(), user.getId())), this.encrypt(requestForm.getCreditCard().getNumber(), user.getId())));
				response.addCookie(new Cookie("monthCookie" + (this.encrypt(new Integer(user.getId()).toString(), user.getId())), this.encrypt(new Integer(requestForm.getCreditCard().getExpirationMonth()).toString(), user.getId())));
				response.addCookie(new Cookie("yearCookie" + (this.encrypt(new Integer(user.getId()).toString(), user.getId())), this.encrypt(new Integer(requestForm.getCreditCard().getExpirationYear()).toString(), user.getId())));
				response.addCookie(new Cookie("cvvCookie" + (this.encrypt(new Integer(user.getId()).toString(), user.getId())), this.encrypt(new Integer(requestForm.getCreditCard().getCvv()).toString(), user.getId())));
			} catch (final Throwable oops) {
				String messageError = "request.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(requestForm, messageError);
			}

		return result;
	}

	//Ancillary methods

	protected ModelAndView createEditModelAndView(final RequestForm requestForm) {
		ModelAndView result;

		result = this.createEditModelAndView(requestForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final RequestForm requestForm, final String messageCode) {
		ModelAndView result;
		final Rendezvous rendezvous = requestForm.getRendezvous();
		final Service service = requestForm.getService();
		final Collection<Service> availableServices = this.serviceService.findAvailableServicesToRequest(requestForm.getRendezvous().getId());

		result = new ModelAndView("request/user/edit");
		result.addObject("requestForm", requestForm);
		result.addObject("service", service);
		result.addObject("availableServices", availableServices);
		result.addObject("rendezvous", rendezvous);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "request/user/edit.do");
		return result;
	}
	protected ModelAndView listModelAndView(final String string) {
		ModelAndView result;

		result = this.listModelAndView(string, null);

		return result;
	}

	protected ModelAndView listModelAndView(final String string, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("redirect:/rendezvous/user/list.do");
		result.addObject("string", string);

		result.addObject("message", messageCode);
		//		result.addObject("requestURI", "rendezvous/user/edit.do");
		return result;
	}

	private ModelAndView readCookies(final HttpServletRequest request, final ModelAndView result) {
		User user;
		user = this.userService.findByPrincipal();
		Cookie[] cookies;
		cookies = request.getCookies();
		String brandCookie;
		brandCookie = "";
		String holderCookie;
		holderCookie = "";
		String numberCookie;
		numberCookie = "";
		String monthCookie;
		monthCookie = "";
		String yearCookie;
		yearCookie = "";
		String cvvCookie;
		cvvCookie = "";
		String userCookie;
		userCookie = "";
		for (final Cookie c : cookies) {
			if (c.getName().equals("brandCookie" + this.encrypt(new Integer(user.getId()).toString(), user.getId())))
				brandCookie = c.getValue();
			if (c.getName().equals("userCookie" + this.encrypt(new Integer(user.getId()).toString(), user.getId())))
				userCookie = c.getValue();
			if (c.getName().equals("holderCookie" + this.encrypt(new Integer(user.getId()).toString(), user.getId())))
				holderCookie = c.getValue();
			if (c.getName().equals("numberCookie" + this.encrypt(new Integer(user.getId()).toString(), user.getId())))
				numberCookie = c.getValue();
			if (c.getName().equals("monthCookie" + this.encrypt(new Integer(user.getId()).toString(), user.getId())))
				monthCookie = c.getValue();
			if (c.getName().equals("yearCookie" + this.encrypt(new Integer(user.getId()).toString(), user.getId())))
				yearCookie = c.getValue();
			if (c.getName().equals("cvvCookie" + this.encrypt(new Integer(user.getId()).toString(), user.getId())))
				cvvCookie = c.getValue();
		}
		if (userCookie != "" && new Integer(this.decrypt(userCookie, user.getId())).equals(new Integer(user.getId()))) {
			result.addObject("brandCookie", this.decrypt(brandCookie, user.getId()));
			result.addObject("holderCookie", this.decrypt(holderCookie, user.getId()));
			result.addObject("numberCookie", this.decrypt(numberCookie, user.getId()));
			result.addObject("monthCookie", this.decrypt(monthCookie, user.getId()));
			result.addObject("yearCookie", this.decrypt(yearCookie, user.getId()));
			result.addObject("cvvCookie", this.decrypt(cvvCookie, user.getId()));
		} else {
			result.addObject("brandCookie", "");
			result.addObject("holderCookie", "");
			result.addObject("numberCookie", "");
			result.addObject("monthCookie", "");
			result.addObject("yearCookie", "");
			result.addObject("cvvCookie", "");
		}
		return result;
	}
	private String encrypt(final String strClearText, final int userId) {

		String strData = "";
		final byte[] bytes;
		bytes = strClearText.getBytes();
		byte[] encrypted;
		encrypted = new byte[bytes.length];
		Random randomGenerator;
		randomGenerator = new Random(userId);
		for (int i = 0; i < bytes.length; i++)
			encrypted[i] = (byte) (bytes[i] + randomGenerator.nextInt(27));

		try {
			strData = new String(Base64.encode(encrypted));
			strData = URLEncoder.encode(strData, "UTF-8");
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return strData;

	}

	private String decrypt(final String strCodeText, final int userId) {
		String strData = "";
		String decode;
		try {
			decode = URLDecoder.decode(strCodeText, "UTF-8");
			byte[] encrypted;

			encrypted = Base64.decode(decode.getBytes());
			byte[] decrypted;
			decrypted = new byte[encrypted.length];
			Random randomGenerator;
			randomGenerator = new Random(userId);
			for (int i = 0; i < encrypted.length; i++)
				decrypted[i] = (byte) (encrypted[i] - randomGenerator.nextInt(27));
			strData = new String(decrypted);

		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return strData;

	}

}
