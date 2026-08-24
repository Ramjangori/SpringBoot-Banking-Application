package com.bank.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.bank.dto.AccountRequest;
import com.bank.dto.AccountResponse;
import com.bank.dto.TransferRequest;
import com.bank.entity.Account;
import com.bank.entity.Transaction;
import com.bank.exception.AccountAlreadyExistsException;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InsufficientBalanaceException;
import com.bank.exception.InvalidAccountNumberException;
import com.bank.exception.InvalidPasswordException;
import com.bank.exception.NegativeAmountException;
import com.bank.exception.TransactionNotFoundException;
import com.bank.mapper.AccountMapper;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepo;

	@Autowired
	private TransactionRepository transactionRepo;

	@Autowired
	private AccountMapper accountMapper;


	@Override
	public AccountResponse getAccountDetails(String accountNumber) {

		Account ac = accountRepo.findByAccountNumber(accountNumber)
				.orElseThrow(() ->
						new AccountNotFoundException("Account Not Found"));

		return accountMapper.toResponse(ac);
	}


	@Override
	public AccountResponse createAccount(AccountRequest account) {

		if (accountRepo.existsByEmail(account.getEmail())) {
			throw new AccountAlreadyExistsException(
					"Account with Email : " + account.getEmail()
							+ " is Already Exists.");
		}

		Account ac = accountMapper.toEntity(account);

		// pehle save karna padega id ke liye
		accountRepo.save(ac);

		String accountNumber =
				ac.getHolderName().substring(0, 2)
						+ 100
						+ ac.getId();

		ac.setAccountNumber(accountNumber);
		ac.setCreatedAt(LocalDateTime.now());

		// account number set hone ke baad dobara save
		Account saved = accountRepo.save(ac);

		return accountMapper.toResponse(saved);
	}


	@Override
	@Transactional
	public String transferMoney(TransferRequest request) {

		if (request.getAmount() <= 0) {
			throw new NegativeAmountException(
					"Amount can not be negative or zero");
		}

		if (request.getFromAccount()
				.equals(request.getToAccount())) {

			throw new InvalidAccountNumberException(
					"Sender and Receiver Account Cannot Be Same");
		}

		Account sender = accountRepo
				.findByAccountNumber(request.getFromAccount())
				.orElseThrow(() ->
						new InvalidAccountNumberException(
								"Invalid Sender Account Number"));

		Account receiver = accountRepo
				.findByAccountNumber(request.getToAccount())
				.orElseThrow(() ->
						new InvalidAccountNumberException(
								"Invalid Receiver Account Number"));

		Double amount = request.getAmount();

		if (sender.getBalance() < amount) {
			throw new InsufficientBalanaceException(
					"Insufficient Balance in Your Account");
		}

		sender.setBalance(sender.getBalance() - amount);

		receiver.setBalance(receiver.getBalance() + amount);

		accountRepo.save(sender);
		accountRepo.save(receiver);

		Transaction transaction = new Transaction();

		transaction.setAmount(amount);
		transaction.setFromAccount(sender);
		transaction.setToAccount(receiver);
		transaction.setTransactionDate(LocalDateTime.now());
		transaction.setStatus("Success");
		transaction.setTransactionType("Transfer");

		transactionRepo.save(transaction);

		return "Money Transfer Successfully";
	}


	@Override
	public List<Transaction> getTransactionHistory(String accountNumber) {

		Account account = accountRepo
				.findByAccountNumber(accountNumber)
				.orElseThrow(() ->
						new AccountNotFoundException("Account Not Found"));

		List<Transaction> transactions =
				transactionRepo.findByFromAccountOrToAccount(
						account, account);

		if (transactions.isEmpty()) {
			throw new TransactionNotFoundException(
					"No Transaction Found For Account : " + accountNumber);
		}

		return transactions;
	}


	@Override
	public AccountResponse login(String accountNumber, String password) {

		Account ac = accountRepo
				.findByAccountNumber(accountNumber)
				.orElseThrow(() ->
						new AccountNotFoundException(
								"Account Not Found"));

		if (!password.equals(ac.getPassword())) {
			throw new InvalidPasswordException(
					"Password does not match");
		}

		return accountMapper.toResponse(ac);
	}

	@Override
	@Transactional
	public String withdrow(String accountNumber, Double amount, String password) {

		Account ac = accountRepo.findByAccountNumber(accountNumber)
				.orElseThrow(() ->
						new AccountNotFoundException("Account Not Found"));

		if (!ac.getPassword().equals(password)) {
			throw new InvalidPasswordException("Invalid Password");
		}

		if (amount <= 0) {
			throw new NegativeAmountException(
					"Amount can not be negative or zero");
		}

		if (ac.getBalance() < amount) {
			throw new InsufficientBalanaceException(
					"Insufficient Balance in your account");
		}

		ac.setBalance(ac.getBalance() - amount);

		accountRepo.save(ac);

		// Save Transaction History

		Transaction t = new Transaction();
		t.setToAccount(null);
		t.setAmount(amount);
		t.setType("withdrow");
		t.setStatus("success");
		t.setFromAccount(ac);
		t.setTransactionDate(LocalDateTime.now());
		transactionRepo.save(t);

		return "Money Withdraw Successfully";
	}


	@Override
	@Transactional
	public String deposit(String accountNumber, Double amount, String password) {

		Account ac = accountRepo.findByAccountNumber(accountNumber)
				.orElseThrow(() ->
						new AccountNotFoundException("Account Not Found"));

		if (!ac.getPassword().equals(password)) {
			throw new InvalidPasswordException("Invalid Password");
		}

		if (amount <= 0) {
			throw new NegativeAmountException(
					"Amount can not be negative or zero");
		}

		ac.setBalance(ac.getBalance() + amount);

		accountRepo.save(ac);

		// Save Transaction History

		Transaction t = new Transaction();
		t.setToAccount(ac);
		t.setAmount(amount);
		t.setType("Deposit");
		t.setStatus("success");
		t.setFromAccount(null);
		t.setTransactionDate(LocalDateTime.now());
		transactionRepo.save(t);

		return "Money Deposit Successfully";
	}
}