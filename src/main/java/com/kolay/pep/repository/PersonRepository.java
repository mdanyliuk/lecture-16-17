package com.kolay.pep.repository;

import com.kolay.pep.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonRepository extends MongoRepository<Person, String>, PersonRepositoryCustom {
}
