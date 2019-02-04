package com.oreilly.sacon.library.availability;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AvailabilityClientTest {

    @Autowired
    private AvailabilityClient availabilityClient;

    @MockBean
    private RestTemplate restTemplate;

    @Value("${availability.url}")
    private String availabilityUrl;


    @Before
    public void setUp() {
        initMocks(this);
        when(restTemplate.getForObject(availabilityUrl + "1", Book.class)).thenReturn(new Book(true));
        when(restTemplate.getForObject(availabilityUrl + "2", Book.class)).thenReturn(new Book(false));
    }

    @Test
    public void shouldReturnItemAvailability() {
        assertThat(availabilityClient.inStock(1L), is(true));
        assertThat(availabilityClient.inStock(2L), is(false));
    }

    @Test
    public void shouldChangeBookAvailability() {
        availabilityClient.borrow(1L);
        verify(restTemplate, atMost(1)).put(availabilityUrl + "borrow", "1");
    }

}