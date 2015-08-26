package com.drivojoy.documents;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Kumar
 *	This class represents a document that will be stored in the collection mechanic
 */
@Document(collection="mechanic")
public class Mechanic {
	
	@Id
	private String _id;
	private String name;
	private List<String> listOfExpertise=new ArrayList<String>();
	private List<String> listOfBrands=new ArrayList<String>();
	@GeoSpatialIndexed(type=GeoSpatialIndexType.GEO_2DSPHERE)
	private double[] location;
	@DBRef
	private List<BookingStatus> assignedSlots=new ArrayList<BookingStatus>();

	public Mechanic(){}
	
	public Mechanic(String name, List<String> expertise, List<String> brands, double[] location){
		this.name = name;
		this.listOfExpertise = expertise;
		this.listOfBrands = brands;
		this.location = location;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getListOfExpertise() {
		return listOfExpertise;
	}
	public void setListOfExpertise(List<String> listOfExpertise) {
		this.listOfExpertise = listOfExpertise;
	}
	public List<String> getListOfBrands() {
		return listOfBrands;
	}
	public void setListOfBrands(List<String> listOfBrands) {
		this.listOfBrands = listOfBrands;
	}

	public String get_id() {
		return _id;
	}


	public double[] getLocation() {
		return location;
	}

	public void setLocation(double[] location) {
		this.location = location;
	}

	public List<BookingStatus> getAssignedSlots() {
		return assignedSlots;
	}

	public void setAssignedSlots(List<BookingStatus> assignedSlots) {
		this.assignedSlots = assignedSlots;
	}

	@Override
	public String toString(){
		try{
			return String.format("Mechanic [_id: '%s' "
					+ "name: '%s'"
					+ "location: {%s, %s} ]", this._id, this.name, this.location[0], this.location[1]);
		}catch (Exception ex){
			return String.format("Mechanic [_id: '%s' "
					+ "name: '%s'"
					+ "location: unknown ]", this._id, this.name);
		}
		
	}
	
	

}
