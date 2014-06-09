package nl.btodorov.springbank.mvc;

import javax.validation.constraints.NotNull;

import nl.btodorov.springbank.service.messages.TransactionCreationRequest;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * This data class represents the form that users have to fill in when
 * submitting a new transaction
 *
 */
public class TransactionForm {

	@NotEmpty(message = "From Account is required.")
	private String fromAccount;

	@NotEmpty(message = "To Account is required.")
	private String toAccount;

	@NotNull(message = "Amount is required.")
	private Integer amount;

	public String getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	public String getToAccount() {
		return toAccount;
	}

	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	/**
	 * Converts the filled in form to {@link TransactionCreationRequest}
	 * @return
	 */
	public TransactionCreationRequest toTransactionCreationRequest() {
		TransactionCreationRequest transactionRequest = new TransactionCreationRequest();

		transactionRequest.setAmount(amount);
		transactionRequest.setFromAccount(fromAccount);
		transactionRequest.setToAccount(toAccount);

		return transactionRequest;
	}
}
