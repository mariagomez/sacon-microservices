package com.oreilly.sacon.library.rating;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RatingServiceClientTest {

    @Autowired
    private RatingServiceClient ratingServiceClient;

    @MockBean
    private RestTemplate restTemplate;

    private int firstItemRating = 1;
    private ItemRating first = new ItemRating(firstItemRating);

    @Before
    public void setUp() {
        initMocks(this);
        when(restTemplate.getForObject(anyString(), any())).thenReturn(first);
    }

    @Test
    public void shouldReturnItemRating() {
        assertThat(ratingServiceClient.getRating(1L), samePropertyValuesAs(first));
    }
}
