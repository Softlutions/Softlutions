package com.cenfotec.dondeEs.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.cenfotec.dondeEs.ejb.Auction;

public interface AuctionRepository extends CrudRepository<Auction, Integer>{
	
	List<Auction> findAll();
	List<Auction> findAllByEventEventId(int event_id);
	List<Auction> findAllByState(byte state);
	List<Auction> findAllByServiceCatalogServiceCatalogIdAndState(int serviceCataloId, byte state);
}