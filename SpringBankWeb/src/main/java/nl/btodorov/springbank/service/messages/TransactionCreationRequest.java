package nl.btodorov.springbank.service.messages;

/**
 * Encapsulates all needed information for submitting transaction creation request 
 *
 */
public class TransactionCreationRequest {
	
	private String fromAccount;
	private String toAccount;
	private int amount;
	
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

	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
}
