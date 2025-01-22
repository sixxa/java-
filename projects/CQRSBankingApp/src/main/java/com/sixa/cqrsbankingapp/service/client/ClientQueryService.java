package com.sixa.cqrsbankingapp.service.client;

import com.sixa.cqrsbankingapp.domain.model.Client;
import com.sixa.cqrsbankingapp.service.QueryService;

public interface ClientQueryService extends QueryService<Client>{

    boolean existsByUsername(String username);
}
