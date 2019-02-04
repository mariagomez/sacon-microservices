# SACON NY 2019 Workshop

## Exercise 2 step-by-step solution

### Catalog domain
- Create a new method called `getAllBooks` in `CatalogController` and move the logic from the method `catalog`

From here:
````
@RequestMapping("/catalog")
public String catalog(ModelMap model) {
    Iterable<Item> items = bookRepository.findAll();
    List<Book> books = StreamSupport.stream(items.spliterator(), false)
            .map(item -> new Book(item.getId(), item.getName(), item.getAuthor(), item.getDescription(), item.getRating(),
                    item.getImagePath(), item.isAvailable()))
            .sorted(Comparator.comparingLong(item -> item.getId()))
            .collect(Collectors.toList());
    model.addAttribute("books", books);
    return "catalog";
}
````
to here:
````
@RequestMapping("/catalog")
public String catalog(ModelMap model) {
    List<Book> books = getAllBooks();
    model.addAttribute("books", books);
    return "catalog";
}

private List<Book> getAllBooks() {
    Iterable<Item> items = bookRepository.findAll();
    return StreamSupport.stream(items.spliterator(), false)
            .map(item -> new Book(item.getId(), item.getName(), item.getAuthor(), item.getDescription(), item.getRating(),
                    item.getImagePath(), item.isAvailable()))
            .sorted(Comparator.comparingLong(item -> item.getId()))
            .collect(Collectors.toList());
}
````

- Run all the tests to ensure you're not breaking code.

- Create a new package `com.oreilly.sacon.library.catalog` and class called `Catalog` inside it to copy the logic of retrieving the books.

````
package com.oreilly.sacon.library.catalog;

import com.oreilly.sacon.library.dao.Item;
import com.oreilly.sacon.library.models.Book;
import com.oreilly.sacon.library.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class Catalog {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        Iterable<Item> items = bookRepository.findAll();
        List<Book> books = StreamSupport.stream(items.spliterator(), false)
                .map(item -> new Book(item.getId(), item.getName(), item.getAuthor(), item.getDescription(), item.getRating(),
                        item.getImagePath(), item.isAvailable()))
                .sorted(Comparator.comparing(item -> item.getName()))
                .collect(Collectors.toList());
        return books;
    }
}
````

- Create tests for this class

````
package com.oreilly.sacon.library.catalog;

import com.oreilly.sacon.library.dao.Item;
import com.oreilly.sacon.library.models.Book;
import com.oreilly.sacon.library.repositories.BookRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CatalogTest {

    @Autowired
    private Catalog catalog;
    @MockBean
    private BookRepository bookRepository;
    private Item first = new Item("First book", "Author", "Description", 1, true, "path");
    private Item second = new Item("Second book", "Author", "Description", 1, false, "path");
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
    public void shouldReturnAllBooksInBookRepository() {
      List<Book> completeCatalog = catalog.getAllBooks();
      assertThat(completeCatalog, is(notNullValue()));
      assertThat(completeCatalog.get(0), samePropertyValuesAs(new Book(first.getId(), first.getName(), first.getAuthor(), first.getDescription(), first.getRating(), first.getImagePath(), first.isAvailable())));
      assertThat(completeCatalog.get(1), samePropertyValuesAs(new Book(second.getId(), second.getName(), second.getAuthor(), second.getDescription(), second.getRating(), second.getImagePath(), second.isAvailable())));
    }
}
````

- Run all the tests in the project ( `$ ./gradlew clean test`) to ensure the refactoring is going well

- Refactor `CatalogController` to use the new `Catalog` class.

 - Inject Catalog:

    ````
    @Autowired
    private Catalog catalog;
    ````
  - Refactor logic in the method `catalog` to use the new class
    ````
    List<Item> books = catalog.getAllBooks();
    ````

  - Run the tests in `CatalogControllerTest`. One of them is now failing as the bean `Catalog` is not being defined. To fix them, inject the bean as a mock and refactor the failing test:

    ````
    @MockBean
    private Catalog catalog;
    ````
    ````
    @Test
    public void shouldReturnAListOfBooks() throws Exception {
        Long id = 1l;
        Book book = new Book(id, name, author, description, rating, imagePath, available);
        List<Book> books = new ArrayList() {{add(book);}};
        when(catalog.getAllBooks()).thenReturn(books);

        MvcResult mvcResult = mockMvc.perform(get("/catalog"))
                .andExpect(view().name("catalog"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        List actualBooks = (List) modelAndView.getModel().get("books");

        assertThat(actualBooks.get(0), samePropertyValuesAs(book));
    }
    ````
  - Last, remove the unused method `getAllBooks` from `CatalogController`


- Run all the tests in the project ( `$ ./gradlew clean test`). `IndexPageTest` should fail. Fix the injected beans as we did in the previous steps:

````
@MockBean
private BookRepository bookRepository;

@MockBean
private Catalog catalog;

@Test
public void shouldShowCatalogWhenRequestingIndex() throws Exception {
    Book book = mock(Book.class);
    given(catalog.getAllBooks()).willReturn(Arrays.asList(book));

    HtmlPage page = this.webClient.getPage("/");
    assertThat(page.getBody().getTextContent()).contains("Catalog");
}
````

- If all the tests are passing, commit your changes and push them to your repo:
````
$ git add .
$ git commit -m "Catalog domain created"
````

### [Optional] Run tests as part of the Continuous Integration pipeline with TravisCI & check code coverage with Codecov
- Go to https://travis-ci.org, allow Travis to access your public Github repos.
- Go to _Settings_, find the forked repository and activate it so Travis can track it
- Go to https://codecov.io and allow Codecov to access your public Github repos
- Added your forked repository

- Push the changes to the remote repo and wait for Travis to run the jobs and Codecov to create the report
````
$ git push
````
