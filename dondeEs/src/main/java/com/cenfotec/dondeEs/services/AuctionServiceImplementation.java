package com.cenfotec.dondeEs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cenfotec.dondeEs.repositories.AuctionServiceRepository;
import com.cenfotec.dondeEs.repositories.ServiceContactRepository;
import com.cenfotec.dondeEs.contracts.ContractNotification;
import com.cenfotec.dondeEs.controller.SendEmailController;
import com.cenfotec.dondeEs.ejb.AuctionService;
import com.cenfotec.dondeEs.ejb.ServiceContact;
import com.cenfotec.dondeEs.pojo.EventPOJO;
import com.cenfotec.dondeEs.pojo.ServicePOJO;
import com.cenfotec.dondeEs.pojo.UserPOJO;

@Service
public class AuctionServiceImplementation implements AuctionServiceImpInterface{

	@Autowired private AuctionServiceRepository auctionServiceRepository;
	@Autowired private SendEmailController sendEmailController;
	@Autowired private ServiceContactRepository serviceContactRepository;

	@Override
	public Boolean saveAuctionService(AuctionService service) {
		AuctionService auctionService =  auctionServiceRepository.save(service);
	 	return (auctionService == null) ? false : true;	
	}
	
	@Override
	@Transactional
	public Boolean contract(int auctionServiceId){
		AuctionService auctionService = auctionServiceRepository.findOne(auctionServiceId);
		
		ContractNotification contractNotification = new ContractNotification();
		
		EventPOJO eventPOJO = new EventPOJO();
		eventPOJO.setEventId(auctionService.getAuction().getEvent().getEventId());
		contractNotification.setEvent(eventPOJO);
		
		ServicePOJO servicePOJO = new ServicePOJO();
		servicePOJO.setServiceId(auctionService.getService().getServiceId());
		contractNotification.setService(servicePOJO);
		
		UserPOJO userPOJO = new UserPOJO();
		userPOJO.setEmail(auctionService.getService().getUser().getEmail());
		servicePOJO.setUser(userPOJO);
		
		// incluir validacion de si ya se habia enviado la notificacion
		boolean isValid = true;
		
		if(isValid){
			ServiceContact serviceContact = new ServiceContact();
			serviceContact.setEvent(auctionService.getAuction().getEvent());
			serviceContact.setService(auctionService.getService());
			serviceContactRepository.save(serviceContact);
			
			sendEmailController.sendEmailContractNotification(contractNotification);
		}
		
		return isValid;
	}
}