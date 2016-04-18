package com.cenfotec.dondeEs.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cenfotec.dondeEs.contracts.BaseResponse;
import com.cenfotec.dondeEs.contracts.EventResponse;
import com.cenfotec.dondeEs.ejb.Event;
import com.cenfotec.dondeEs.ejb.EventParticipant;
import com.cenfotec.dondeEs.ejb.Place;
import com.cenfotec.dondeEs.ejb.User;
import com.cenfotec.dondeEs.logic.AES;
import com.cenfotec.dondeEs.pojo.EventPOJO;
import com.cenfotec.dondeEs.pojo.UserPOJO;
import com.cenfotec.dondeEs.services.EventParticipantServiceInterface;
import com.cenfotec.dondeEs.services.EventServiceInterface;
import com.cenfotec.dondeEs.services.PlaceServiceInterface;
import com.cenfotec.dondeEs.services.UserServiceInterface;
import com.cenfotec.dondeEs.utils.Utils;

@Controller
@RestController
@RequestMapping(value = "rest/protected/event")
public class EventController {

	@Autowired
	private EventParticipantServiceInterface eventParticipantServiceInterface;
	@Autowired
	private EventServiceInterface eventServiceInterface;
	@Autowired
	private UserServiceInterface userServiceInterface;
	@Autowired
	private ServletContext servletContext;
	@Autowired
	private PlaceServiceInterface placeServiceInterface;

	@RequestMapping(value = "/getAllEventByUser/{id}", method = RequestMethod.GET)
	public EventResponse getAllByUser(@PathVariable("id") int id) {
		EventResponse response = new EventResponse();
		response.setCode(200);
		response.setCodeMessage("Events by user");
		response.setEventList(eventServiceInterface.getAllEventByUser(id));
		return response;
	}

	/***
	 * Obtiene todos los eventos que han sido publicados.
	 * 
	 * @author Enmanuel García González
	 * @return
	 * @version 1.0
	 */

	@SuppressWarnings("finally")
	@RequestMapping(value = "/getAllEventPublish", method = RequestMethod.GET)
	public EventResponse getAll() {
		EventResponse response = new EventResponse();
		;

		try {
			response.setCode(200);
			response.setCodeMessage("eventsPublish fetch success");
			response.setEventList(eventServiceInterface.getAllEventPublish());

		} catch (Exception e) {
			response.setCode(500);
			response.setCodeMessage(e.toString());
			e.printStackTrace();

		} finally {
			return response;
		}
	}

