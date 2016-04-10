package com.cenfotec.dondeEs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.contracts.ParticipationOnEventsResponse;
import com.cenfotec.dondeEs.services.ReportServiceInterface;


@RestController
@RequestMapping(value = "rest/protected/report")
public class ReportController {
	

	@Autowired private ReportServiceInterface reportServiceInterface;
	
	/**
	 * Consulta la cantidad de veces que un prestatario participó en eventos, puede filtrar 
	 * por rango de fechas si se le indica.
	 * @author Enmanuel García González
	 * @param 
	 * @return 
	 * @version 1.0
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value ="/getParticipationOnEvents/{serviceProviderId}/{dateBegin}/{dateEnd}", 
						method = RequestMethod.GET)
	public ParticipationOnEventsResponse getParticipationOnEvents(@PathVariable("serviceProviderId") int serviceProviderId,
			@PathVariable("dateBegin") String dateBegin, @PathVariable("dateEnd") String dateEnd){
		ParticipationOnEventsResponse response = new ParticipationOnEventsResponse();
		
		try {
			
			response.setCode(200);
			
		} catch (Exception e) {
			response.setCode(500);
			response.setCodeMessage(e.toString());
			e.printStackTrace();

		} finally { return response; }
	}
}