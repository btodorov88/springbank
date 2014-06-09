package nl.btodorov.springbank.repository;

import java.util.List;

import nl.btodorov.springbank.domain.Person;
import nl.btodorov.springbank.domain.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Responsible to provide CRUD operation for {@link Transaction}
 *
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	/**
	 * Retrieves all transactions which involve an account owned by the provided
	 * user. Uses a named query defined in {@link Transaction}
	 */
	public List<Transaction> findByOwner(Person p);
}
