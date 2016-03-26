package com.cenfotec.dondeEs.services;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.cenfotec.dondeEs.pojo.EventImagePOJO;
import com.cenfotec.dondeEs.ejb.EventImage;

public interface EventImageServiceInterface {

	Boolean saveImage(int eventParticipantId, MultipartFile file);
	List<EventImagePOJO> getAllByEventId(int eventId);
}
