package es.codemotion.madrid.library.controllers;

import es.codemotion.madrid.library.dao.Item;
import es.codemotion.madrid.library.repositories.BookRepository;
import es.codemotion.madrid.library.models.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CatalogController.class)
public class CatalogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;
    private final String name = "Lorem Ipsum";
    private final String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas placerat odio felis, vel bibendum justo pulvinar nec. Nam et consectetur turpis, sed venenatis diam. Nunc consectetur ultrices nisl venenatis venenatis. Integer venenatis suscipit lorem quis varius. Aliquam quis erat erat. Nunc aliquet nulla in turpis imperdiet, eget condimentum tellus ornare. Pellentesque fringilla dictum massa, et dapibus purus elementum vitae. Aliquam erat volutpat. Donec libero ante, molestie porta odio ut, lobortis finibus urna. Aenean interdum massa elit, ut feugiat urna rhoncus eu. Morbi ac ex ut lorem cursus congue. Mauris dignissim libero et ullamcorper bibendum. Ut turpis metus, viverra et cursus eget, suscipit ut arcu. Morbi sit amet vehicula est. Quisque sodales sapien elit, in pharetra erat elementum ut. In hac habitasse platea dictumst.";
    private final int rating = 3;
    private final String imagePath = "http://bulma.io/images/placeholders/640x480.png";
    private final boolean available = true;
    private final String author = "Lorem Ipsum Dolor";

    @Test
    public void shouldReturnAListOfBooks() throws Exception {
        Item item = new Item(name, author, description, rating, available, imagePath);
        Book book = new Book(item.getId(), name, author, description, rating, imagePath, available);
        Iterable<Item> items = Arrays.asList(item);
        when(bookRepository.findAll()).thenReturn(items);

        MvcResult mvcResult = mockMvc.perform(get("/catalog"))
                .andExpect(view().name("catalog"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        List actualBooks = (List) modelAndView.getModel().get("books");

        assertThat(actualBooks.get(0), samePropertyValuesAs(book));
    }

    @Test
    public void shouldModifyTheAvailabilityOfTheBookFromAvailableToNot() throws Exception {
        Item item = new Item(name, author, description, rating, available, imagePath);
        when(bookRepository.findOne(anyLong())).thenReturn(item);
        mockMvc.perform(post("/catalog/borrow", item.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/catalog"));
    }
}