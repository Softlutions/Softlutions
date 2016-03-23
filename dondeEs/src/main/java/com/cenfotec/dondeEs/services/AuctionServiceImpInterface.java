package com.cenfotec.dondeEs.services;

import java.util.List;

import com.cenfotec.dondeEs.ejb.AuctionService;
import com.cenfotec.dondeEs.pojo.AuctionServicePOJO;

public interface AuctionServiceImpInterface {

	Boolean saveAuctionService(AuctionService service);

	/**
	 * @author Ernesto Mendez A.
	 * @param auctionServiceId servicio auctualmente en subasta que se desea contratar
	 * @return si se contrato el servicio correctamente
	 */
	Boolean contract(int auctionServiceId);
	
	List<AuctionServicePOJO> getAllAuctionServicesByAuctionId(int AuctionId);
}