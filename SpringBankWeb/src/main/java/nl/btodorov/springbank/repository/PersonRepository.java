package nl.btodorov.springbank.repository;

import nl.btodorov.springbank.domain.Person;

import org.springframework.data.repository.CrudRepository;

/**
 * Responsible to provide CRUD operation for {@link Person}
 *
 */
public interface PersonRepository extends CrudRepository<Person, Long>{
	
	/**
	 * Retrieves the full Person object based on the provided username
	 * @param username
	 * @return
	 */
	public Person findByUsername(String username);
}
