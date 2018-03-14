
package services.form;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import services.RequestService;
import services.UserService;
import domain.Request;
import domain.User;
import domain.form.RequestForm;

@Service
@Transactional
public class RequestFormService {

	// Managed repository -----------------------------------------------------

	// Supporting services ----------------------------------------------------

	@Autowired
	private RequestService	requestService;

	@Autowired
	private UserService		userService;


	// Constructors -----------------------------------------------------------

	public RequestFormService() {
		super();
	}

	public RequestForm create() {
		RequestForm result;

		result = new RequestForm();

		return result;
	}

	public RequestForm create(final int requestId) {
		final Request r = this.requestService.findOne(requestId);
		final User u = this.userService.findByPrincipal();

		final RequestForm requestForm = new RequestForm();
		requestForm.setComment(r.getComment());
		requestForm.setCreditCard(r.getCreditCard());
		requestForm.setId(r.getId());
		requestForm.setRendezvous(r.getRendezvous());
		requestForm.setService(r.getService());
		return requestForm;
	}

	public Request saveFromCreate(final RequestForm requestForm) {

		final Request r = this.requestService.create();

		r.setComment(requestForm.getComment());
		r.setCreditCard(requestForm.getCreditCard());
		r.setId(requestForm.getId());
		r.setRendezvous(requestForm.getRendezvous());
		r.setService(requestForm.getService());
		final Request requestSave = this.requestService.saveFromCreate(r);

		return requestSave;

	}

}
