package com.drivojoy.documents;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Document(collection="bookingStatus")
public class BookingStatus {

	@Id
	private String _id;
	@Indexed(unique=false, direction=IndexDirection.DESCENDING)
	@DateTimeFormat(iso = ISO.DATE)
	private Date bookingDate;
	private int bookingSlots;
	private int currentRequests;
	
	public BookingStatus() {}
	
	public BookingStatus(Date bookingDate, int bookingSlots, int currentRequests){
		this.bookingDate = bookingDate;
		this.bookingSlots = bookingSlots;
		this.currentRequests = currentRequests;
	}
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public Date getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}
	public int getBookingSlots() {
		return bookingSlots;
	}
	public void setBookingSlots(int bookingSlots) {
		this.bookingSlots = bookingSlots;
	}
	public int getCurrentRequests() {
		return currentRequests;
	}
	public void setCurrentRequests(int currentRequests) {
		this.currentRequests = currentRequests;
	}
	
	@Override
	public String toString(){
		return String.format("Booking Status for %s Slot %s : %s", 
				this.bookingDate, this.bookingSlots, this.currentRequests);
	}
}
