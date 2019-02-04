package com.oreilly.sacon.library;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.oreilly.sacon.library.availability.Availability;
import com.oreilly.sacon.library.catalog.CatalogService;
import com.oreilly.sacon.library.catalog.Item;
import com.oreilly.sacon.library.controllers.CatalogController;
import com.oreilly.sacon.library.controllers.IndexController;
import com.oreilly.sacon.library.rating.ItemRating;
import com.oreilly.sacon.library.rating.RatingService;
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
    private Availability availability;
    @MockBean
    private RatingService ratingService;

    @Test
    public void shouldShowCatalogWhenRequestingIndex() throws Exception {
        Item book = mock(Item.class);
        given(catalogService.getAllBooks()).willReturn(Arrays.asList(book));
        given(this.availability.inStock(anyLong())).willReturn(true);
        ItemRating rating = mock(ItemRating.class);
        given(ratingService.getRating(anyLong())).willReturn(rating);

        HtmlPage page = this.webClient.getPage("/");
        assertThat(page.getBody().getTextContent()).contains("Catalog");
    }

}