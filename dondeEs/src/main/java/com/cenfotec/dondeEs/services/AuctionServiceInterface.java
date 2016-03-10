package com.cenfotec.dondeEs.services;

import java.util.List;
import com.cenfotec.dondeEs.pojo.AuctionPOJO;
import com.cenfotec.dondeEs.pojo.AuctionServicePOJO;
import com.cenfotec.dondeEs.contracts.AuctionRequest;
import com.cenfotec.dondeEs.ejb.Auction;

public interface AuctionServiceInterface {

	Boolean saveAuction(Auction auction);
	List<AuctionPOJO> getAllAuctionByEvent(int event_id);
	
	/**
	 * @author Ernesto Mendez A.
	 * @param auction_id Id de la subasta a obtener
	 * @return Subasta con la informacion basica
	 * @version 1.0
	 */
	AuctionServicePOJO getAuctionService(int auction_id);

	/**
	 * @author Ernesto Mendez A.
	 * @param request Respuesta a la invitacion de la subasta
	 * @return resultado de la operacion
	 * @version 1.0
	 */
	boolean auctionInvitationAnswer(AuctionRequest request);
}
