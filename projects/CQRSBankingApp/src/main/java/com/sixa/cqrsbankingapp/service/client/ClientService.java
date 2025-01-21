package com.sixa.cqrsbankingapp.service.client;

import com.sixa.cqrsbankingapp.domain.model.Client;
import com.sixa.cqrsbankingapp.service.CommandService;
import com.sixa.cqrsbankingapp.service.QueryService;

public interface ClientService extends QueryService<Client>, CommandService<Client> {
}
