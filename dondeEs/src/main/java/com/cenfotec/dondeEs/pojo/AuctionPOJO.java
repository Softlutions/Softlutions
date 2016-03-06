package com.cenfotec.dondeEs.pojo;

import java.util.Date;
import java.util.List;

import com.cenfotec.dondeEs.ejb.AuctionService;

public class AuctionPOJO {

	private int auctionId;

	private Date date;

	private String description;

	private String name;

	private List<AuctionServicePOJO> auctionServices;

	public AuctionPOJO() {
	}

	public int getAuctionId() {
		return this.auctionId;
	}

	public void setAuctionId(int auctionId) {
		this.auctionId = auctionId;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AuctionServicePOJO> getAuctionServices() {
		return this.auctionServices;
	}

	public void setAuctionServices(List<AuctionServicePOJO> auctionServices) {
		this.auctionServices = auctionServices;
	}

	public AuctionServicePOJO addAuctionService(AuctionServicePOJO auctionService) {
		getAuctionServices().add(auctionService);
		auctionService.setAuction(this);

		return auctionService;
	}

	public AuctionService removeAuctionService(AuctionService auctionService) {
		getAuctionServices().remove(auctionService);
		auctionService.setAuction(null);

		return auctionService;
	}

}
