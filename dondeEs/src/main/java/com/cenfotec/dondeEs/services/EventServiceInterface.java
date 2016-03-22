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
	
	/***
	 * Obtiene todos los eventos publicados.
	 * @author Enmanuel García González
	 * @version 1.0
	 */
	List<EventPOJO> getAllEventPublish();
	
	/**
	 * @auctor Ernesto Mendez A.
	 * @param e Eventoa modificar
	 * @return retorna true si la operacion fue exitosa, false si no
	 */
	boolean editEvent(Event e);
}