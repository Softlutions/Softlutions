package com.cenfotec.dondeEs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.dondeEs.repositories.PlaceRepository;

@Service
public class PlaceService implements PlaceServiceInterface {
@Autowired private PlaceRepository placeRepository;
}
