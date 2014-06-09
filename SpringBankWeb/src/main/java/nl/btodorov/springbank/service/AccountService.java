package nl.btodorov.springbank.service;

import java.util.List;

import nl.btodorov.springbank.domain.Account;
import nl.btodorov.springbank.domain.Person;
import nl.btodorov.springbank.repository.AccountRepository;
import nl.btodorov.springbank.repository.PersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

/**
 * Contains all the needed business logic for processing {@link Accounts}
 *
 */
@Service
@Repository
@Transactional
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PersonRepository personRepository;

	/**
	 * Provides all bank accounts owned by the provided person
	 * 
	 * @param person
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Account> findByPerson(Person person) {
		// TODO Check if the authenticate user has the rights to retrive this
		// info
		person = personRepository.findOne(person.getId());
		return Lists.newArrayList(accountRepository.findAllByOwner(person));
	}

	/**
	 * Provides all bank accounts
	 * @return
	 */
	public List<Account> findAllAccounts() {
		// FIXME might be better for security if we only return the account numbers
		return Lists.newArrayList(accountRepository.findAll());
	}
}
