package com.cenfotec.dondeEs.controller;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.dondeEs.contracts.AuctionServiceResponse;
import com.cenfotec.dondeEs.contracts.BaseResponse;
import com.cenfotec.dondeEs.ejb.AuctionService;
import com.cenfotec.dondeEs.services.AuctionServiceImpInterface;

@RestController
@RequestMapping(value = "rest/protected/auctionService")
public class AuctionServiceController {
	
	@Autowired private AuctionServiceImpInterface auctionServiceImpInterface;
	
	@RequestMapping(value ="/createAuctionService", method = RequestMethod.POST)
	@Transactional
	public AuctionServiceResponse createAuctionService(@RequestBody AuctionService auctionService){
		AuctionServiceResponse response = new AuctionServiceResponse();
		Boolean state = auctionServiceImpInterface.saveAuctionService(auctionService);
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
	 * @param auctionServiceId id del servicio en subasta a contratar
	 * @return resultado del contrato
	 * @version 1.0
	 */
	@RequestMapping(value ="/contract/{auctionServiceId}", method = RequestMethod.GET)
	public BaseResponse contract(@PathVariable("auctionServiceId") int auctionServiceId){				
		BaseResponse response = new BaseResponse();
		
		if(auctionServiceImpInterface.contract(auctionServiceId)){
			response.setCode(200);
			response.setCodeMessage("Successful");
		}else{
			response.setCode(400);
			response.setCodeMessage("You already was invited");
		}
		
		return response;
	}
	
	@RequestMapping(value ="/getAllAuctionServicesByAuctionId/{AuctionId}", method = RequestMethod.GET)
	public AuctionServiceResponse getAllAuctionServicesByAuctionId(@PathVariable("AuctionId") int AuctionId){				
 		AuctionServiceResponse response = new AuctionServiceResponse();
 		response.setAuctionServiceList(auctionServiceImpInterface.getAllAuctionServicesByAuctionId(AuctionId));
 		return response;
 	}	
}
