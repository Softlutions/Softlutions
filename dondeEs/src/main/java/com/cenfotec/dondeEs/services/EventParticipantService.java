package com.cenfotec.dondeEs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.ejb.EventParticipant;
import com.cenfotec.dondeEs.repositories.EventParticipantRepository;

@Service
public class EventParticipantService implements EventParticipantServiceInterface {
	@Autowired
	private EventParticipantRepository eventParticipantRepository;

	@Override
	public Boolean saveParticipant(EventParticipant peventParticipant) {
		EventParticipant eventParticipant = eventParticipantRepository.save(peventParticipant);
		return (eventParticipant == null) ? false : true;
	}
}
