
package services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Audit;
import domain.Auditor;
import domain.Trip;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class AuditServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private AuditService	auditService;
	@Autowired
	private AuditorService	auditorService;


	// Tests
	@Test
	public void testCreate() {
		//login
		super.authenticate("auditor1");
		//Variables
		Audit auditToCreate;
		//Initialise
		auditToCreate = this.auditService.create();
		Assert.notNull(auditToCreate);
		auditToCreate.setMoment(new Date(System.currentTimeMillis() - 1));

		this.auditService.saveFromCreate(auditToCreate);

		super.unauthenticate();

	}
	@Test
	public void testSaveFromCreate() {
		//login

		super.authenticate("auditor1");
		final Auditor auditor = this.auditorService.findByPrincipal();
		final Audit a = auditor.getAudits().iterator().next();
		final Trip trip = a.getTrip();

		a.setTitle("title2");
		a.setMoment(new Date(System.currentTimeMillis() - 1));
		a.setDescription("Description2");
		a.setIsDraft(true);
		a.setTrip(trip);
		final Audit saveAudit = this.auditService.saveFromCreate(a);
		Assert.notNull(saveAudit);
		Collection<Audit> tripAudits = new ArrayList<Audit>();
		Collection<Audit> auditorAudits = new ArrayList<Audit>();

		tripAudits = trip.getAudits();
		auditorAudits = auditor.getAudits();

		tripAudits.add(saveAudit);
		auditorAudits.add(saveAudit);
		this.auditorService.saveFromEdit(auditor);
		Assert.isTrue(trip.getAudits().contains(saveAudit));
		Assert.isTrue(auditor.getAudits().contains(saveAudit));
		super.unauthenticate();

	}

	@Test
	public void testSaveFromEdit() {
		//Comprobar que los cambios se hagan en trip y auditor

		super.authenticate("auditor1");
		final Auditor auditor = this.auditorService.findByPrincipal();
		final Audit a = auditor.getAudits().iterator().next();
		final Trip trip = a.getTrip();

		a.setTitle("title2");
		a.setMoment(new Date(System.currentTimeMillis() - 1));
		a.setDescription("Description2");
		a.setIsDraft(true);
		a.setTrip(trip);
		final Audit saveAudit = this.auditService.saveFromCreate(a);
		Assert.notNull(saveAudit);
		Collection<Audit> tripAudits = new ArrayList<Audit>();
		Collection<Audit> auditorAudits = new ArrayList<Audit>();

		tripAudits = trip.getAudits();
		auditorAudits = auditor.getAudits();

		tripAudits.add(saveAudit);
		auditorAudits.add(saveAudit);
		this.auditorService.saveFromEdit(auditor);
		Assert.isTrue(trip.getAudits().contains(saveAudit));
		Assert.isTrue(auditor.getAudits().contains(saveAudit));
		super.unauthenticate();

	}

	@Test
	public void testDelete() {
		super.authenticate("auditor2");
		final Auditor auditor = this.auditorService.findByPrincipal();
		final Audit a = auditor.getAudits().iterator().next();
		a.setIsDraft(true);
		this.auditService.saveFromEdit(a);
		this.auditorService.saveFromEdit(auditor);

		this.auditService.delete(a);
		Assert.isTrue(!auditor.getAudits().contains(a));
		super.unauthenticate();
	}

}
