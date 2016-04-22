package com.cenfotec.dondeEs.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.pojo.ParticipationOnEventsPOJO;
import com.cenfotec.dondeEs.repositories.ReportRepository;

@Service
public class ReportService implements ReportServiceInterface{

//	@Autowired private ReportRepository reportRepository;
	
	@Override
	public List<ParticipationOnEventsPOJO> getParticipationOnEvents(int serviceProviderId, String dateBegin, String dateEnd) {
		List<ParticipationOnEventsPOJO> ParticipationOnEvents = new ArrayList<ParticipationOnEventsPOJO>();
		
		if (dateBegin == "" && dateEnd == "") {
			if (serviceProviderId == 0) {
				// búsqueda de todos los usuarios.
				
			} else {
				// búsqueda de un usuario.
	/*			reportRepository.getParticipationOnEvents().stream().forEach(u -> {
					ParticipationOnEventsPOJO pe = new ParticipationOnEventsPOJO();
					pe.setTotal(u.getTotal());
					pe.setUserName(u.getUserName());
					
					ParticipationOnEvents.add(pe);
				}); */
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