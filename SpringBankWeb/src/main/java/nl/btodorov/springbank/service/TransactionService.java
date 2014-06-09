package nl.btodorov.springbank.service;

import java.util.Date;
import java.util.List;

import nl.btodorov.springbank.domain.Account;
import nl.btodorov.springbank.domain.Person;
import nl.btodorov.springbank.domain.Transaction;
import nl.btodorov.springbank.repository.AccountRepository;
import nl.btodorov.springbank.repository.TransactionRepository;
import nl.btodorov.springbank.service.messages.TransactionCreationRequest;
import nl.btodorov.springbank.service.messages.TransactionCreationStatusResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

/**
 * Contains all the needed business logic for processing {@link Transaction}
 *
 */
@Service
@Repository
@Transactional
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountRepository accountRepository;

	/**
	 * Retrieves all transactions
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Transaction> findAll() {
		return Lists.newArrayList(transactionRepository.findAll());
	}

	/**
	 * Creates new transaction adjusting the capital of the involved bank
	 * accounts
	 * 
	 * @param transactionRequest
	 * @return
	 */
	@Transactional
	public TransactionCreationStatusResponse makeTransaction(TransactionCreationRequest transactionRequest) {
		// TODO validation of the inputs and authenticated user rights

		Account fromAccount = accountRepository
				.findByAccountNumber(transactionRequest.getFromAccount());
		Account toAccount = accountRepository
				.findByAccountNumber(transactionRequest.getToAccount());

		TransactionCreationStatusResponse validationResult = validateTransaction(
				fromAccount, toAccount, transactionRequest);
		if (validationResult != null) {
			// A problem is found
			return validationResult;
		}

		performPayment(fromAccount, toAccount, transactionRequest.getAmount());

		return new TransactionCreationStatusResponse(true, "Success");
	}

	/**
	 * Validates if all requirements for making the transaction are fulfilled
	 * 
	 * @param fromAccount
	 * @param toAccount
	 * @param paymentRequest
	 * 
	 * @return null if no problems are found
	 */
	private TransactionCreationStatusResponse validateTransaction(Account fromAccount,
			Account toAccount, TransactionCreationRequest paymentRequest) {
		if (fromAccount == null) {
			return new TransactionCreationStatusResponse(false, "From account not found!");
		}

		if (toAccount == null) {
			return new TransactionCreationStatusResponse(false, "To account not found!");
		}
		
		if(fromAccount.getId().equals(toAccount.getId())){
			return new TransactionCreationStatusResponse(false, "To and From accounts are the same!");
		}

		// TODO check ownership

		if (fromAccount.getCapital() < paymentRequest.getAmount()) {
			return new TransactionCreationStatusResponse(false, "Insufficient capital!");
		}

		return null;
	}

	/**
	 * Performs the actual payment 
	 * 
	 * @param fromAccount
	 * @param toAccount
	 * @param paymentAmount
	 */
	private void performPayment(Account fromAccount, Account toAccount,
			int paymentAmount) {
		adjustCapitalInAccounts(fromAccount, toAccount, paymentAmount);

		fromAccount = accountRepository.save(fromAccount);
		toAccount = accountRepository.save(toAccount);

		// Create and store the new transaction
		Transaction transaction = new Transaction();
		transaction.setAmount(paymentAmount);
		transaction.setFromAccount(fromAccount);
		transaction.setToAccount(toAccount);
		transaction.setTransactionDate(new Date());

		transactionRepository.save(transaction);
	}

	/**
	 * Adjusts the capital of the bank accounts involved into the transaction
	 * @param fromAccount
	 * @param toAccount
	 * @param paymentAmount
	 */
	private void adjustCapitalInAccounts(Account fromAccount,
			Account toAccount, int paymentAmount) {
		fromAccount.setCapital(fromAccount.getCapital() - paymentAmount);
		toAccount.setCapital(toAccount.getCapital() + paymentAmount);
	}

	/**
	 * Retrieves all transactions which involve an account owned by the provided
	 * user.
	 */
	public List<Transaction> findByPerson(Person p) {
		// TODO Check if the authenticated user has the required rights to get
		// this info
		return transactionRepository.findByOwner(p);
	}
}