	/***
	 * Publica un determinado evento.
	 * 
	 * @author Enmanuel García González
	 * @param eventRequest
	 * @return
	 * @version 2.0
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value = "/publishEvent", method = RequestMethod.PUT)
	public EventResponse publishEvent(@RequestBody EventPOJO eventRequest) {
		EventResponse response = new EventResponse();

		try {
			if (eventRequest.getEventId() != 0) {
				Event event = eventServiceInterface.getEventById(eventRequest.getEventId());
				event.setState((byte) 3);
				event.setPublishDate(new Date());

				int eventId = eventServiceInterface.saveEvent(event);

				if (eventId != 0) {
					response.setCode(200);
					response.setErrorMessage("success");
				} else {
					response.setCode(500);
					response.setErrorMessage("publish event error");
				}

			} else {
				response.setCode(409);
				response.setErrorMessage("idEvent is zero");
			}
		} catch (Exception e) {
			response.setCode(500);
			response.setCodeMessage(e.toString());
			e.printStackTrace();

		} finally {
			return response;
		}
	}

	// get event by id
	@RequestMapping(value = "/getEventById/{id}", method = RequestMethod.GET)
	public EventResponse getEventById(@PathVariable("id") int id) {
		EventResponse response = new EventResponse();
		if (id != 0) {
			response.setEventPOJO(eventServiceInterface.eventById(id));
			response.getEventPOJO().setPrivate_((byte) 1);
			response.getEventPOJO().setState((byte) 1);
			response.getEventPOJO().setPublishDate(new Date());
			response.setCode(200);
		} else {
			response.setCode(400);
			response.setCodeMessage("Something is wrong");
		}
		return response;
	}
	
	@RequestMapping(value = "/getEventDataById/{eventId}", method = RequestMethod.GET)
	public EventResponse getEventDataById(@PathVariable("eventId") int eventId) {
		EventResponse response = new EventResponse();
		EventPOJO eventPOJO = eventServiceInterface.eventById(eventId);
		
		if(eventPOJO != null) {
			response.setCode(200);
			response.setCodeMessage("Success");
			response.setEventPOJO(eventPOJO);
		}else{
			response.setCode(404);
			response.setCodeMessage("Event not found");
		}
		
		return response;
	}

	/***
	 * Cancela un evento que ha sido previamente publicado.
	 * 
	 * @author Enmanuel García González
	 * @param eventRequest
	 * @return
	 * @version 2.0
	 */
	@SuppressWarnings("finally")
	@Transactional
	@RequestMapping(value = "/cancelEvent", method = RequestMethod.PUT)
	public EventResponse cancelEvent(@RequestBody EventPOJO eventRequest) {
		EventResponse response = new EventResponse();
		SendEmailController sendEmail = new SendEmailController();
		Boolean resultSendEmail = null;

		try {
			if (eventRequest.getEventId() != 0) {
				Event event = eventServiceInterface.getEventById(eventRequest.getEventId());
				event.setState((byte) 0);
				event.setPublishDate(new Date());

				int eventId = eventServiceInterface.saveEvent(event);

				if (eventId != 0) {
					response.setCode(200);
					response.setErrorMessage("success");

					List<UserPOJO> servicesProviders = userServiceInterface
							.getAllServicesProviderAuction(event.getEventId());

					if (!servicesProviders.isEmpty()) {
						for (UserPOJO sp : servicesProviders) {
							String fullName = sp.getName() + " " + sp.getLastName1() + " " + sp.getLastName2();

							resultSendEmail = sendEmail.sendNotificationCancelEvent(sp.getEmail(), fullName,
									event.getName());

							if (!resultSendEmail) {
								response.setCode(500);
								response.setErrorMessage("notification cancel event error");
								break;
							}
						}
					}
				} else {
					response.setCode(500);
					response.setErrorMessage("cancel event error");
				}
			} else {
				response.setCode(409);
				response.setErrorMessage("eventId is zero");
			}
		} catch (Exception e) {
			response.setCode(500);
			response.setCodeMessage(e.toString());
			e.printStackTrace();

		} finally {
			return response;
		}
	}

	/***
	 * Devuelve un evento cuando le envian el id encriptado
	 * 
	 * @author Alejandro bermudez Vargas
	 * @param eventRequest
	 * @return
	 * @version 1.0
	 */
	// get event by id
	@RequestMapping(value = "/getEventByEncryptId/{idEvent}", method = RequestMethod.GET)
	public EventResponse getEventByEncryptId(@PathVariable("idEvent") String id) {
		EventResponse response = new EventResponse();
		int eventId = Integer.parseInt(AES.base64decode(id));
		if (eventId != 0) {
			response.setEventPOJO(eventServiceInterface.eventById(eventId));
			response.getEventPOJO().setPrivate_((byte) 1);
			response.getEventPOJO().setState((byte) 1);
			response.getEventPOJO().setPublishDate(new Date());
			response.setCode(200);
		} else {
			response.setCode(400);
			response.setCodeMessage("Something is wrong");
		}
		return response;
	}

	/*
	 * @RequestMapping(value="/getActualEventStep", method= RequestMethod.GET)
	 * public EventResponse getActualEventStep(@PathVariable("idEvent") String
	 * id){ EventResponse response = new EventResponse(); int eventId =
	 * Integer.parseInt(AES.base64decode(id)); if(eventId != 0){ EventPOJO
	 * eventPOJO = eventServiceInterface.eventById(eventId);
	 * if(eventPOJO.getServiceContacts()==null) eventPOJO.setState(2);
	 * if(eventPOJO.getChats()==null) eventPOJO.setState(3);
	 * response.setEventPOJO(eventPOJO); }else{ response.setCode(400);
	 * response.setCodeMessage("Something is wrong"); } return response; }
	 */

