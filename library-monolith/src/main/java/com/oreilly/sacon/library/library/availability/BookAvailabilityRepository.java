package com.oreilly.sacon.library.availability;

import org.springframework.data.repository.CrudRepository;

public interface BookAvailabilityRepository extends CrudRepository<Book, Long> {
}

