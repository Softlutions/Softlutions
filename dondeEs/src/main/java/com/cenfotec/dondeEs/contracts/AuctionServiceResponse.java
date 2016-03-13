package com.cenfotec.dondeEs.contracts;

import java.util.List;
import com.cenfotec.dondeEs.pojo.AuctionServicePOJO;

public class AuctionServiceResponse extends BaseResponse{
	
	private List<AuctionServicePOJO> auctionServiceList;
	private AuctionServicePOJO auctionService;

	public List<AuctionServicePOJO> getAuctionServiceList() {
		return auctionServiceList;
	}

	public void setAuctionServiceList(List<AuctionServicePOJO> auctionServiceList) {
		this.auctionServiceList = auctionServiceList;
	}

	public AuctionServicePOJO getAuctionService() {
		return auctionService;
	}

	public void setAuctionService(AuctionServicePOJO auctionService) {
		this.auctionService = auctionService;
	}


}
