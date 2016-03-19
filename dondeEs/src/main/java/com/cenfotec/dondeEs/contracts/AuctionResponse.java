package com.cenfotec.dondeEs.contracts;

import java.util.List;

import com.cenfotec.dondeEs.pojo.AuctionPOJO;

public class AuctionResponse extends BaseResponse {
	
	private AuctionPOJO auction;
	private List<AuctionPOJO> auctionList;

	public AuctionPOJO getAuction() {
		return auction;
	}

	public void setAuction(AuctionPOJO auction) {
		this.auction = auction;
	}
	
	public List<AuctionPOJO> getAuctionList() {
		return auctionList;
	}

	public void setAuctionList(List<AuctionPOJO> auctionList) {
		this.auctionList = auctionList;
	}

}
