
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.RequestRepository;
import domain.CreditCard;
import domain.Rendezvous;
import domain.Request;
import domain.User;

@Service
@Transactional
public class RequestService {

	// Managed Repository -----------------------------------------------------

	@Autowired
	private RequestRepository	requestRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private ServiceService		serviceService;
	@Autowired
	private UserService			userService;
	@Autowired
	private RendezvousService	RendezvousService;


	// Constructors -----------------------------------------------------------

	public RequestService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Request create() {
		final Request result = new Request();
		final CreditCard creditCard = new CreditCard();
		final Rendezvous rendezvous = new Rendezvous();
		final domain.Service service = new domain.Service();
		final String comment = "";
		result.setCreditCard(creditCard);
		result.setComment(comment);
		result.setRendezvous(rendezvous);
		result.setService(service);
		return result;
	}
	public Request save(final Request request) {
		Assert.notNull(request);
		Request result;

		result = this.requestRepository.save(request);

		return result;

	}

	public Request saveFromCreate(final Request request) {
		Assert.notNull(request);
		final Request result = request;
		Assert.isTrue(request.getRendezvous().getIsDraft() == false);
		Assert.isTrue(request.getRendezvous().getIsDeleted() == false);
		Assert.isTrue(request.getService().getIsInappropriate() == false);
		this.save(result);
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

	public void requestService(final int serviceId) {
		final domain.Service service = this.serviceService.findOne(serviceId);
		Assert.notNull(service);
		final Rendezvous rendezvous = this.RendezvousService.findRendezvousByService(serviceId);
		Assert.notNull(rendezvous);
		final User creator = rendezvous.getCreator();
		Assert.notNull(creator);
		final User principal = this.userService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(creator == principal);

		final Request request = this.create();
		this.saveFromCreate(request);
		Collection<Request> requests = new ArrayList<Request>();
		requests = service.getRequests();
		requests.add(request);

		request.setRendezvous(rendezvous);
		service.setRequests(requests);
		rendezvous.setRequests(requests);

	}
}
