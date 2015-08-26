package com.drivojoy.serviceImpl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.drivojoy.documents.BookingStatus;
import com.drivojoy.repositories.BookingStatusRepository;
import com.drivojoy.service.IBookingStatusService;
import com.drivojoy.shared.BookingSlotCount;

@Service
public class BookingStatusServiceImpl implements IBookingStatusService {

	@Autowired
	private BookingStatusRepository bookingRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	private final Logger logger = Logger.getLogger(BookingStatusServiceImpl.class);
	
	public int getBookingStatusForSlot(Date date, int slot){
		logger.debug("getBookingStatus() invoked");
		try{
			AggregationOperation matchDate = Aggregation.match(Criteria.where("bookingDate").is(date));
			AggregationOperation matchSlot = Aggregation.match(Criteria.where("bookingSlots").is(slot));
			AggregationOperation group = Aggregation.group("bookingDate", "bookingSlots").sum("currentRequests").as("total");
			Aggregation aggregation = Aggregation.newAggregation(matchDate, matchSlot, group);
			AggregationResults<BookingSlotCount> countResults = mongoTemplate.aggregate(aggregation, 
					BookingStatus.class, BookingSlotCount.class);
			logger.debug("Aggregation executed...");
			List<BookingSlotCount> result = countResults.getMappedResults();
			logger.debug("Results mapped...");
			logger.debug("Bookings currently made for "+date+" slot "+slot+" are "+result.get(0).getTotal());
			return result.get(0).getTotal();
		}catch(Exception ex){
			logger.error("Exception thrown while getting booking info");
			return -1;
		}
	}

	public String getBookingSlotId(Date date, int slot) {
		logger.debug("getBookingStatus() invoked");
		logger.debug("getting booking id for "+date+" slot "+slot);
		try{
			return bookingRepository.getBookingId(date, slot);
		}catch(Exception e){
			logger.error("Exception caught while getting booking id");
			return null;
		}
	}

	public int getBookingStatusById(String _id) {
		logger.debug("getBookingStatusById() invoked");
		logger.debug("getting booking status for _id "+_id);
		try{
			return bookingRepository.findOne(_id).getCurrentRequests();
		}catch(Exception ex){
			logger.debug("Exception thrown while getting booking status");
			return -1;
		}
	}

	public BookingStatus createBookingSlot(Date date, int slot, int currentRequests) {
		logger.debug("createBookingSlot() invoked");
		BookingStatus newBooking = new BookingStatus(date, slot, currentRequests);
		try{
			return bookingRepository.save(newBooking);
		}catch(Exception ex){
			logger.error("Exception thrown while saving new booking");
			return null;
		}
	}

	public void incrementCurrentRequest(String _id) {
		logger.debug("incrementCurrentRequest() invoked");
		try{
			BookingStatus status = bookingRepository.findOne(_id);
			status.setCurrentRequests(status.getCurrentRequests()+1);
			bookingRepository.save(status);
		}catch(Exception ex){
			logger.error("Exception thrown while incrementing current requests");
		}
	}
}
