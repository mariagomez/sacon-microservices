package es.codemotion.madrid.library.borrow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BorrowServiceTest {

    @Autowired
    private BorrowService borrowService;
    @MockBean
    private ItemAvailabilityRepository itemAvailabilityRepository;
    private boolean firstItemAvailability = true;
    private ItemAvailability first = new ItemAvailability(firstItemAvailability);
    private boolean secondItemAvailability = true;
    private ItemAvailability second = new ItemAvailability(secondItemAvailability);

    @Before
    public void setUp() {
        initMocks(this);
        when(itemAvailabilityRepository.findOne(1L)).thenReturn(first);
        when(itemAvailabilityRepository.findOne(2L)).thenReturn(second);
    }

    @Test
    public void shouldReturnItemAvailability() {
        assertThat(borrowService.getAvailability(1L), samePropertyValuesAs(first));
        assertThat(borrowService.getAvailability(2L), samePropertyValuesAs(second));
    }

    @Test
    public void shouldChangeItemAvailability() {
        borrowService.changeAvailability(1L);
        verify(itemAvailabilityRepository, atLeastOnce()).save(itemWithAvailability(!firstItemAvailability));

        borrowService.changeAvailability(2L);
        verify(itemAvailabilityRepository, atLeastOnce()).save(itemWithAvailability(!secondItemAvailability));
    }

    private ItemAvailability itemWithAvailability(boolean expectedAvailability) {
        return argThat(new ArgumentMatcher<ItemAvailability>() {
            @Override
            public boolean matches(Object argument) {
                if (!(argument instanceof ItemAvailability)) {
                    return false;
                }
                return ((ItemAvailability) argument).isAvailable() == expectedAvailability;
            }
        });
    }

}