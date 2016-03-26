package com.cenfotec.dondeEs.services;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.cenfotec.dondeEs.pojo.EventImagePOJO;

public interface EventImageServiceInterface {

	/**
	 * @author Ernesto Mendez A.
	 * @param eventParticipantId id del participante del evento asociado
	 * @param file archivo de imagen a subir
	 * @return si la operacion fue efectiva o no
	 */
	Boolean saveImage(int eventParticipantId, MultipartFile file);
	
	/**
	 * @author Ernesto Mendez A.
	 * @param eventId id del evento del cual se desea objetener las imagenes asociadas
	 * @return Lista de imagenes del evento
	 */
	List<EventImagePOJO> getAllByEventId(int eventId);
	
	/**
	 * @author Ernesto Mendez A.
	 * @param imageId id de la imagen a eliminar
	 * @return si la operacion fue efectiva o no
	 */
	Boolean deleteImage(int imageId);
}
