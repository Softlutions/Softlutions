package com.cenfotec.dondeEs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.contracts.AuctionRequest;
import com.cenfotec.dondeEs.controller.SendEmailController;
import com.cenfotec.dondeEs.ejb.Auction;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import com.cenfotec.dondeEs.pojo.AuctionPOJO;
import com.cenfotec.dondeEs.pojo.AuctionServicePOJO;
import com.cenfotec.dondeEs.pojo.EventPOJO;
import com.cenfotec.dondeEs.pojo.ServiceCatalogPOJO;
import com.cenfotec.dondeEs.pojo.ServicePOJO;
import com.cenfotec.dondeEs.pojo.UserPOJO;
import com.cenfotec.dondeEs.repositories.AuctionRepository;
import com.cenfotec.dondeEs.repositories.AuctionServiceRepository;
import com.cenfotec.dondeEs.repositories.ServiceRepository;

@Service
public class AuctionService implements AuctionServiceInterface{

	@Autowired private AuctionRepository auctionRepository;
	@Autowired private AuctionServiceRepository auctionServiceRepository;
	@Autowired private ServiceRepository serviceRepository;
	@Autowired private SendEmailController sendEmailController;
	
	@Override
	public Boolean saveAuction(Auction auction) {
		Auction saveAuction =  auctionRepository.save(auction);
		List<com.cenfotec.dondeEs.ejb.Service> services = serviceRepository.getByCatalogId(auction.getServiceCatalog().getServiceCatalogId());
		
		if(saveAuction != null){
			new Thread("sendAuctionInvitationEmail"){
				public void run(){
					List<String> userInvited = new ArrayList<>();
					
					services.forEach(s -> {
						String email = s.getUser().getEmail();
						
						if(s.getState() == 1 && !userInvited.contains(email)){
							sendEmailController.sendAuctionInvitationEmail(saveAuction, email);
							userInvited.add(email);
						}
							
					});
				}
			}.start();
		}
		
	 	return (saveAuction != null);
	}
	
	@Override
	public Boolean finishAuction(Auction auction) {
		Auction saveAuction =  auctionRepository.save(auction);
	 	return (saveAuction != null);
	}
	
	@Override
	@Transactional
	public List<AuctionPOJO> getAllAuctionByEvent(int event_id) {
		List<Auction> auctions = auctionRepository.findAllByEventEventId(event_id);	
		List<AuctionPOJO> auctionsPOJO = new ArrayList<AuctionPOJO>();
		Date date = new Date();
		auctions.stream().forEach(e -> {
			AuctionPOJO auctionPOJO = new AuctionPOJO();
			if(e.getState()==1 && e.getDate().compareTo(date)!=1){
				e.setState((byte) 2);			
			}
			BeanUtils.copyProperties(e, auctionPOJO);
			
			auctionPOJO.setServiceCatalog(new ServiceCatalogPOJO());
			auctionPOJO.getServiceCatalog().setName(e.getServiceCatalog().getName());;
			
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
	 * @return response Respuesta del servidor de la petición que lista todas las subastas.
	 * @version 1.0
	 */
	@Override
	@Transactional
	public List<AuctionPOJO> getAllAuctions() {
		List<Auction> auctions = auctionRepository.findAll();	
		List<AuctionPOJO> auctionsPOJO = new ArrayList<AuctionPOJO>();
		Date date = new Date();
		auctions.stream().forEach(e -> {
			AuctionPOJO auctionPOJO = new AuctionPOJO();
			if(e.getState()==1 && e.getDate().compareTo(date)!=1){
				e.setState((byte) 2);			
			}
			auctionPOJO.setAuctionId(e.getAuctionId());
			auctionPOJO.setDate(e.getDate());
			auctionPOJO.setDescription(e.getDescription());
			auctionPOJO.setName(e.getName());
			auctionPOJO.setState(e.getState());
			
			ServiceCatalogPOJO serviceCatalogPOJO = new ServiceCatalogPOJO();
			serviceCatalogPOJO.setServiceCatalogId(e.getServiceCatalog().getServiceCatalogId());
			serviceCatalogPOJO.setName(e.getServiceCatalog().getName());
			auctionPOJO.setServiceCatalog(serviceCatalogPOJO);
			
			auctionsPOJO.add(auctionPOJO); 
		});
		
		return auctionsPOJO;		
	}

	@Override
	public Auction findById(int auctionId) {
		Auction actn = auctionRepository.findOne(auctionId);
		return actn;
	}
	
	@Override
	@Transactional
	public boolean auctionInvitationAnswer(AuctionRequest request){
		com.cenfotec.dondeEs.ejb.AuctionService auctionService = auctionServiceRepository.findOne(request.getAuctionServiceId());
		boolean state = false;
		
		if(auctionService != null && auctionService.getAcept() == 0){
			if(!request.isAcept()){
				auctionServiceRepository.delete(auctionService);
			}else{
				auctionService.setAcept((byte) 1);
				auctionService.setDescription(request.getDescription());
				auctionService.setPrice(request.getInitialPrice());
			}
			
			state = true;
		}
		
		return state;
	}

	@Override
	public AuctionPOJO getAuctionById(int auctionId) {
		Auction auction = auctionRepository.findOne(auctionId);
		AuctionPOJO auctionPOJO = new AuctionPOJO();
		
		auctionPOJO.setAuctionId(auction.getAuctionId());
		auctionPOJO.setDate(auction.getDate());
		auctionPOJO.setDescription(auction.getDescription());
		auctionPOJO.setName(auction.getName());
		auctionPOJO.setState(auction.getState());
		
		ServiceCatalogPOJO serviceCatalogPOJO = new ServiceCatalogPOJO();
		serviceCatalogPOJO.setServiceCatalogId(auction.getServiceCatalog().getServiceCatalogId());
		serviceCatalogPOJO.setName(auction.getServiceCatalog().getName());
		auctionPOJO.setServiceCatalog(serviceCatalogPOJO);
		
		return auctionPOJO;
	}

	@Override
	public AuctionPOJO getAllServicesByAuction(int auctionId) {
		Auction auction = auctionRepository.findOne(auctionId);
		AuctionPOJO auctionPOJO = null;
		
		if(auction != null){
			auctionPOJO = new AuctionPOJO();
			
			auctionPOJO.setAuctionId(auction.getAuctionId());
			auctionPOJO.setDate(auction.getDate());
			auctionPOJO.setDescription(auction.getDescription());
			auctionPOJO.setName(auction.getName());
			auctionPOJO.setState(auction.getState());
			
			if (auction.getAuctionServices() != null) {
				List<AuctionServicePOJO> auctionServicesPOJO = new ArrayList<AuctionServicePOJO>();	
				
				auction.getAuctionServices().stream().forEach(a -> {
					AuctionServicePOJO s = new AuctionServicePOJO();
					BeanUtils.copyProperties(a, s);
					
					s.setService(new ServicePOJO()); 
					BeanUtils.copyProperties(a.getService(), s.getService());
					s.getService().setServiceContacts(null);
					s.getService().setServiceCatalog(null);									
					s.getService().setUser(null);
					
					auctionServicesPOJO.add(s);
				});
			
				auctionPOJO.setAuctionServices(auctionServicesPOJO);
			}
		}
		
		return auctionPOJO;
	}

}
