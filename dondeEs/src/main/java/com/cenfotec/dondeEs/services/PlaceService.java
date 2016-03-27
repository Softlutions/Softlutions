package com.cenfotec.dondeEs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.ejb.Place;
import com.cenfotec.dondeEs.repositories.PlaceRepository;

@Service
public class PlaceService implements PlaceServiceInterface {
	@Autowired private PlaceRepository placeRepository;
	
	@Override
	public Place findById(int id){
		return placeRepository.findOne(id);
	}
	
	@Override
	public Place savePlace(Place _place) {
		Place place = placeRepository.save(_place);
		return place;
	}	
}
