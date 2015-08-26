package com.drivojoy.repositories;

import java.util.Date;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.drivojoy.documents.BookingStatus;

@Repository
public interface BookingStatusRepository extends MongoRepository<BookingStatus, String>{
	
	@Query(value="{ 'date' : ?0, 'slot' : ?1 }", fields="{'_id' : 1}")
	public String getBookingId(Date date, int slot);
}
