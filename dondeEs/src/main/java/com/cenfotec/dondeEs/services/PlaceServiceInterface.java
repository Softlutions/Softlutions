package com.cenfotec.dondeEs.services;

import com.cenfotec.dondeEs.ejb.Place;

public interface PlaceServiceInterface {
	Place savePlace(Place e);
	
	Place findById(int id);
}
