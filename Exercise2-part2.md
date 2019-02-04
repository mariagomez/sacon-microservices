# SACON NY 2019 Workshop

## Exercise 2 Part 2: step-by-step solution

### Availability domain
- Create a new method called `borrowBook` in `CatalogController` and move the logic from the method `borrow`

From here:
````
@RequestMapping(value = "/catalog/borrow", params={"bookId"})
public String borrow(final HttpServletRequest request) {
    String parameter = request.getParameter("bookId");
    Item book = bookRepository.findOne(Long.valueOf(parameter));
    book.setAvailable(false);
    bookRepository.save(book);
    return "redirect:/catalog";
}
````
To here:
````
@RequestMapping(value = "/catalog/borrow", params={"bookId"})
public String borrow(final HttpServletRequest request) {
    String parameter = request.getParameter("bookId");
    borrowBook(Long.valueOf(parameter));
    return "redirect:/catalog";
}

private void borrowBook(Long bookId) {
    Item book = bookRepository.findOne(bookId);
    book.setAvailable(false);
    bookRepository.save(book);
}
````
- Run all the tests to ensure you're not breaking code (`$ ./gradlew test`).

- Create a new package `com.oreilly.sacon.library.availability` and a class called `Availability` inside and copy the logic for borrowing books.

````
package com.oreilly.sacon.library.availability;

import com.oreilly.sacon.library.dao.Item;
import com.oreilly.sacon.library.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Availability {

    @Autowired
    private BookRepository bookRepository;

    public void borrow(Long bookId) {
        Item book = bookRepository.findOne(bookId);
        book.setAvailable(false);
        bookRepository.save(book);
    }

}
````

- Create tests for the class

````
package com.oreilly.sacon.library.availability;

import com.oreilly.sacon.library.dao.Item;
import com.oreilly.sacon.library.repositories.BookRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AvailabilityTest {

    @Autowired
    private Availability availability;

    @MockBean
    private BookRepository bookRepository;
    private Item bookAvailable = new Item("First book", "Author", "Description", 1, true, "path");
    private Item bookNotAvailable = new Item("Second book", "Author", "Description", 1, false, "path");

    @Before
    public void setUp() {
        initMocks(this);
        when(bookRepository.findOne(1L)).thenReturn(bookAvailable);
        when(bookRepository.findOne(2L)).thenReturn(bookNotAvailable);
    }

    @Test
    public void shouldChangeBookAvailability() {
        availability.borrow(1L);
        verify(bookRepository, atLeastOnce()).save(assertBookIsNotAvailable());

        availability.borrow(2L);
        verify(bookRepository, atLeastOnce()).save(assertBookIsNotAvailable());
    }

    private Item assertBookIsNotAvailable() {
        return argThat(new ArgumentMatcher<Item>() {
            @Override
            public boolean matches(Object argument) {
                if (!(argument instanceof Item)) {
                    return false;
                }
                return !((Item) argument).isAvailable();
            }
        });
    }
}
````

- Run all the tests in the project ( `$ ./gradlew clean test`) to ensure the refactoring is going well.

- Refactor `CatalogController` to use the new `Availability` class.

  - Inject Availability:

   ````
   @Autowired
   private Availability availability;
   ````
 - Refactor logic in the method `borrow` to use the new class
   ````
   @RequestMapping(value = "/catalog/borrow", params={"bookId"})
   public String borrow(final HttpServletRequest request) {
      String parameter = request.getParameter("bookId");
      availability.borrow(Long.valueOf(parameter));
      return "redirect:/catalog";
   }
   ````
 - Run the tests in `CatalogControllerTest`. One of them is now failing as the bean `Availability` is not being defined. To fix them, inject the bean as a mock and refactor the failing test:

   ````
   @MockBean
   private Catalog catalog;
   ````
   ````
   @Test
   public void shouldModifyTheAvailabilityOfTheBookFromAvailableToNot() throws Exception {
        Long aId = 1l;
        mockMvc.perform(post("/catalog/borrow").param("bookId", aId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/catalog"));

        verify(availability, atLeastOnce()).borrow(aId);
    }
    ````

- Run all the tests in the project ( `$ ./gradlew clean test`). `IndexPageTest` fails again. Like in the previous exercise, fix it by injecting the missing bean

````
@MockBean
private Availability availability;
````

- Last, let's remove some unused code:

  - CatalogController: remove `borrowBook` method and unused `bookRepository` bean.

  - CatalogControllerTest: remove unused `bookRepository` bean.

  - IndexPageTest: remove unused `bookRepository` bean.

- If all the tests are passing, commit your changes and push them to your repo:
````
$ git add .
$ git commit -m "Availability domain created"
````

### Database refactoring (part I)
We would like our domains or bounded contexts to have independent data stores. For that we need to refactor Availability so it has its own table and later refactor CatalogController so the information shown to the user comes from the Availability.

#### Create Book table
- In the folder `src/main/resources/db/migration`, create a [flyway](www.flyway.com) migration script named `V3__Create_Book_Table.sql` with the content:

````
CREATE TABLE BOOK (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY,
	available BIT DEFAULT FALSE
);

INSERT INTO BOOK(id, available)
SELECT id, available FROM ITEM;
````

This script is already copying the availability information from the Item table.

#### Book aggregate

- In the package `com.oreilly.sacon.library.availability` create a new class called `Book`:

````
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean available;
}
````

- Create a repository interface (`BookAvailabilityRepository`) in the same package to access this Entity

````
package com.oreilly.sacon.library.availability;

import org.springframework.data.repository.CrudRepository;

public interface BookAvailabilityRepository extends CrudRepository<Book, Long> {
}
````

#### Bringing it all together
- Add borrow & inStock functionality to `Book`

````
package com.oreilly.sacon.library.availability;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean available;

    protected Book() {
    }

    public Book(boolean available) {
        this.available = available;
    }

    public boolean borrow() {
        if (available) {
            available = false;
            return true;
        }
        return false;
    }

    public boolean inStock() {
        return available;
    }
}
````

