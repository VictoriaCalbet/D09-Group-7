
package services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

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
		Assert.isTrue(this.checkCreditCard(request.getCreditCard()));
		Assert.isTrue(!request.getRendezvous().getRequests().contains(request));
		final User principal = this.userService.findByPrincipal();
		Assert.isTrue(principal.getRendezvoussesCreated().contains(request.getRendezvous()));
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
	public void flush() {
		this.requestRepository.flush();
	}

	private boolean checkCreditCard(final CreditCard creditCard) {
		Assert.notNull(creditCard);
		Assert.notNull(creditCard.getHolderName());
		Assert.notNull(creditCard.getBrandName());
		Assert.notNull(creditCard.getNumber());
		Assert.notNull(creditCard.getExpirationMonth());
		Assert.notNull(creditCard.getExpirationYear());
		Assert.notNull(creditCard.getCvv());

		boolean result = false;
		Date now = null;

		now = new Date();
		final Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		final int year = cal.get(Calendar.YEAR);
		final int month = cal.get(Calendar.MONTH) + 1;

		if (creditCard.getExpirationYear() > year)
			result = true;
		else if (creditCard.getExpirationYear() == year) {
			if (creditCard.getExpirationMonth() >= month)
				result = true;
			else
				result = false;
		} else
			result = false;

		return result;
	}
}
