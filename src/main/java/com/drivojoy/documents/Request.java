package com.drivojoy.documents;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class Request {

	private String _id;
	private String name;
	private String serviceType;
	private String comments;
	private String bikeModel;
	private String location;
	private String addressDetails;
	private String email;
	private String mobile;
	private String date;
	private int slot;
	private double[] customerCoordinates;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBikeModel() {
		return bikeModel;
	}
	public void setBikeModel(String bikeModel) {
		this.bikeModel = bikeModel;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getAddressDetails() {
		return addressDetails;
	}
	public void setAddressDetails(String addressDetails) {
		this.addressDetails = addressDetails;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getSlot() {
		return slot;
	}
	public void setSlot(int slot) {
		this.slot = slot;
	}
	public double[] getCustomerCoordinates() {
		return customerCoordinates;
	}
	public void setCustomerCoordinates(double[] customerCoordinates) {
		this.customerCoordinates = customerCoordinates;
	}
	@Override
	public String toString(){
		try{
			return String.format("Request[name: '%s' ; "
					+ "serviceType: '%s' ; "
					+ "bike: '%s' ; "
					+ "location: '%s') ; "
					+ "date: '%s' "
					+ "slot: %s]", this.name, this.serviceType, this.bikeModel, 
					this.location, this.date, this.slot);
		}catch(Exception ex){
			return String.format("Request[name: '%s' ; "
					+ "serviceType: '%s' ; "
					+ "bike: '%s' ; "
					+ "location: (unknown) ; "
					+ "date: '%s' "
					+ "slot: %s]", this.name, this.serviceType, this.bikeModel, 
					this.date, this.slot);
			
		}
	}
}
