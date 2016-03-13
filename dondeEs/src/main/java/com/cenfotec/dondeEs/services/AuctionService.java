package com.cenfotec.dondeEs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cenfotec.dondeEs.ejb.Auction;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import com.cenfotec.dondeEs.pojo.AuctionPOJO;
import com.cenfotec.dondeEs.pojo.AuctionServicePOJO;
import com.cenfotec.dondeEs.pojo.EventPOJO;
import com.cenfotec.dondeEs.pojo.ServicePOJO;
import com.cenfotec.dondeEs.pojo.UserPOJO;
import com.cenfotec.dondeEs.repositories.AuctionRepository;
import com.cenfotec.dondeEs.repositories.AuctionServiceRepository;

@Service
public class AuctionService implements AuctionServiceInterface{

	@Autowired private AuctionRepository auctionRepository;
	@Autowired private AuctionServiceRepository auctionServiceRepository;
	
	@Override
	public Boolean saveAuction(Auction auction) {
		Auction serviceContact =  auctionRepository.save(auction);
	 	return (serviceContact == null) ? false : true;
	}

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
			auctionsPOJO.add(auctionPOJO); 
		});
		
		return auctionsPOJO;		
	}
		
	@Override
	@Transactional
	public AuctionServicePOJO getAuctionService(int auctionServiceId) {
		com.cenfotec.dondeEs.ejb.AuctionService auctionService = auctionServiceRepository.findOne(auctionServiceId);
		
		AuctionServicePOJO auctionservicePOJO = null;
		
		if(auctionService != null){
			auctionservicePOJO = new AuctionServicePOJO();
			auctionservicePOJO.setAuctionServicesId(auctionService.getAuctionServicesId());
			auctionservicePOJO.setAcept(auctionService.getAcept());
			auctionservicePOJO.setDate(auctionService.getDate());
			auctionservicePOJO.setDescription(auctionService.getDescription());
			auctionservicePOJO.setPrice(auctionService.getPrice());
			
			AuctionPOJO auctionPOJO = new AuctionPOJO();
			auctionPOJO.setAuctionId(auctionService.getAuction().getAuctionId());
			auctionPOJO.setDate(auctionService.getAuction().getDate());
			auctionPOJO.setName(auctionService.getAuction().getName());
			auctionPOJO.setDescription(auctionService.getAuction().getDescription());
			auctionservicePOJO.setAuction(auctionPOJO);
			
			EventPOJO eventPOJO = new EventPOJO();
			eventPOJO.setEventId(auctionService.getAuction().getEvent().getEventId());
			eventPOJO.setName(auctionService.getAuction().getEvent().getName());
			eventPOJO.setDescription(auctionService.getAuction().getEvent().getDescription());
			eventPOJO.setLargeDescription(auctionService.getAuction().getEvent().getLargeDescription());
			eventPOJO.setImage(auctionService.getAuction().getEvent().getImage());
			eventPOJO.setPublishDate(auctionService.getAuction().getEvent().getPublishDate());
			auctionPOJO.setEvent(eventPOJO);
			
			UserPOJO userPOJO = new UserPOJO();
			userPOJO.setUserId(auctionService.getAuction().getEvent().getUser().getUserId());
			userPOJO.setName(auctionService.getAuction().getEvent().getUser().getName());
			userPOJO.setLastName1(auctionService.getAuction().getEvent().getUser().getLastName1());
			userPOJO.setLastName2(auctionService.getAuction().getEvent().getUser().getLastName2());
			userPOJO.setEmail(auctionService.getAuction().getEvent().getUser().getEmail());
			userPOJO.setImage(auctionService.getAuction().getEvent().getUser().getImage());
			eventPOJO.setUser(userPOJO);
			
			ServicePOJO servicePOJO = new ServicePOJO();
			servicePOJO.setServiceId(auctionService.getService().getServiceId());
			servicePOJO.setName(auctionService.getService().getName());
			servicePOJO.setDescription(auctionService.getService().getDescription());
			auctionservicePOJO.setService(servicePOJO);
		}
		
		return auctionservicePOJO;		
	}
	
	
	/**
	 * @Author Juan Carlos Sánchez G.
	 * @return response Respuesta del servidor de la petición que lista todas las subastas existentes.
	 * @version 1.0
	 */
	
	@Override
	@Transactional
	public List<AuctionPOJO> getAllAuctions() {
		List<Auction> auctions = auctionRepository.findAllByState((byte) 1);	
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
			auctionsPOJO.add(auctionPOJO); 
		});
		
		return auctionsPOJO;		
	}

	@Override
	public List<AuctionPOJO> getAllByAuctionsByServiceCatalog(int serviceCataloId) {
		List<Auction> auctions = auctionRepository.findAllByServiceCatalogServiceCatalogIdAndState(serviceCataloId,(byte) 1);	
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
			auctionsPOJO.add(auctionPOJO); 
		});
		
		return auctionsPOJO;
	}

	@Override
	public Auction findById(int auctionId) {
		Auction actn = auctionRepository.findOne(auctionId);
		return actn;
	}
}
