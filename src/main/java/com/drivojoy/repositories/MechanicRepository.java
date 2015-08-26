package com.drivojoy.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.drivojoy.documents.Mechanic;

@Repository
public interface MechanicRepository extends MongoRepository<Mechanic, String>{

}
