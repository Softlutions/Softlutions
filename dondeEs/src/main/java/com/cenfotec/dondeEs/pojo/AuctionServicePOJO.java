package com.cenfotec.dondeEs.pojo;

import java.util.Date;
import com.cenfotec.dondeEs.pojo.AuctionPOJO;
import com.cenfotec.dondeEs.pojo.ServicePOJO;

public class AuctionServicePOJO {
	
	private int auctionServicesId;

	private byte acept;

	private Date date;

	private String description;

	private int price;

	private AuctionPOJO auction;

	private ServicePOJO service;

	public AuctionServicePOJO() {
	}

	public int getAuctionServicesId() {
		return this.auctionServicesId;
	}

	public void setAuctionServicesId(int auctionServicesId) {
		this.auctionServicesId = auctionServicesId;
	}

	public byte getAcept() {
		return this.acept;
	}

	public void setAcept(byte acept) {
		this.acept = acept;
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

	public int getPrice() {
		return this.price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public AuctionPOJO getAuction() {
		return this.auction;
	}

	public void setAuction(AuctionPOJO auction) {
		this.auction = auction;
	}

	public ServicePOJO getService() {
		return this.service;
	}

	public void setService(ServicePOJO service) {
		this.service = service;
	}

}
