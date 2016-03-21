package com.cenfotec.dondeEs.controller;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.contracts.AuctionRequest;
import com.cenfotec.dondeEs.contracts.AuctionResponse;
import com.cenfotec.dondeEs.contracts.AuctionServiceResponse;
import com.cenfotec.dondeEs.ejb.Auction;

import org.springframework.web.bind.annotation.PathVariable;

import com.cenfotec.dondeEs.services.AuctionServiceInterface;

@RestController
@RequestMapping(value = "rest/protected/auction")
public class AuctionController {
	

	@Autowired private AuctionServiceInterface auctionServiceInterface;
	
	/**
	 * @author Juan Carlos Sánchez G. (Autor)
	 * @author Ernesto Mendez A. (Contribuyente)
	 * @param auction Peticion que contiene la información de la subasta por crear.
	 * @return response Respuesta del servidor de la petición.
	 * @version 1.0
	 */
	@RequestMapping(value ="/createAuction", method = RequestMethod.POST)
	@Transactional
	public AuctionResponse createAuction(@RequestBody Auction auction){
		AuctionResponse response = new AuctionResponse();
		auction.setState((byte)1);
		Boolean state = auctionServiceInterface.saveAuction(auction);
		if(state){
			response.setCode(200);
			response.setCodeMessage("Succesfull");
		}else{
			response.setCode(500);
			response.setCodeMessage("Internal error");
		}
		return response;
	}
	
	/**
	 * @author Ernesto Mendez A.
	 * @param id Id de la subasta a obtener
	 * @return Informacion de la subasta, servicio y evento
	 * @version 1.0
	 */
	@RequestMapping(value ="/getAuctionService/{id}", method = RequestMethod.GET)
	public AuctionServiceResponse getAuctionService(@PathVariable("id") int id){				
		AuctionServiceResponse response = new AuctionServiceResponse();
		response.setCode(200);
		response.setCodeMessage("Auction service fetched successfully");
		response.setAuctionService(auctionServiceInterface.getAuctionService(id));
		return response;
	}
	
	/**
	 * @author Ernesto Mendez A.
	 * @param request Respuesta de la invitacion a la subasta
	 * @return codigo y mensaje del resultado de la operacion
	 * @version 1.0
	 */
	@RequestMapping(value ="/auctionInvitationAnswer", method = RequestMethod.POST)
	public AuctionResponse auctionInvitationAnswer(@RequestBody AuctionRequest request){				
		AuctionResponse response = new AuctionResponse();
		
		if(auctionServiceInterface.auctionInvitationAnswer(request)){
			response.setCode(200);
			response.setCodeMessage("Successfull");
		}else{
			response.setCode(500);
			response.setCodeMessage("Something is wrong");
		}
		
		return response;
	}
	
	/**
	 * Obtiene todas las subastas de un determinado evento.
	 * @author Enmanuel García González
	 * @param id
	 * @return subastas con sus respectivos servicios de un evento.
	 * @version 1.0
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value ="/getAllAuctionByEvent/{id}", method = RequestMethod.GET)
	public AuctionResponse getAllAuctionByEvent(@PathVariable("id") int id){
		AuctionResponse response = new AuctionResponse();
		
		try {
			response.setAuctionList(auctionServiceInterface.getAllAuctionByEvent(id));
			response.setCode(200);
			response.setCodeMessage("Auctions fetch success");
			
		} catch (Exception e) {
			response.setCode(500);
			response.setCodeMessage(e.toString());
			e.printStackTrace();

		} finally { return response; }
	}

	/**
	 * @Author Juan Carlos Sánchez G.
	 * @return response Respuesta del servidor de la petición.
	 * @version 1.0
	 */
	@RequestMapping(value ="/getAllAuctions", method = RequestMethod.GET)
	public AuctionResponse getAllAuctions(){				
		AuctionResponse response = new AuctionResponse();
		response.setAuctionList(auctionServiceInterface.getAllAuctions());
		return response;
	}

	/**
	 * @author Antoni Ramirez Montano
	 * @param nauction se recibe la subasta a la modificar
	 * @return retorna la respuesta con el estado del url
	 * @version 1.0
	 */
	@RequestMapping(value ="/finishAuction", method = RequestMethod.PUT)
	@Transactional
	public AuctionResponse finishAuction(@RequestBody Auction nauction){
		AuctionResponse auctionResponse = new AuctionResponse();
		if(nauction.getAuctionId() != 0){
		Auction auction = auctionServiceInterface.findById(nauction.getAuctionId());
		
		auction.setState((byte)0);
		
		Boolean stateAuction = auctionServiceInterface.saveAuction(auction);
		
		if(stateAuction){
			auctionResponse.setCode(200);
		}else{
			auctionResponse.setCode(500);
		}
		}
		return auctionResponse;
	}
}