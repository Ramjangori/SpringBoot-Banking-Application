package com.bank.controller;

import java.util.List;

import com.bank.dto.AccountRequest;
import com.bank.dto.AccountResponse;
import com.bank.dto.TransferRequest;
import com.bank.entity.Transaction;
import com.bank.service.AccountService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {

	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@PostMapping("/create")
	public ResponseEntity<AccountResponse> createAccount(
			@Valid @RequestBody AccountRequest ac) {

		AccountResponse response = accountService.createAccount(ac);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}


	@GetMapping("/{accountNumber}")
	public ResponseEntity<AccountResponse> getAccountDetails(
			@PathVariable String accountNumber) {

		AccountResponse response =
				accountService.getAccountDetails(accountNumber);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	@PostMapping("/login")
	public ResponseEntity<AccountResponse> loginAccount(
			@RequestParam String accountNumber,
			@RequestParam String password) {

		AccountResponse response =
				accountService.login(accountNumber, password);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	@PostMapping("/transfer")
	public ResponseEntity<String> transferMoney(
			@RequestBody TransferRequest request) {

		String response = accountService.transferMoney(request);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	@GetMapping("/{accountNumber}/transactions")
	public ResponseEntity<List<Transaction>> getTransactionHistory(
			@PathVariable String accountNumber) {

		List<Transaction> transactions =
				accountService.getTransactionHistory(accountNumber);

		return new ResponseEntity<>(transactions, HttpStatus.OK);
	}

	@PostMapping("/withdrow")
	public ResponseEntity<String> withdrow(@RequestParam String acc , @RequestParam Double amount
	    , @RequestParam String password){

		String result = accountService.withdrow(acc,amount,password);

		return new ResponseEntity<>(result,HttpStatus.OK);
	}


	@PostMapping("/deposit")
	public ResponseEntity<String> deposit(@RequestParam String acc , @RequestParam Double amount
			, @RequestParam String password){

		String result = accountService.deposit(acc,amount,password);

		return new ResponseEntity<>(result,HttpStatus.OK);
	}
}