package com.cenfotec.dondeEs.controller;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.cenfotec.dondeEs.contracts.AuctionResponse;
import com.cenfotec.dondeEs.ejb.Auction;
import org.springframework.web.bind.annotation.PathVariable;
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
}