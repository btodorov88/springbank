package nl.btodorov.springbank.service.messages;

/**
 * Encapsulates the information which defines the result of a creation of a
 * transaction
 *
 */
public class TransactionCreationStatusResponse {
	private boolean isSuccessful;
	private String message;

	public TransactionCreationStatusResponse(boolean isSuccessful,
			String message) {
		super();
		this.isSuccessful = isSuccessful;
		this.message = message;
	}

	public boolean isSuccessful() {
		return isSuccessful;
	}

	public String getMessage() {
		return message;
	}

}
