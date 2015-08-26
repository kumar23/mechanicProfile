package com.drivojoy.service;

import org.springframework.stereotype.Service;

@Service
public interface IGoogleApi {

	public double callGoogleDistanceAPI(String fromLocation, double toLatitude, double toLongitude);
}
