
package domain.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

public class QuestionAndAnswerForm {

	private String	questionText;
	private String	answerText;
	private int		questionId;
	private int		answerId;


	public int getAnswerId() {
		return this.answerId;
	}

	public void setAnswerId(final int answerId) {
		this.answerId = answerId;
	}

	@SafeHtml
	@NotBlank
	@NotNull
	public String getQuestionText() {
		return this.questionText;
	}

	public void setQuestionText(final String questionText) {
		this.questionText = questionText;
	}
	@SafeHtml
	@NotBlank
	@NotNull
	public String getAnswerText() {
		return this.answerText;
	}

	public void setAnswerText(final String answerText) {
		this.answerText = answerText;
	}

	public int getQuestionId() {
		return this.questionId;
	}

	public void setQuestionId(final int questionId) {
		this.questionId = questionId;
	}

}
