package com.cenfotec.dondeEs.contracts;

import java.util.List;

import com.cenfotec.dondeEs.pojo.AuctionPOJO;

public class AuctionResponse extends BaseResponse {
	private List<AuctionPOJO> listAuction;

	public List<AuctionPOJO> getListAuction() {
		return listAuction;
	}

	public void setListAuction(List<AuctionPOJO> listAuction) {
		this.listAuction = listAuction;
	}
}
