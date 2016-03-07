package com.cenfotec.dondeEs.services;

import java.util.List;
import com.cenfotec.dondeEs.pojo.AuctionPOJO;
import com.cenfotec.dondeEs.pojo.UserPOJO;
import com.cenfotec.dondeEs.ejb.Auction;

public interface AuctionServiceInterface {

	Boolean saveAuction(Auction auction);
	List<AuctionPOJO> getAllAuctionByEvent(int event_id);

}
