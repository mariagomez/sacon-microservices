package com.oreilly.sacon.library.rating;

import org.springframework.data.repository.CrudRepository;

public interface RatingRepository extends CrudRepository<ItemRating, Long> {
}
