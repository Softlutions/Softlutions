package com.cenfotec.dondeEs.services;

import java.util.List;
import com.cenfotec.dondeEs.pojo.EventParticipantPOJO;
import com.cenfotec.dondeEs.ejb.EventParticipant;

public interface EventParticipantServiceInterface {

	List<EventParticipantPOJO> getAllEventParticipants(int idEvent);

	Integer createParticipant(EventParticipant eventParticipant);

	Boolean saveParticipant(EventParticipant eventParticipant);
	
	EventParticipant findById(int id);
	
	EventParticipantPOJO findByUserAndEvent(int userId, int eventId);

	Boolean participantState(int participantId, byte state);
}
