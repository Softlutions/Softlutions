package com.cenfotec.dondeEs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.repositories.LogRepository;

@Service
public class LogService implements LogServiceInterface {
	@Autowired private LogRepository logRepository;
}
