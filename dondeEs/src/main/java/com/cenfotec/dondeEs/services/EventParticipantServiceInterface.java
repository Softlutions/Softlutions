package com.cenfotec.dondeEs.services;

import java.util.List;
import com.cenfotec.dondeEs.pojo.EventParticipantPOJO;
import com.cenfotec.dondeEs.ejb.EventParticipant;

public interface EventParticipantServiceInterface {

	List<EventParticipantPOJO> getAllEventParticipants(int idEvent);

	Boolean saveParticipant(EventParticipant eventParticipant);
	
	EventParticipant findById(int id);


}
