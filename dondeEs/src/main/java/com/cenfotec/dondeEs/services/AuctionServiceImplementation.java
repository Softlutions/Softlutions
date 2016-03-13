package com.cenfotec.dondeEs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cenfotec.dondeEs.repositories.AuctionServiceRepository;
import com.cenfotec.dondeEs.ejb.AuctionService;

@Service
public class AuctionServiceImplementation implements AuctionServiceImpInterface{

	@Autowired private AuctionServiceRepository auctionServiceRepository;

	@Override
	public Boolean saveAuctionService(AuctionService service) {
		AuctionService auctionService =  auctionServiceRepository.save(service);
	 	return (auctionService == null) ? false : true;	
	}

}