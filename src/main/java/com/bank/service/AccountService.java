package com.bank.service;

import java.util.List;

import com.bank.dto.AccountRequest;
import com.bank.dto.AccountResponse;
import com.bank.dto.TransferRequest;
import com.bank.entity.Account;
import com.bank.entity.Transaction;

public interface AccountService {
   
	 public AccountResponse createAccount(AccountRequest account);
	 
	 public AccountResponse getAccountDetails(String accountNumber);
	 
	 String transferMoney(TransferRequest request);
	 
	 List<Transaction> getTransactionHistory(String accountNumber);

	AccountResponse login(String accountNumber, String password);

	String withdrow( String accountNumber ,Double ammount , String password);

	String deposit(String accountNumber , Double ammount , String password);
}
