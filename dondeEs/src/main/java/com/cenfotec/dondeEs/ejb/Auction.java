package com.cenfotec.dondeEs.ejb;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the auction database table.
 * 
 */
@Entity
@Table(name="auction")
@NamedQuery(name="Auction.findAll", query="SELECT a FROM Auction a")
public class Auction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="auction_id")
	private int auctionId;

	@Temporal(TemporalType.TIMESTAMP) 
	private Date date;

	private String description;

	private String name;

	//bi-directional many-to-one association to Event
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="event_id")
	private Event event;

	//bi-directional many-to-one association to AuctionService
	@OneToMany(mappedBy="auction")
	private List<AuctionService> auctionServices;

	public Auction() {
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

	public Event getEvent() {
		return this.event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public List<AuctionService> getAuctionServices() {
		return this.auctionServices;
	}

	public void setAuctionServices(List<AuctionService> auctionServices) {
		this.auctionServices = auctionServices;
	}

	public AuctionService addAuctionService(AuctionService auctionService) {
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