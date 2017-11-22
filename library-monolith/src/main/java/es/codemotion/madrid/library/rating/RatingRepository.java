package es.codemotion.madrid.library.rating;

import org.springframework.data.repository.CrudRepository;

public interface RatingRepository extends CrudRepository<ItemRating, Long> {
}
