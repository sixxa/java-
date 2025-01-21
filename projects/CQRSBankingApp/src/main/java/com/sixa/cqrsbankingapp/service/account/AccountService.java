package com.sixa.cqrsbankingapp.service.account;

import com.sixa.cqrsbankingapp.domain.model.Account;
import com.sixa.cqrsbankingapp.service.CommandService;
import com.sixa.cqrsbankingapp.service.QueryService;

public interface AccountService extends QueryService<Account>, CommandService<Account> {
}
