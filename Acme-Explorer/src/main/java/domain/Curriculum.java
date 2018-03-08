
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class Curriculum extends DomainEntity {

	// Attributes
	private String	ticker;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@Column(unique = true)
	public String getTicker() {
		return this.ticker;
	}
	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}


	// Relationships
	private PersonalRecord					personalRecord;
	private Collection<EndorserRecord>		endorserRecords;
	private Collection<ProfessionalRecord>	professionalRecords;
	private Collection<EducationRecord>		educationRecords;
	private Collection<MiscellaneousRecord>	miscellaneousRecords;


	@Valid
	@OneToOne(optional = true)
	public PersonalRecord getPersonalRecord() {
		return this.personalRecord;
	}

	public void setPersonalRecord(final PersonalRecord personalRecord) {
		this.personalRecord = personalRecord;
	}

	@Valid
	@NotNull
	@OneToMany
	public Collection<EndorserRecord> getEndorserRecords() {
		return this.endorserRecords;
	}
	public void setEndorserRecords(final Collection<EndorserRecord> endorserRecords) {
		this.endorserRecords = endorserRecords;
	}

	@Valid
	@NotNull
	@OneToMany
	public Collection<ProfessionalRecord> getProfessionalRecords() {
		return this.professionalRecords;
	}
	public void setProfessionalRecords(final Collection<ProfessionalRecord> professionalRecords) {
		this.professionalRecords = professionalRecords;
	}

	@Valid
	@NotNull
	@OneToMany
	public Collection<EducationRecord> getEducationRecords() {
		return this.educationRecords;
	}
	public void setEducationRecords(final Collection<EducationRecord> educationRecords) {
		this.educationRecords = educationRecords;
	}

	@Valid
	@NotNull
	@OneToMany
	public Collection<MiscellaneousRecord> getMiscellaneousRecords() {
		return this.miscellaneousRecords;
	}
	public void setMiscellaneousRecords(final Collection<MiscellaneousRecord> miscellaneousRecords) {
		this.miscellaneousRecords = miscellaneousRecords;
	}

}
