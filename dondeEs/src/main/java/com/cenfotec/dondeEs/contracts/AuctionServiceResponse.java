package com.cenfotec.dondeEs.contracts;

import com.cenfotec.dondeEs.pojo.AuctionServicePOJO;

public class AuctionServiceResponse extends BaseResponse {
	
	private AuctionServicePOJO auctionService;

	public AuctionServicePOJO getAuctionService() {
		return auctionService;
	}

	public void setAuctionService(AuctionServicePOJO auctionService) {
		this.auctionService = auctionService;
	}

}
