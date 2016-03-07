package com.cenfotec.dondeEs.controller;

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
	
	@RequestMapping(value ="/getAllAuctionByEvent/{id}", method = RequestMethod.GET)
	public AuctionResponse getAllAuctionByEvent(@PathVariable("id") int id){				
		AuctionResponse response = new AuctionResponse();
		response.setCode(200);
		response.setCodeMessage("Auctions by event");
		response.setAuctionList(auctionServiceInterface.getAllAuctionByEvent(id));
		return response;
	}
}