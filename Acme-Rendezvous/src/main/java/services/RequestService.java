
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.RequestRepository;
import domain.CreditCard;
import domain.Rendezvous;
import domain.Request;

@Service
@Transactional
public class RequestService {

	// Managed Repository -----------------------------------------------------

	@Autowired
	private RequestRepository	requestRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private ServiceService		serviceService;


	// Constructors -----------------------------------------------------------

	public RequestService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Request create() {
		final Request result = new Request();
		final CreditCard creditCard = new CreditCard();
		final Rendezvous rendezvous = new Rendezvous();
		final String comments = "";
		result.setCreditCard(creditCard);
		result.setComments(comments);
		result.setRendezvous(rendezvous);
		return result;
	}
	public Request save(final Request request) {
		Assert.notNull(request);
		Request result;

		result = this.requestRepository.save(request);

		return result;

	}

	public Request saveFromCreate(final Request request) {
		final domain.Service service = this.serviceService.findOne(request.getService().getId());
		Assert.notNull(service);
		Request result = this.create();

		Assert.isTrue(request.getRendezvous().getIsDraft() == false);
		Assert.isTrue(request.getRendezvous().getIsDeleted() == false);
		Assert.isTrue(request.getService().getIsInappropriate() == false);

		result = this.requestRepository.save(request);
		return result;
	}

	public Request saveFromEdit(final Request request) {
		Assert.notNull(request);

		final Request result = this.requestRepository.save(request);

		return result;

	}
	// Other business methods -------------------------------------------------

	public Collection<Request> findAll() {
		Collection<Request> result = null;
		result = this.requestRepository.findAll();
		return result;
	}

	public Request findOne(final int requestId) {
		Request result = null;
		result = this.requestRepository.findOne(requestId);
		return result;
	}

}
