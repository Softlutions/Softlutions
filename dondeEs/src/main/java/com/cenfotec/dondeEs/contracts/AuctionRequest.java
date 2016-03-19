package com.cenfotec.dondeEs.contracts;

public class AuctionRequest extends BaseResponse {
	
	private int auctionServiceId;
	private int initialPrice;
	private String description;
	private boolean acept;
	
	public int getAuctionServiceId() {
		return auctionServiceId;
	}
	
	public void setAuctionServiceId(int auctionServiceId) {
		this.auctionServiceId = auctionServiceId;
	}
	
	public int getInitialPrice() {
		return initialPrice;
	}
	
	public void setInitialPrice(int initialPrice) {
		this.initialPrice = initialPrice;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean isAcept() {
		return acept;
	}
	
	public void setAcept(boolean acept) {
		this.acept = acept;
	}

}
