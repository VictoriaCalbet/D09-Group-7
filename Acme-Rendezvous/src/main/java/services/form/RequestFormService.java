
package services.form;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import services.RequestService;
import domain.Request;
import domain.form.RequestForm;

@Service
@Transactional
public class RequestFormService {

	// Managed repository -----------------------------------------------------

	// Supporting services ----------------------------------------------------

	@Autowired
	private RequestService	requestService;


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

		final RequestForm requestForm = new RequestForm();
		requestForm.setComments(r.getComments());
		requestForm.setCreditCard(r.getCreditCard());
		requestForm.setId(r.getId());
		requestForm.setRendezvous(r.getRendezvous());
		requestForm.setService(r.getService());

		return requestForm;
	}
	public Request saveFromCreate(final RequestForm requestForm) {

		final Request r = this.requestService.create();

		r.setComments(requestForm.getComments());
		r.setCreditCard(requestForm.getCreditCard());
		r.setId(requestForm.getId());
		r.setRendezvous(requestForm.getRendezvous());
		r.setService(requestForm.getService());

		Assert.isTrue(!requestForm.getRendezvous().getRequests().contains(requestForm), "message.error.request.alreadyRequested");

		final Request requestSave = this.requestService.saveFromCreate(r);

		return requestSave;

	}

}
