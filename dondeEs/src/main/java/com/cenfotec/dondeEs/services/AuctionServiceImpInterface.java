package com.cenfotec.dondeEs.services;

import com.cenfotec.dondeEs.ejb.AuctionService;

public interface AuctionServiceImpInterface {

	Boolean saveAuctionService(AuctionService service);

	/**
	 * @author Ernesto Mendez A.
	 * @param auctionServiceId servicio auctualmente en subasta que se desea contratar
	 * @return si se contrato el servicio correctamente
	 */
	Boolean contract(int auctionServiceId);
}