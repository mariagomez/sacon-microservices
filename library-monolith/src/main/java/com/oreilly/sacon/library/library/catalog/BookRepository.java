package com.oreilly.sacon.library.catalog;

import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Item, Long> {
}
