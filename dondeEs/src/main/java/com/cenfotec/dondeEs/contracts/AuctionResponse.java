package com.cenfotec.dondeEs.contracts;

import java.util.List;

import com.cenfotec.dondeEs.pojo.AuctionPOJO;

public class AuctionResponse extends BaseResponse {
	
	List<AuctionPOJO> listAuctions;

	private List<AuctionPOJO> auctionList;

	public List<AuctionPOJO> getAuctionList() {
		return auctionList;
	}

	public void setAuctionList(List<AuctionPOJO> auctionList) {
		this.auctionList = auctionList;
	}

}
