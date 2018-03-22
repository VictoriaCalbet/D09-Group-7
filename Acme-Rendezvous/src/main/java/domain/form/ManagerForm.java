
package domain.form;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

public class ManagerForm extends ActorForm {

	// Attributes -------------------------------------------------------------

	private String	VAT;


	@NotBlank
	@Pattern(regexp = "^[A-Za-z0-9-]+")
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getVAT() {
		return this.VAT;
	}

	public void setVAT(final String VAT) {
		this.VAT = VAT;
	}

}
