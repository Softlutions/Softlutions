package com.cenfotec.dondeEs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.repositories.PasswordHistoryRepository;

@Service
public class PasswordHistoryService implements PasswordHistoryServiceInterface {
	@Autowired
	private PasswordHistoryRepository passwordHistoryRepository;
}
