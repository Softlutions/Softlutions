package com.cenfotec.dondeEs.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.ejb.Auction;
import com.cenfotec.dondeEs.pojo.AuctionPOJO;
import com.cenfotec.dondeEs.pojo.AuctionServicePOJO;
import com.cenfotec.dondeEs.pojo.ServicePOJO;
import com.cenfotec.dondeEs.pojo.UserPOJO;
import com.cenfotec.dondeEs.repositories.AuctionRepository;

@Service
public class AuctionService implements AuctionServiceInterface{
	@Autowired
	private AuctionRepository auctionRepository;
	
	/***
	 * Obtiene todas las subastas de un evento.
	 * @author Enmanuel García González
	 * @version 1.0
	 */
	@Override
	@Transactional
	public List<AuctionPOJO> getAllAuctionByEvent(int event_id) {
		List<Auction> auctions = auctionRepository.findAllByEventEventId(event_id);	
		List<AuctionPOJO> auctionsPOJO = new ArrayList<AuctionPOJO>();
		auctions.stream().forEach(e -> {
			AuctionPOJO auctionPOJO = new AuctionPOJO();
			BeanUtils.copyProperties(e, auctionPOJO);
			
			if (e.getAuctionServices() != null) {
				List<AuctionServicePOJO> auctionServicesPOJO = new ArrayList<AuctionServicePOJO>();	
				
				e.getAuctionServices().stream().forEach(as -> {
					AuctionServicePOJO asp = new AuctionServicePOJO();
					BeanUtils.copyProperties(as, asp);
					
					asp.setService(new ServicePOJO()); 
					BeanUtils.copyProperties(as.getService(), asp.getService());
					asp.getService().setServiceContacts(null);
					asp.getService().setServiceCatalog(null);
					
					asp.getService().setUser(new UserPOJO()); 
					asp.getService().getUser().setUserId(as.getService().getUser().getUserId());
					
					auctionServicesPOJO.add(asp);
				});
				
				auctionPOJO.setAuctionServices(auctionServicesPOJO);
			} 			
			auctionPOJO.setEvent(null);
			auctionsPOJO.add(auctionPOJO); 
		});
		
		return auctionsPOJO;		
	}
}