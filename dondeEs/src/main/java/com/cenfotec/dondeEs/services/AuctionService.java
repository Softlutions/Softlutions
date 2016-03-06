package com.cenfotec.dondeEs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cenfotec.dondeEs.ejb.Auction;
import com.cenfotec.dondeEs.repositories.AuctionRepository;

@Service
public class AuctionService implements AuctionServiceInterface{

	@Autowired private AuctionRepository auctionRepository;
	
	@Override
	public Boolean saveAuction(Auction auction) {
		Auction serviceContact =  auctionRepository.save(auction);
	 	return (serviceContact == null) ? false : true;
	}
}
