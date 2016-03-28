package com.cenfotec.dondeEs.contracts;

import java.util.List;
import com.cenfotec.dondeEs.pojo.EventImagePOJO;

public class EventImageResponse extends BaseResponse {

	List<EventImagePOJO> images;

	public List<EventImagePOJO> getImages() {
		return images;
	}

	public void setImages(List<EventImagePOJO> comments) {
		this.images = comments;
	}
	
}
