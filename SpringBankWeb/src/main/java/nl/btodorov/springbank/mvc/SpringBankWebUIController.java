package nl.btodorov.springbank.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import nl.btodorov.springbank.domain.Account;
import nl.btodorov.springbank.domain.Person;
import nl.btodorov.springbank.service.AccountService;
import nl.btodorov.springbank.service.TransactionService;
import nl.btodorov.springbank.service.messages.TransactionCreationStatusResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;

/**
 * This controller is responsible to handle all requests made from the user's
 * web browser by processing them and providing the required information back
 *
 */
@Controller
public class SpringBankWebUIController {

	@Autowired
	AccountService accountService;

	@Autowired
	TransactionService transactionService;

	/**
	 * Responsible to provide the view which provides summary about all accounts
	 * and transactions for the authenticated user
	 * 
	 * @param person
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String viewTransactions(@AuthenticationPrincipal Person person,
			Model model) {
		if (person != null && person.getId() != null) {
			model.addAttribute("accounts", accountService.findByPerson(person));
			model.addAttribute("transactions",
					transactionService.findByPerson(person));
		} else {
			model.addAttribute("transactions", Lists.newArrayList());
			model.addAttribute("accounts", Lists.newArrayList());
		}

		return "transactions/show";
	}

	/**
	 * Provides the view that is used to submit new transaction
	 * 
	 * @param transactionForm
	 *            - empty transaction form
	 * @return
	 */
	@RequestMapping(value = "createTransaction", method = RequestMethod.GET)
	public String createForm(@AuthenticationPrincipal Person person, @ModelAttribute TransactionForm transactionForm, Model model) {
		model.addAllAttributes(getAccountsAttributesForPerson(person));
		
		return "transactions/compose";
	}

	/**
	 * Provides the personalAccounts and allAccounts model attributes
	 * @param person
	 * @return
	 */
	private Map<String, List<Account>> getAccountsAttributesForPerson(Person person) {
		HashMap<String, List<Account>> attributes = new HashMap<String, List<Account>>();
		
		if (person != null && person.getId() != null) {
			attributes.put("personalAccounts", accountService.findByPerson(person));
			attributes.put("allAccounts", accountService.findAllAccounts());
		} else {
			attributes.put("personalAccounts", Lists.<Account>newArrayList());
			attributes.put("allAccounts", Lists.<Account>newArrayList());
		}
		
		return attributes;
	}

	/**
	 * Processes the submitted transaction form. Validates the provided form and
	 * then notifies the {@link transactionService} to create the transaction.
	 * In case of success the user is redirected to the root page and a success
	 * message is provided.
	 * 
	 * @param transactionForm
	 * @param bindingResult
	 * @param redirect
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView createTrasaction(@Valid TransactionForm transactionForm,
			BindingResult bindingResult, RedirectAttributes redirect, @AuthenticationPrincipal Person person) {
		
		if (bindingResult.hasErrors()) {
			return new ModelAndView("transactions/compose", getAccountsAttributesForPerson(person));
		}

		TransactionCreationStatusResponse transactionCreationResult = transactionService
				.makeTransaction(transactionForm.toTransactionCreationRequest());

		if (!transactionCreationResult.isSuccessful()) {
			bindingResult.reject("failure",	transactionCreationResult.getMessage());
			return new ModelAndView("transactions/compose", getAccountsAttributesForPerson(person));
		}

		redirect.addFlashAttribute("globalMessage", "Transaction successfully");
		return new ModelAndView("redirect:/");
	}

}
