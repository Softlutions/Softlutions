package com.cenfotec.dondeEs.services;

import java.util.List;
import com.cenfotec.dondeEs.pojo.AuctionPOJO;
import com.cenfotec.dondeEs.pojo.AuctionServicePOJO;
import com.cenfotec.dondeEs.ejb.Auction;

public interface AuctionServiceInterface {

	Boolean saveAuction(Auction auction);
	List<AuctionPOJO> getAllAuctionByEvent(int event_id);
	List<AuctionPOJO> getAllAuctions();
	AuctionServicePOJO getAuctionService(int auction_id);
	List<AuctionPOJO> getAllByAuctionsByServiceCatalog(int serviceCatalog_id);
}
