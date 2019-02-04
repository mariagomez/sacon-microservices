package com.oreilly.sacon.library.library.rating;

import org.springframework.data.repository.CrudRepository;

public interface RatingRepository extends CrudRepository<ItemRating, Long> {
}
