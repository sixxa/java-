package com.sixa.cqrsbankingapp.service.transaction;

import com.sixa.cqrsbankingapp.domain.model.Transaction;
import com.sixa.cqrsbankingapp.service.CommandService;
import com.sixa.cqrsbankingapp.service.QueryService;

public interface TransactionService extends QueryService<Transaction>, CommandService<Transaction> {
}
