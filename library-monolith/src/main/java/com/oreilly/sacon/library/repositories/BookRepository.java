package com.oreilly.sacon.library.repositories;

import com.oreilly.sacon.library.dao.Item;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Item, Long> {
}
