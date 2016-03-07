package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.pojo.AuctionPOJO;
import com.cenfotec.dondeEs.pojo.UserPOJO;

public interface AuctionServiceInterface {
	List<AuctionPOJO> getAllAuctionByEvent(int event_id);
}
