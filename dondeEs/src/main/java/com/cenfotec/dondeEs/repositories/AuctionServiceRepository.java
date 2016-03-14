package com.cenfotec.dondeEs.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.cenfotec.dondeEs.ejb.AuctionService;

public interface AuctionServiceRepository extends CrudRepository<AuctionService, Integer> {
	
	List<AuctionService> findAll();
	List<AuctionService> findAllByAuctionAuctionId(int auction_id);
}