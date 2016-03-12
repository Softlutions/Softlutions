package com.cenfotec.dondeEs.controller;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.cenfotec.dondeEs.contracts.AuctionResponse;
import com.cenfotec.dondeEs.ejb.Auction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.contracts.AuctionResponse;

import com.cenfotec.dondeEs.services.AuctionServiceInterface;

@RestController
@RequestMapping(value = "rest/protected/auction")
public class AuctionController {
	

	@Autowired private AuctionServiceInterface auctionServiceInterface;
	
	/**
	 * @Author Juan Carlos Sánchez G.
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

	
	@RequestMapping(value ="/getAllAuctionByEvent/{id}", method = RequestMethod.GET)
	public AuctionResponse getAllAuctionByEvent(@PathVariable("id") int id){				
		AuctionResponse response = new AuctionResponse();
		response.setCode(200);
		response.setCodeMessage("Auctions by event");
		response.setAuctionList(auctionServiceInterface.getAllAuctionByEvent(id));
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
		auction.setDate(new Date());
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