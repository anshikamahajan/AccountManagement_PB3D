package com.barclays.accountmanagement.serviceimpls;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.barclays.accountmanagement.entity.Account;
import com.barclays.accountmanagement.entity.Customer;
import com.barclays.accountmanagement.repositories.AccountRepository;
import com.barclays.accountmanagement.repositories.CustomerRepository;
import com.barclays.accountmanagement.services.ManagerService;
import com.barclays.accountmanagement.utility.LoggingAspect;



@Service
public class ManagerServiceImpl implements ManagerService{
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private AccountRepository accountRepository;

	/**
	 * createNewAccount ( Create new customer saving account )
	 * @author Jaypal
	 */
	
	public Account createNewAccount(int customerId) throws Exception{
		Account account = new Account();
		//System.out.println(customerId);
		Customer customer = customerRepository.findById(customerId).get();
		//System.out.println(customer.getName());
		account.setCurrentBalance(100000);
		account.setCustomer(customer);
		account.setDailyLimit(10000);
		
		try {
			account = accountRepository.save(account);
			LoggingAspect.LOGGER.info("Account has been created with Account Number-"+account.getAccountNumber());
		}catch(Exception e) {
			throw e;
		}
		return account;
	}

	/**
	 * method to create a customer account and save it to db
	 * 
	 */
	
	public Customer createNewCustomer(Customer customer) throws Exception{
		try {
			if(checkUserExist(customer.getPanCard()))
			customer = customerRepository.save(customer);
		}catch(Exception e){
			throw e;
		}
		return customer;
	}

	
	public ResponseEntity<Object> verifyPanCard(String panCardNumber)
	{
		System.out.println(panCardNumber);
		Customer customer = customerRepository.findCustomerByPanCard(panCardNumber);
		HashMap<String,String> result = new HashMap<String, String>();
		if(customer != null)
		{
			long customerId = customer.getCustomerId();
			String custId = String.valueOf(customerId);
			result.put("customer ID", custId);
			result.put("message","Account exists");
			return new ResponseEntity<>(result,HttpStatus.OK);
		}
		else 
		{
			result.put("message","Account doest not exists");
			return new ResponseEntity<>(result,HttpStatus.NOT_FOUND);
		}
	}
	public boolean checkUserExist(String panCardNumber)
	{
		Customer customer = customerRepository.findCustomerByPanCard(panCardNumber);
		if(customer!=null)
		{
			return false;
		}else
		{
			return true;
		}
	}


}