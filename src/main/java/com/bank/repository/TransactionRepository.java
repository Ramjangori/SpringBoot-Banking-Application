package com.bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.entity.Account;
import com.bank.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {

	 public List<Transaction> findByFromAccountOrToAccount( Account fromAccount,
		        Account toAccount);
}
