package nl.btodorov.springbank.repository;

import java.util.List;

import nl.btodorov.springbank.domain.Account;
import nl.btodorov.springbank.domain.Person;

import org.springframework.data.repository.CrudRepository;

/**
 * Responsible to provide CRUD operation for {@link Account}
 *
 */
public interface AccountRepository extends CrudRepository<Account, Long> {

	/**
	 * Retrieves all accounts associated with the provided person
	 * 
	 * @param person
	 * @return
	 */
	public List<Account> findAllByOwner(Person person);

	/**
	 * Retrieves the full Account object for the provided account number 
	 * @param accountNumber
	 * @return
	 */
	public Account findByAccountNumber(String accountNumber);
	
}
