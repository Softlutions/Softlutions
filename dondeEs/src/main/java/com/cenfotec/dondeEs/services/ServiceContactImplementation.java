package com.cenfotec.dondeEs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.repositories.ServiceContactRepository;

@Service
public class ServiceContactImplementation implements ServiceContactInterface {
@Autowired private ServiceContactRepository contactRepository;
}
