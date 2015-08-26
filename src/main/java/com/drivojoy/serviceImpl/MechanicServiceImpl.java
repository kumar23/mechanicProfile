package com.drivojoy.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.drivojoy.documents.BookingStatus;
import com.drivojoy.documents.Mechanic;
import com.drivojoy.repositories.MechanicRepository;
import com.drivojoy.service.IBookingStatusService;
import com.drivojoy.service.IMechanicService;

/**
 * @author Kumar
 *	This class is the service layer for mechanic repository.
 *	Any business logic, server-side validations and other service logic will be implemented here.
 */
@Component
public class MechanicServiceImpl implements IMechanicService {

	@Autowired
	private MechanicRepository mechanicRepo;
	@Autowired
	private IBookingStatusService bookingService;
	/*@Autowired
	private IGoogleApi googleApis;*/

	@Autowired
	private MongoTemplate mongoTemplate;

	private final int MAX_BOOKINGS_PER_SLOT = 2;
	private final Logger logger = Logger.getLogger(MechanicServiceImpl.class);

	/* ********************EXPOSED METHODS********************** */

	public long getMechanicCount(){
		logger.debug("MechanicService invoked: getMechanicCount()");
		return mechanicRepo.count();
	}

	public boolean isBookingPossible(Date date, int slot){
		logger.debug("isBookingPossible() invoked");
		if(bookingService.getBookingStatusForSlot(date, slot) < MAX_BOOKINGS_PER_SLOT*getMechanicCount()){
			logger.info("Bookings available for selected slot!");
			return true;
		}
		logger.info("Bookings full for selected slot!");
		return false;
	}

	@Transactional
	public boolean addSampleDataForMechanics(){
		logger.debug("MechanicService invoked: addSampleDataForMechanics()");
		logger.debug("Checking if sample data already added...");
		if(getMechanicCount()>0){
			logger.debug("Sample data already added...");
			return false;
		}else{
			logger.debug("Creating sample data set...");
			List<Mechanic> mechList = new ArrayList<Mechanic>();
			ArrayList<String> listOfExpertise = new ArrayList<String>();
			ArrayList<String> listOfBrands = new ArrayList<String>();
			listOfBrands.add("Activa");
			listOfBrands.add("Pulsar");
			listOfExpertise.add("Breakdowns");
			listOfExpertise.add("Clutch");
			listOfExpertise.add("Free Service");
			/* Sholinganallur Location coordinates */
			double location[] = {12.900543, 80.22810900000002};
			mechList.add(new Mechanic("Mechanic 1", listOfExpertise, listOfBrands , location));

			listOfExpertise = new ArrayList<String>();
			listOfBrands = new ArrayList<String>();
			listOfBrands.add("Discover");
			listOfBrands.add("Karizma");
			listOfBrands.add("Pulsar");
			listOfExpertise.add("Oil Change");
			listOfExpertise.add("Regular Service");
			listOfExpertise.add("Free Service");
			/* Karappakkam Location coordinates */
			double location2[] = {12.913905, 80.22941300000002};
			mechList.add(new Mechanic("Mechanic 2", listOfExpertise, listOfBrands , location2));


			try{
				mechanicRepo.save(mechList);
			}catch(Exception ex){
				logger.error("Exception caught while adding sample data...");
				return false;
			}
		}
		return true;
	}


	@Transactional
	public String generateMechanicProfile(String customerLocation, double[] customerCoordinates, Date date, int slot,
			String serviceType, String bikeModel) {
		String bookingSlotId = bookingService.getBookingSlotId(date, slot);
		GeoResults<Mechanic> listOfMechanics = getMechanicListMatchingCriteria(customerLocation, customerCoordinates, bookingSlotId);
		logger.debug("Mechanics within a radius of 30KM from customer location : "+listOfMechanics.getContent().size());
		Mechanic mechanic = getNearestMechanic(listOfMechanics, date, slot);
		if(mechanic!=null){
			return mechanic.toString();
		}else{
			return "NO_MECH_AVAILABLE";
		}
		
	}


	/* ********************PRIVATE METHODS********************** */

	private GeoResults<Mechanic> getMechanicListMatchingCriteria(String customerLocation, 
			double[] customerCoordinates,
			String bookingSlotId
			/*String serviceType, 
			String bikeModel */){
		//Query query = new Query();
		//query.addCriteria(Criteria.where("listOfExpertise").in(serviceType));
		//query.addCriteria(Criteria.where("listOfBrands").in(bikeModel));
		//query.addCriteria(Criteria.where("assignedSlots").in(bookingSlotId));
		NearQuery nearQuery = NearQuery.near(new Point(customerCoordinates[0], customerCoordinates[1]))
				.maxDistance(new Distance(30, Metrics.KILOMETERS));
		try{
			GeoResults<Mechanic> nearByMechanics = mongoTemplate.geoNear(nearQuery, Mechanic.class);
			return nearByMechanics;
		}catch(Exception ex){
			logger.debug("Exception thrown while executing geo-near query..");
			return null;
		}
	}

	private Mechanic getNearestMechanic(GeoResults<Mechanic> listOfMechanics, Date date, int slot){
		Iterator<GeoResult<Mechanic>> ir = listOfMechanics.iterator();
		while(ir.hasNext()){
			Mechanic mechanic = ir.next().getContent();
			if(isMechanicAvailable(mechanic, date, slot)){
				return mechanic;
			}
			/*temp = googleApis.callGoogleDistanceAPI(customerLocation, mechanic.getLocation()[0], mechanic.getLocation()[1]);
			logger.debug("Distance of "+mechanic.getName()+" at ("+mechanic.getLocation()[0]
					+", "+mechanic.getLocation()[1]+") from "+customerLocation+" is: "+temp+" Kms");
			if(temp<least){
				least = temp;
				result = mechanic;
			}*/
		}
		return null;
	}
	
	private boolean isMechanicAvailable(Mechanic mechanic, Date date, int slot){
		logger.debug("isMechanicAvailable() invoked ");
		List<BookingStatus> currentlyAssignedSlots = mechanic.getAssignedSlots();
		Iterator<BookingStatus> ir = currentlyAssignedSlots.iterator();
		//boolean slotFound = false;
		while(ir.hasNext()){
			BookingStatus bookingSlot = ir.next();
			if(bookingSlot.getBookingDate().equals(date) && bookingSlot.getBookingSlots() == slot){
				//slotFound=true;
				logger.debug("Slot found for this mechanic, checking if bookings are maxed out");
				if(bookingSlot.getCurrentRequests() < MAX_BOOKINGS_PER_SLOT){
					logger.debug("Mechanic has bandwidth to process the request, incrementing request counter for mechanic");
					bookingService.incrementCurrentRequest(bookingSlot.get_id());
					return true;
				}
				return false;
			}
		}
		logger.debug("Slot not found for this mechanic, add new booking slot for mechanic");
		mechanic.getAssignedSlots().add(bookingService.createBookingSlot(date, slot, 1));
		try{
			mechanicRepo.save(mechanic);
		}catch(Exception ex){
			logger.debug("Exception thrown while updating slot for mechanic");
		}
		return true;
	}
}
