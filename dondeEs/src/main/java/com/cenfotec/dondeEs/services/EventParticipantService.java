package com.cenfotec.dondeEs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.repositories.EventParticipantRepository;

@Service
public class EventParticipantService implements EventParticipantServiceInterface {
 @Autowired private EventParticipantRepository eventParticipantRepository;
}
