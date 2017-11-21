package es.codemotion.madrid.library.repositories;

import es.codemotion.madrid.library.dao.Item;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Item, Long> {
}
