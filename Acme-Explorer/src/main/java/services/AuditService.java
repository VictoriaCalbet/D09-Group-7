
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AuditRepository;
import domain.Audit;
import domain.Auditor;
import domain.Trip;

@Service
@Transactional
public class AuditService {

	// Managed Repository
	@Autowired
	private AuditRepository	auditRepository;

	@Autowired
	private AuditorService	auditorService;
	@Autowired
	private TripService		tripService;
	@Autowired
	private MessageService	messageService;


	// Supporting Services

	// Constructor

	public AuditService() {
		super();
	}

	// Simple CRUD methods
	// CREATE AN AUDIT
	public Audit create() {
		Audit result;

		result = new Audit();
		result.setMoment(new Date(System.currentTimeMillis() - 1));
		result.setIsDraft(true);

		return result;
	}

	public Audit saveFromCreate(final Audit audit) {
		Assert.notNull(audit, "message.error.audit.null");

		final Audit result;

		Auditor principal;
		Collection<Audit> principalAudits;

		principal = this.auditorService.findByPrincipal();
		Assert.notNull(principal, "message.error.audit.login");

		audit.setMoment(new Date(System.currentTimeMillis() - 1));
		result = this.auditRepository.save(audit);

		principalAudits = principal.getAudits();
		principalAudits.add(result);
		principal.setAudits(principalAudits);
		this.auditorService.saveFromEdit(principal);

		return result;
	}

	public Audit findOne(final int auditId) {
		Audit result;

		result = this.auditRepository.findOne(auditId);

		return result;
	}

	//LIST ALL AUDITS

	public Collection<Audit> listAll() {
		return this.auditRepository.findAll();
	}
	//SAVE AN AUDIT
	public void save(final Audit a) {
		Assert.notNull(a);
		final Audit auditCheckDraft = this.auditRepository.findOne(a.getId());
		Assert.isTrue(auditCheckDraft.getIsDraft());
		this.auditRepository.save(a);
		final Auditor principal = this.auditorService.findByPrincipal();

		Boolean isSuspicious;

		isSuspicious = principal.getIsSuspicious();
		isSuspicious = isSuspicious || this.messageService.checkSpam(a.getTitle()) || this.messageService.checkSpam(a.getDescription()) || this.messageService.checkSpam(a.getAttachmentUrl());
		principal.setIsSuspicious(isSuspicious);
		this.auditorService.saveFromEdit(principal);

		final Trip trip = a.getTrip();
		this.tripService.saveByOtherActors(trip);
	}
	public Audit saveFromEdit(final Audit a) {
		Assert.notNull(a);
		final Audit auditCheckDraft = this.auditRepository.findOne(a.getId());
		Assert.isTrue(auditCheckDraft.getIsDraft());

		final Audit result = this.auditRepository.save(a);
		final Auditor principal = this.auditorService.findByPrincipal();
		Assert.notNull(result);
		Assert.isTrue(principal.getAudits().contains(a));

		Boolean isSuspicious;
		isSuspicious = principal.getIsSuspicious();
		isSuspicious = isSuspicious || this.messageService.checkSpam(a.getTitle()) || this.messageService.checkSpam(a.getDescription());
		principal.setIsSuspicious(isSuspicious);
		this.auditorService.saveFromEdit(principal);

		this.auditorService.saveFromEdit(principal);
		return result;

	}

	//DELETE AN AUDIT

	public void delete(final Audit a) {
		Assert.notNull(a);
		Assert.isTrue(a.getIsDraft());
		final Audit result = this.auditRepository.save(a);
		final Auditor principal = this.auditorService.findByPrincipal();
		Assert.notNull(principal);
		this.auditorService.saveFromEdit(principal);
		Assert.isTrue(principal.getAudits().contains(result));

		final Collection<Audit> auditorAudits = principal.getAudits();
		auditorAudits.remove(result);

		final Collection<Audit> auditsAuditorRemoved = auditorAudits;

		principal.setAudits(auditsAuditorRemoved);

		this.auditorService.saveFromEdit(principal);

		//Delete audit
		this.auditRepository.delete(a);

	}
	// Other business methods

	//B-16.4.2.1 The minimum number of audit records per trip
	public Integer findMinAuditRecordsPerTrip() {
		return this.auditRepository.findMinAuditsPerTrip();
	}

	//B-16.4.2.2 The maximum number of audit records per trip
	public Integer findMaxAuditsPerTrip() {
		return this.auditRepository.findMaxAuditsPerTrip();
	}

	//B-16.4.2.3 The average number of audit records per trip
	public Double findAverageOfAuditsPerTrip() {
		return this.auditRepository.findAverageAuditsPerTrip();
	}

	//B-16.4.2.4 The standard deviation of the number of audit records per trip
	public Double findStandardDeviationOfAuditsPerTrip() {
		return this.auditRepository.findStandardDeviationOfAuditsPerTrip();
	}

	public Collection<Audit> findAll() {
		Collection<Audit> result;

		result = this.auditRepository.findAll();

		return result;
	}

	public Collection<Audit> findAllFinalAudits(final int id) {
		Collection<Audit> result;

		result = this.auditRepository.findAllFinalAudits(id);
		return result;
	}

}
