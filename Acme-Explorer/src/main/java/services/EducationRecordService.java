
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.EducationRecordRepository;
import domain.Curriculum;
import domain.EducationRecord;
import domain.Ranger;

@Service
@Transactional
public class EducationRecordService {

	// Managed Repository
	@Autowired
	private EducationRecordRepository	educationRecordRepository;

	// Supporting Services
	@Autowired
	private CurriculumService			curriculumService;
	@Autowired
	private RangerService				rangerService;
	@Autowired
	private MessageService				messageService;


	// Constructor
	public EducationRecordService() {
		super();
	}

	// Simple CRUD methods
	public EducationRecord create() {

		final EducationRecord record = new EducationRecord();
		return record;
	}
	public EducationRecord save(final EducationRecord educationRecord) {
		this.isARangerAuthenticated();
		final Boolean edit = this.educationRecordRepository.findOne(educationRecord.getId()) != null;
		if (edit)
			this.isTheCorrectRanger(this.curriculumService.findByEducationRecordId(educationRecord.getId()).getId());

		Assert.notNull(educationRecord.getInstitution());
		Assert.notNull(educationRecord.getDiplomaTitle());
		if (educationRecord.getStartDate() != null) {
			Date startDate;
			startDate = educationRecord.getStartDate();
			if (educationRecord.getEndDate() != null) {
				Date endDate;
				endDate = educationRecord.getEndDate();
				Boolean datesAreCorrect = false;
				datesAreCorrect = startDate.before(endDate);
				Assert.isTrue(datesAreCorrect);
			}
		}
		final EducationRecord savedEducationRecord = this.educationRecordRepository.save(educationRecord);
		Ranger principal;
		principal = this.rangerService.findByPrincipal();
		//checking is supicious
		final Collection<String> allStrings = new ArrayList<String>();
		allStrings.add(savedEducationRecord.getComments());
		allStrings.add(savedEducationRecord.getAttachmentLink());
		allStrings.add(savedEducationRecord.getDiplomaTitle());
		allStrings.add(savedEducationRecord.getInstitution());
		Boolean isSuspicious;
		isSuspicious = this.isSuspicious(allStrings);
		if (isSuspicious)
			principal.setIsSuspicious(true);
		//end check
		if (!edit) {

			Curriculum curriculumPrincipal;
			curriculumPrincipal = principal.getCurriculum();
			curriculumPrincipal.getEducationRecords().add(savedEducationRecord);
			this.curriculumService.save(curriculumPrincipal);

		}
		this.rangerService.saveFromEdit(principal);

		return savedEducationRecord;

	}
	public void delete(final EducationRecord educationRecord) {
		Assert.notNull(this.educationRecordRepository.findOne(educationRecord.getId()));
		final Curriculum c = this.curriculumService.findByEducationRecordId(educationRecord.getId());

		this.isTheCorrectRanger(c.getId());

		c.getEducationRecords().remove(educationRecord);
		this.curriculumService.save(c);

		this.educationRecordRepository.delete(educationRecord);

	}
	public EducationRecord findOne(final int recordId) {
		EducationRecord r;
		r = this.educationRecordRepository.findOne(recordId);
		return r;
	}

	// Other business methods
	public Collection<EducationRecord> findAllByCurriculumId(final int curriculumId) {
		final Curriculum c = this.curriculumService.findOne(curriculumId);
		final Collection<EducationRecord> r = new ArrayList<EducationRecord>();
		if (c != null)
			r.addAll(c.getEducationRecords());
		return r;
	}
	public void deleteAllByCurriculum(final Curriculum c) {
		this.isTheCorrectRanger(c.getId());

		final Collection<EducationRecord> records = new ArrayList<EducationRecord>(c.getEducationRecords());
		if (!records.isEmpty())
			for (final EducationRecord r : records)
				this.delete(r);
	}

	//Auxiliares
	private Ranger isARangerAuthenticated() {

		final Ranger r = this.rangerService.findByPrincipal();

		Assert.notNull(r);

		return r;
	}
	private void isTheCorrectRanger(final int CurriculumId) {

		Assert.notNull(this.isARangerAuthenticated().getCurriculum());
		Assert.isTrue(this.isARangerAuthenticated().getCurriculum().getId() == CurriculumId);
	}
	private Boolean isSuspicious(final Collection<String> texts) {
		Boolean isSuspicious = false;
		for (final String text : texts)
			if (text != null) {
				final Boolean check = this.messageService.checkSpam(text);
				if (check) {
					isSuspicious = true;
					break;
				}
			}
		return isSuspicious;
	}
}
