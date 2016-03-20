package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.ejb.Event;
import com.cenfotec.dondeEs.pojo.EventPOJO;

public interface EventServiceInterface {
	List<EventPOJO> getAllEventByUser(int pidUsuario);
	Event getEventById(int idEvent);
	
	/***
	 * Guarda un evento.
	 * @author Enmanuel García González
	 * @return True en caso de efectuarse la inserción o false en caso contrario.
	 * @version 1.0
	 */
	int saveEvent(Event e);
	EventPOJO eventById(int idEvent);
	List<EventPOJO> getAllEventPublish();
}