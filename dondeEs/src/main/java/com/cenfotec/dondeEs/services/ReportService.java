package com.cenfotec.dondeEs.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.pojo.ParticipationOnEventsPOJO;

@Service
public class ReportService implements ReportServiceInterface{

	@Override
	public List<ParticipationOnEventsPOJO> getParticipationOnEvents(int serviceProviderId, String dateBegin, String dateEnd) {
		List<ParticipationOnEventsPOJO> ParticipationOnEvents = new ArrayList<ParticipationOnEventsPOJO>();
		
		if (dateBegin == "" && dateEnd == "") {
			if (serviceProviderId == 0) {
				// búsqueda de todos los usuarios.
				
			} else {
				// búsqueda de un usuario.
				
			}
		} else {
			if (serviceProviderId == 0) {
				// búsqueda de todos los usuarios en rango de fechas.
				
			} else {
				// búsqueda de un usuario en rango de fechas.
				
			}
		}
		
		return ParticipationOnEvents;
	}

}
