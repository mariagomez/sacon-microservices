package com.oreilly.sacon.library.catalog;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CatalogServiceTest {

    @Autowired
    private CatalogService catalogService;
    @MockBean
    private BookRepository bookRepository;
    private Item first = new Item("First book", "Author", "Description", "path");
    private Item second = new Item("Second book", "Author", "Description", "path");
    private List<Item> completeCollection = new ArrayList() {{
        add(first);
        add(second);
    }};

    @Before
    public void setUp() {
        initMocks(this);
        when(bookRepository.findAll()).thenReturn(completeCollection);
    }

    @Test
    public void shouldReturnAllBooksAvailableInBookRepository() {
        List<Item> completeCatalog = catalogService.getAllBooks();
        assertThat(completeCatalog, is(notNullValue()));
        assertThat(completeCatalog.get(0), samePropertyValuesAs(new Item(first.getName(), first.getAuthor(), first.getDescription(), first.getImagePath())));
        assertThat(completeCatalog.get(1), samePropertyValuesAs(new Item(second.getName(), second.getAuthor(), second.getDescription(), second.getImagePath())));
    }
}
