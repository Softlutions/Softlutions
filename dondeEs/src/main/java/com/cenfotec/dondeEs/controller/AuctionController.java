package com.cenfotec.dondeEs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.cenfotec.dondeEs.contracts.AuctionResponse;
import com.cenfotec.dondeEs.ejb.Auction;
import com.cenfotec.dondeEs.services.AuctionServiceInterface;

@RestController
@RequestMapping(value = "rest/protected/auction")
public class AuctionController {
	
	@Autowired private AuctionServiceInterface auctionResponseInterface;
	
	@RequestMapping(value ="/createAuction", method = RequestMethod.POST)
	public AuctionResponse createAuction(@RequestBody Auction auction){
		AuctionResponse response = new AuctionResponse();
		Boolean state = auctionResponseInterface.saveAuction(auction);
		if(state){
			response.setCode(200);
			response.setCodeMessage("Succesfull");
		}else{
			response.setCode(500);
			response.setCodeMessage("Internal error");
		}
		return response;
	}

}