	/***
	 * Crea un evento.
	 * 
	 * @author Enmanuel García González
	 * @param eventRequest
	 * @return
	 * @version 2.0
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value = "/createEvent", method = RequestMethod.POST)
	public EventResponse create(@RequestParam("name") String name, @RequestParam("description") String description,
			@RequestParam("largeDescription") String largeDescription, @RequestParam("eventType") int eventType,
			@RequestParam("eventPlaceName") String placeName, @RequestParam("placeLatitude") String placeLatitude,
			@RequestParam("placeLongitude") String placeLongitude, @RequestParam("loggedUser") int userId,
			@RequestParam("publishDate") String publishDate, @RequestParam(value="file", required=false) MultipartFile file) {
		EventResponse eventResponse = new EventResponse();
		Event event = new Event();
		Place place;

		try {
			if (file != null) {
				event.setImage(Utils.writeToFile(file, servletContext));
			}
			
			place = new Place();
			place.setLatitude(placeLatitude);
			place.setLongitude(placeLongitude);
			place.setName(placeName);
			place = placeServiceInterface.savePlace(place);

			User user = userServiceInterface.findById(userId);
			
			publishDate = publishDate.replace(" GMT-0600 (CST)", "");
			
			try{
				SimpleDateFormat format = new SimpleDateFormat("E MMM dd yyyy kk:mm:ss");
				Date date = format.parse(publishDate);
				event.setPublishDate(date);
			}catch(Exception e){ e.printStackTrace(); }
			
			event.setName(name);
			event.setDescription(description);
			event.setLargeDescription(largeDescription);
			event.setState((byte) 1);
			event.setPrivate_((byte) eventType);
			event.setRegisterDate(new Date());
			event.setUser(user);
			event.setPlace(place);

			int eventId = eventServiceInterface.saveEvent(event);
			
			EventParticipant promotor = new EventParticipant();
			promotor.setEvent(event);
			promotor.setUser(user);
			promotor.setState((byte) 2);
			eventParticipantServiceInterface.saveParticipant(promotor);
			
			if (eventId != 0) {
				eventResponse.setCode(200);
				EventPOJO eventPOJO = new EventPOJO();
				eventPOJO.setEventId(eventId);
				eventResponse.setEventPOJO(eventPOJO);
				eventResponse.setCodeMessage("Event created succesfully");
			} else {
				eventResponse.setCode(500);
			}
		} catch (Exception e) {
			eventResponse.setCode(500);
			eventResponse.setCodeMessage(e.toString());
			e.printStackTrace();

		} finally {
			return eventResponse;
		}
	}

	/**
	 * @author Ernesto Mendez A.
	 * @param event
	 * @param file
	 * @return
	 * @version 1.0
	 */
	@RequestMapping(value = "/editEvent", method = RequestMethod.POST)
	public BaseResponse edit(@RequestParam("name") String name, @RequestParam("description") String description,
			@RequestParam("largeDescription") String largeDescription, @RequestParam("eventType") int eventType,
			@RequestParam("placeName") String placeName, @RequestParam("placeLatitude") String placeLatitude,
			@RequestParam("placeLongitude") String placeLongitude, @RequestParam("owner") int userId,
			@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("eventId") int eventId,
			@RequestParam("publishDate") String publishDate, @RequestParam("placeId") int placeId) {
		BaseResponse response = new BaseResponse();
		
		Event event = new Event();
		event.setEventId(eventId);
		event.setName(name);
		event.setDescription(description);
		event.setLargeDescription(largeDescription);
		event.setPrivate_((byte) eventType);
		
		publishDate = publishDate.replace(" GMT-0600 (CST)", "");
		
		try{
			SimpleDateFormat format = new SimpleDateFormat("E MMM dd yyyy kk:mm:ss");
			Date date = format.parse(publishDate);
			event.setPublishDate(date);
		}catch(Exception e){ e.printStackTrace(); }

		Place place = new Place();
		place.setPlaceId(placeId);
		place.setName(placeName);
		place.setLatitude(placeLatitude);
		place.setLongitude(placeLongitude);
		event.setPlace(place);
		
		if(eventServiceInterface.editEvent(event, file, servletContext)){
			response.setCode(200);
			response.setCodeMessage("success");
		}else{
			response.setCode(500);
			response.setCodeMessage("internal error");
		}
		
		return response;
	}

	/**
	 * @author Antoni Ramirez Montano
	 * @param nameUser criterio a consultar
	 * @param namePlace criterio a consultar
	 * @param name criterio a consultar
	 * @param publishDate criterio a consultar
	 * @return lista de eventos basados en los criterios
	 */
	@RequestMapping(value="/getEventByParams/{nameUser}/{name}/{namePlace}", method= RequestMethod.GET)
	public EventResponse getEventByParams(@PathVariable("nameUser") String nameUser, @PathVariable("name") String name, @PathVariable("namePlace")String namePlace){
		EventResponse e = new EventResponse();
		e.setEventList(eventServiceInterface.getAllByParam(nameUser, name, namePlace, (byte)3));
		e.setCode(200);
		return e;
	}
}