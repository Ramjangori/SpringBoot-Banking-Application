package com.bank.mapper;

import com.bank.dto.AccountRequest;
import com.bank.dto.AccountResponse;
import com.bank.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountResponse toResponse(Account account);

    Account toEntity(AccountRequest request);

}
