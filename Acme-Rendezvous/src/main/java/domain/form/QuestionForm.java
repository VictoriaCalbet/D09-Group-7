
package domain.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

public class QuestionForm {

	private int		questionId;
	private int		rendezvousId;
	private String	text;


	public int getQuestionId() {
		return this.questionId;
	}

	public void setQuestionId(final int questionId) {
		this.questionId = questionId;
	}

	public int getRendezvousId() {
		return this.rendezvousId;
	}

	public void setRendezvousId(final int rendezvousId) {
		this.rendezvousId = rendezvousId;
	}
	@SafeHtml
	@NotBlank
	@NotNull
	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}

}