- Modify `AvailabilityTest` to use the new repository and aggregate:

````
package com.oreilly.sacon.library.availability;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Before
    public void setUp() {
        initMocks(this);

        when(bookAvailabilityRepository.findOne(1L)).thenReturn(new Book(true));
        when(bookAvailabilityRepository.findOne(2L)).thenReturn(new Book(false));
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
````

- Run the tests (`$ ./gradlew test`). It should fail. To fix it, refactor `Availability` to use the new repository and aggregate.

````
package com.oreilly.sacon.library.availability;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Availability {

    @Autowired
    private BookAvailabilityRepository bookAvailabilityRepository;

    public void borrow(Long bookId) {
        Book book = bookAvailabilityRepository.findOne(bookId);
        book.borrow();
        bookAvailabilityRepository.save(book);
    }

}
````

- If tests are passing, commit them.
````
$ git add .
$ git commit -m "Use a new aggregate for availability"
````

### Add inStock functionality to Availability

- Create a new `inStock` method in `Availability`

````
public boolean inStock(Long bookId) {
  Book book = bookAvailabilityRepository.findOne(bookId);
  return book.inStock();
}
````

- Create the tests for it in `AvailabilityTest`

````
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
````

- Run all the tests (`$ ./gradlew test`).
- If tests are passing, commit them.
````
$ git add .
$ git commit -m "Add inStock functionality to Availability"
````

#### Refactor Catalog to use Availability.
- Inject `Availability` in `Catalog` and refactor the method `getAllBooks`

````
@Service
public class Catalog {

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private Availability availability;

  public List<Book> getAllBooks() {
      Iterable<Item> items = bookRepository.findAll();
      List<Book> books = StreamSupport.stream(items.spliterator(), false)
              .map(item -> new Book(item.getId(),
                      item.getName(),
                      item.getAuthor(),
                      item.getDescription(),
                      item.getRating(),
                      item.getImagePath(),
                      availability.inStock(item.getId())))
              .sorted(Comparator.comparing(item -> item.getName()))
              .collect(Collectors.toList());
      return books;
  }
}
````

- Modifiy `CatalogTest` to add the new dependency

````
@RunWith(SpringRunner.class)
@SpringBootTest
public class CatalogTest {

    @Autowired
    private Catalog catalog;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private Availability availability;

    private Item first = new Item(1l, "First book", "Author", "Description", 1, true, "path");
    private Item second = new Item(2l,"Second book", "Author", "Description", 1, false, "path");
    private List<Item> completeCollection = new ArrayList() {{
        add(first);
        add(second);
    }};

    @Before
    public void setUp() {
        initMocks(this);
        when(bookRepository.findAll()).thenReturn(completeCollection);
        when(availability.inStock(1l)).thenReturn(true);
        when(availability.inStock(2l)).thenReturn(false);
    }

    @Test
    public void shouldReturnAllBooksInBookRepository() {
        List<Book> completeCatalog = catalog.getAllBooks();
        assertThat(completeCatalog, is(notNullValue()));
        assertThat(completeCatalog.get(0), samePropertyValuesAs(new Book(first.getId(), first.getName(), first.getAuthor(), first.getDescription(), first.getRating(), first.getImagePath(), true)));
        assertThat(completeCatalog.get(1), samePropertyValuesAs(new Book(second.getId(), second.getName(), second.getAuthor(), second.getDescription(), second.getRating(), second.getImagePath(), false)));
    }
}
````

- Run all tests (`$ ./gradlew test`) and commit the changes.
````
$ git add .
$ git commit -m "Refactor Catalog to use Availability"
````

### Database refactoring (part II)
Right now, `Item` still have the property `isAvailable` but not in use.

- First, let's remove it from the object `Item`:
  - Remove `setAvailable` & `isAvailable` methods (they should be greyed out).
  - Remove `available` field (it should also be grey).
  - If the editor hasn't done it, remove `available` from the constructors.
  - Run all the tests (`$ ./gradlew test`).

- Finally, let's remove the column from the table:

  - In the folder `src/main/resources/db/migration`, create a [flyway](www.flyway.com) migration script named `V4__Delete_Availability_from_Item.sql` with the content:

````
ALTER TABLE ITEM  DROP COLUMN available;
````
  - Run all the tests (`$ ./gradlew test`).
  - Run the application to check the borrow functionality still works.
  ````
  $ ./gradlew bootRun
  ````
  Go to http://localhost:8080/catalog

- If all is good, commit the changes.
````
$ git add .
$ git commit -m "Remove availability info from Item"
````

### [Optional] Run tests as part of the Continuous Integration pipeline with TravisCI & check code coverage with Codecov
- Push the changes to the remote repo and wait for Travis to run the jobs and Codecov to create the report
````
$ git push
````
