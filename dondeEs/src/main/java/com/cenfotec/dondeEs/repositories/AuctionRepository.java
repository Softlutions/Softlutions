package com.cenfotec.dondeEs.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.cenfotec.dondeEs.ejb.Auction;

public interface AuctionRepository extends CrudRepository<Auction, Integer>{
	
	List<Auction> findAll();
}