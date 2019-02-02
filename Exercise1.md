# SACON NY 2019 Workshop

## Exercise 1 step-by-step solution

### Add tests to IndexController
- Open the project in the editor
- Go to `src/test/java`
- Create a new package called `com.oreilly.sacon.library.controllers`
- Create a class under that package called `IndexControllerTest`
- Add this new test:

````
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = IndexController.class)
public class IndexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldRedirectToCatalog() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/catalog"));
    }
}
````

- Run all the test in the command line: `$ ./gradlew test`
- [Optional] Commit the changes in the repository

### Add tests to CatalogController
- Create a class under the package `com.oreilly.sacon.library.controllers` called `CatalogControllerTest`
- Add these new test:

````
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
````
- Run all the test in the command line: `$ ./gradlew test`
- [Optional] Commit the changes in the repository

### Run tests as part of the Continuous Integration pipeline with TravisCI & check code coverage with Codecov
- Go to https://travis-ci.org, allow Travis to access your public Github repos.
- Go to _Settings_, find the forked repository and activate it so Travis can track it
- Go to https://codecov.io and allow Codecov to access your public Github repos
- Added the forked repository

- Push the changes to the remote repo and wait for Travis to run the jobs and Codecov to create the report
