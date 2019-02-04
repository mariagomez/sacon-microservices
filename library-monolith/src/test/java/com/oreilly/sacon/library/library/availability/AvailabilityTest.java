package com.oreilly.sacon.library.library.availability;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AvailabilityTest {

    @Autowired
    private Availability availability;

    @MockBean
    private BookAvailabilityRepository bookAvailabilityRepository;

    private final Book bookAvailable = new Book(true);
    private final Book bookNotAvailable = new Book(false);

    @Before
    public void setUp() {
        initMocks(this);

        when(bookAvailabilityRepository.findOne(1L)).thenReturn(bookAvailable);
        when(bookAvailabilityRepository.findOne(2L)).thenReturn(bookNotAvailable);
    }

    @Test
    public void shouldReturnItemAvailability() {
        assertThat(availability.inStock(1L), is(bookAvailable.inStock()));
        assertThat(availability.inStock(2L), is(bookNotAvailable.inStock()));
    }

    @Test
    public void shouldChangeBookAvailability() {
        availability.borrow(1L);
        verify(bookAvailabilityRepository, atLeastOnce()).save(assertBookIsNotAvailable());

        availability.borrow(2L);
        verify(bookAvailabilityRepository, atLeastOnce()).save(assertBookIsNotAvailable());
    }

    private Book assertBookIsNotAvailable() {
        return argThat(new ArgumentMatcher<Book>() {
            @Override
            public boolean matches(Object argument) {
                if (!(argument instanceof Book)) {
                    return false;
                }
                return !((Book) argument).inStock();
            }
        });
    }
}