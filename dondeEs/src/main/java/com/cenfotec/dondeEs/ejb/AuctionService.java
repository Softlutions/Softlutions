package com.cenfotec.dondeEs.ejb;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the auction_services database table.
 * 
 */
@Entity
@Table(name="auction_services")
@NamedQuery(name="AuctionService.findAll", query="SELECT a FROM AuctionService a")
public class AuctionService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="auction_services_id")
	private int auctionServicesId;

	private byte acept;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private String description;

	private int price;

	//bi-directional many-to-one association to Auction
	@ManyToOne //(fetch=FetchType.LAZY)
	@JoinColumn(name="auction_id")
	private Auction auction;

	//bi-directional many-to-one association to Service
	@ManyToOne //(fetch=FetchType.LAZY)
	@JoinColumn(name="service_id")
	private Service service;

	public AuctionService() {
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

	public Auction getAuction() {
		return this.auction;
	}

	public void setAuction(Auction auction) {
		this.auction = auction;
	}

	public Service getService() {
		return this.service;
	}

	public void setService(Service service) {
		this.service = service;
	}

}