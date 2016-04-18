package com.cenfotec.dondeEs.services;

import java.util.List;
import com.cenfotec.dondeEs.pojo.AuctionPOJO;
import com.cenfotec.dondeEs.pojo.AuctionServicePOJO;
import com.cenfotec.dondeEs.contracts.AuctionRequest;
import com.cenfotec.dondeEs.ejb.Auction;

public interface AuctionServiceInterface {

	Boolean saveAuction(Auction auction);
	
	/**
	 * @author Antoni Ramirez Montano
	 * @param auction objeto a actualizar
	 * @return si fue exitoso o no la actualizacion
	 */
	Boolean finishAuction(Auction auction);
	
	/***
	 * Obtiene todas las subastas de un evento.
	 * @author Enmanuel García González
	 * @version 1.0
	 */
	List<AuctionPOJO> getAllAuctionByEvent(int event_id);
	
	/**
	 * @Author Juan Carlos Sánchez G.
	 * @return response Respuesta del servidor de la petición que lista todas las subastas existentes.
	 * @version 1.0
	 */
	List<AuctionPOJO> getAllAuctions();

	Auction findById (int auctionId);
	
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

	AuctionPOJO getAuctionById(int auctionId);

	AuctionPOJO getAllServicesByAuction(int auctionId);

}
