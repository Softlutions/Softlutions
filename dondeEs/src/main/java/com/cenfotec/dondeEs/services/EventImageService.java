package com.cenfotec.dondeEs.services;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cenfotec.dondeEs.pojo.EventImagePOJO;
import com.cenfotec.dondeEs.pojo.EventParticipantPOJO;
import com.cenfotec.dondeEs.pojo.OfflineUserPOJO;
import com.cenfotec.dondeEs.pojo.UserPOJO;
import com.cenfotec.dondeEs.ejb.EventImage;
import com.cenfotec.dondeEs.ejb.EventParticipant;
import com.cenfotec.dondeEs.repositories.EventImageRepository;
import com.cenfotec.dondeEs.repositories.EventParticipantRepository;
import com.cenfotec.dondeEs.utils.Utils;

@Service
public class EventImageService implements EventImageServiceInterface {
	@Autowired private EventParticipantRepository eventParticipantRepository;
	@Autowired private EventImageRepository eventImageRepository;
	@Autowired private ServletContext servletContext;
	
	@Override
	public Boolean saveImage(int eventParticipantId, MultipartFile file) {
		boolean saved = false;
		String img = null;
		
		EventImage eventImage = new EventImage();
		
		EventParticipant eventParticipant = eventParticipantRepository.findOne(eventParticipantId);
		eventImage.setEventParticipant(eventParticipant);
		
		if(file != null)
			img = Utils.writeToFile(file, servletContext);
		
		if(img != null){
			eventImage.setImage(img);
			eventImageRepository.save(eventImage);
			saved = true;
		}
		
		return saved;
	}
	
	@Override
	public List<EventImagePOJO> getAllByEventId(int eventId) {
		List<EventImage> imgs = eventImageRepository.findAllByEventParticipantEventEventId(eventId);
		List<EventImagePOJO> images = new ArrayList<>();
		
		imgs.stream().forEach(i -> {
			EventImagePOJO img = new EventImagePOJO();
			
			img.setEventImageId(i.getEventImagesId());
			img.setImage(i.getImage());
			
			EventParticipantPOJO eventParticipant = new EventParticipantPOJO();
			eventParticipant.setEventParticipantId(i.getEventParticipant().getEventParticipantId());
			eventParticipant.setState(i.getEventParticipant().getState());
			
			if(i.getEventParticipant().getOfflineUser() != null){
				OfflineUserPOJO offlineUser = new OfflineUserPOJO();
				offlineUser.setOfflineUserId(i.getEventParticipant().getOfflineUser().getOfflineUserId());
				offlineUser.setEmail(i.getEventParticipant().getOfflineUser().getEmail());
				eventParticipant.setOfflineUser(offlineUser);
			}
			
			if(i.getEventParticipant().getUser() != null){
				UserPOJO user = new UserPOJO();
				user.setUserId(i.getEventParticipant().getUser().getUserId());
				user.setName(i.getEventParticipant().getUser().getName());
				user.setLastName1(i.getEventParticipant().getUser().getLastName1());
				user.setLastName2(i.getEventParticipant().getUser().getLastName2());
				user.setEmail(i.getEventParticipant().getUser().getEmail());
				eventParticipant.setUser(user);
			}
			
			img.setEventParticipant(eventParticipant);
			
			images.add(img);
		});
		
		return images;
	}
}