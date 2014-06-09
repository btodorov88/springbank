package nl.btodorov.springbank.service;

import junit.framework.Assert;
import nl.btodorov.springbank.config.RootConfiguration;
import nl.btodorov.springbank.repository.AccountRepository;
import nl.btodorov.springbank.service.messages.TransactionCreationRequest;
import nl.btodorov.springbank.service.messages.TransactionCreationStatusResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * Tests for the {@link TransactionService} service
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class TransactionServiceTest {

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private AccountRepository accountService;

	/**
	 * Tests if the capitals in the accounts is adjusted correctly after a
	 * transaction is made
	 */
	@Test
	public void testAdjustedAccount() {
		String fromAccount = "btodorov1";
		String toAccount = "btodorov2";
		int transactionAmount = 50;

		int initialCapitalFromAccount = accountService.findByAccountNumber(
				fromAccount).getCapital();
		int initialCapitalToAccount = accountService.findByAccountNumber(
				toAccount).getCapital();

		TransactionCreationRequest transactionRequest = new TransactionCreationRequest();
		transactionRequest.setAmount(transactionAmount);
		transactionRequest.setFromAccount(fromAccount);
		transactionRequest.setToAccount(toAccount);

		TransactionCreationStatusResponse transactionResult = transactionService
				.makeTransaction(transactionRequest);
		Assert.assertTrue(transactionResult.isSuccessful());

		int capitalFromAccount = accountService
				.findByAccountNumber(fromAccount).getCapital();
		int capitalToAccount = accountService.findByAccountNumber(toAccount)
				.getCapital();

		Assert.assertEquals(initialCapitalFromAccount - transactionAmount,
				capitalFromAccount);
		Assert.assertEquals(initialCapitalToAccount + transactionAmount,
				capitalToAccount);
	}

	/**
	 * Tests if the transaction fails when the capital in the from account is
	 * not enough
	 */
	@Test
	public void testTransactionFailureCapital() {
		String fromAccount = "btodorov1";
		String toAccount = "btodorov2";

		int initialCapitalFromAccount = accountService.findByAccountNumber(
				fromAccount).getCapital();

		TransactionCreationRequest transactionRequest = new TransactionCreationRequest();
		transactionRequest.setAmount(initialCapitalFromAccount + 1);
		transactionRequest.setFromAccount(fromAccount);
		transactionRequest.setToAccount(toAccount);

		TransactionCreationStatusResponse transactionResult = transactionService
				.makeTransaction(transactionRequest);
		Assert.assertFalse(transactionResult.isSuccessful());
	}

}
