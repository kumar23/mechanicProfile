package com.drivojoy.service;

import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public interface IMechanicService {

	public long getMechanicCount();
	public String generateMechanicProfile(String customerLocation,
										double[] customerCoordinates,
										Date date,
										int slot,
										String serviceType,
										String bikeModel);
	
	public boolean isBookingPossible(Date date, int slot);
	public boolean addSampleDataForMechanics();
}
