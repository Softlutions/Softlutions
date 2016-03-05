package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.pojo.AuctionPOJO;

public interface AuctionServiceInterface {
	List<AuctionPOJO> getAllAuctionByEvent(int event_id);
}
