package es.codemotion.madrid.library;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import es.codemotion.madrid.library.controllers.IndexController;
import es.codemotion.madrid.library.dao.Item;
import es.codemotion.madrid.library.repositories.BookRepository;
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
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@WebMvcTest({IndexController.class, CatalogController.class})
public class IndexPageTest {

    @Autowired
    private WebClient webClient;

    @MockBean
    private BookRepository bookRepository;

    @Test
    public void shouldShowCatalogWhenRequestingIndex() throws Exception {
        Item item = mock(Item.class);
        given(bookRepository.findAll()).willReturn(Arrays.asList(item));

        HtmlPage page = this.webClient.getPage("/");
        assertThat(page.getBody().getTextContent()).contains("Catalog");
    }

}