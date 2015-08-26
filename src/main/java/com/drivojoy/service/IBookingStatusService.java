package com.drivojoy.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.drivojoy.documents.BookingStatus;

@Service
public interface IBookingStatusService
{
	public int getBookingStatusForSlot(Date date, int slot);
	
	public String getBookingSlotId(Date date, int slot);
	
	public int getBookingStatusById(String _id);
	
	public BookingStatus createBookingSlot(Date date, int slot, int currentRequests);
	
	public void incrementCurrentRequest(String _id);
	//public boolean isBookingPossibleForMechanic(String _id, int MAX_BOOKINGS_PER_SLOT);
}
