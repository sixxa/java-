package com.sixa.virtualguide.Service;

import com.sixa.virtualguide.repo.AccountRepo;
import com.sixa.virtualguide.repo.GuideRepo;
import com.sixa.virtualguide.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuideService {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private GuideRepo guideRepo;

    @Autowired
    private UserRepo userRepo;

}
