package es.codemotion.madrid.library;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import es.codemotion.madrid.library.borrow.BorrowService;
import es.codemotion.madrid.library.catalog.CatalogService;
import es.codemotion.madrid.library.controllers.IndexController;
import es.codemotion.madrid.library.catalog.Item;
import es.codemotion.madrid.library.borrow.ItemAvailability;
import es.codemotion.madrid.library.rating.ItemRating;
import es.codemotion.madrid.library.rating.RatingService;
import es.codemotion.madrid.library.controllers.CatalogController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@WebMvcTest({IndexController.class, CatalogController.class})
public class IndexPageTest {

    @Autowired
    private WebClient webClient;

    @MockBean
    private CatalogService catalogService;
    @MockBean
    private BorrowService borrowService;
    @MockBean
    private RatingService ratingService;

    @Test
    public void shouldShowCatalogWhenRequestingIndex() throws Exception {
        Item book = mock(Item.class);
        given(catalogService.getAllBooks()).willReturn(Arrays.asList(book));
        ItemAvailability availability = mock(ItemAvailability.class);
        given(borrowService.getAvailability(anyLong())).willReturn(availability);
        ItemRating rating = mock(ItemRating.class);
        given(ratingService.getRating(anyLong())).willReturn(rating);

        HtmlPage page = this.webClient.getPage("/");
        assertThat(page.getBody().getTextContent()).contains("Catalog");
    }

}