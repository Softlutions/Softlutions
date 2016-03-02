package com.cenfotec.dondeEs.services;

import java.util.List;
import com.cenfotec.dondeEs.pojo.EventParticipantPOJO;

public interface EventParticipantServiceInterface {

	List<EventParticipantPOJO> getAllEventParticipants(int idEvent);

}
