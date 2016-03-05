package com.cenfotec.dondeEs.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.pojo.AuctionPOJO;
import com.cenfotec.dondeEs.pojo.AuctionServicePOJO;
import com.cenfotec.dondeEs.repositories.AuctionRepository;

@Service
public class AuctionService implements AuctionServiceInterface{
	@Autowired
	private AuctionRepository auctionRepository;
	
	@Override
	public List<AuctionPOJO> getAllAuctionByEvent(int event_id) {
		List<AuctionPOJO> auctionsPOJO = new ArrayList<>();		
		auctionRepository.findAllByEventEventId(event_id).stream().forEach(e -> {
			AuctionPOJO auctionPOJO = new AuctionPOJO();
			List<AuctionServicePOJO> auctionServicesPOJO = new ArrayList<>();
			BeanUtils.copyProperties(e, auctionPOJO);
			BeanUtils.copyProperties(e.getAuctionServices(), auctionServicesPOJO);
			auctionPOJO.setAuctionServices(auctionServicesPOJO);
			auctionPOJO.setEvent(null);
			auctionsPOJO.add(auctionPOJO); 
		});
		
		return auctionsPOJO;		
	}
}