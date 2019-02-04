package com.oreilly.sacon.library.rating;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RatingServiceTest {

    @Autowired
    private RatingService ratingService;

    @MockBean
    private RatingRepository ratingRepository;
    private int firstItemRating = 1;
    private ItemRating first = new ItemRating(firstItemRating);
    private int secondItemRating = 3;
    private ItemRating second = new ItemRating(secondItemRating);

    @Before
    public void setUp() {
        initMocks(this);
        when(ratingRepository.findOne(1L)).thenReturn(first);
        when(ratingRepository.findOne(2L)).thenReturn(second);
    }

    @Test
    public void shouldReturnItemRating() {
        assertThat(ratingService.getRating(1L), samePropertyValuesAs(first));
        assertThat(ratingService.getRating(2L), samePropertyValuesAs(second));
    }
}
