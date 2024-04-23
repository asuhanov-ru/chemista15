package com.chemista15.web.rest;

import static com.chemista15.domain.BookAsserts.*;
import static com.chemista15.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chemista15.IntegrationTest;
import com.chemista15.domain.Author;
import com.chemista15.domain.Book;
import com.chemista15.domain.Media;
import com.chemista15.repository.BookRepository;
import com.chemista15.service.BookService;
import com.chemista15.service.dto.BookDTO;
import com.chemista15.service.mapper.BookMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BookResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BookResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_MEDIA_START_PAGE = 1;
    private static final Integer UPDATED_MEDIA_START_PAGE = 2;
    private static final Integer SMALLER_MEDIA_START_PAGE = 1 - 1;

    private static final Integer DEFAULT_MEDIA_END_PAGE = 1;
    private static final Integer UPDATED_MEDIA_END_PAGE = 2;
    private static final Integer SMALLER_MEDIA_END_PAGE = 1 - 1;

    private static final String ENTITY_API_URL = "/api/books";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BookRepository bookRepository;

    @Mock
    private BookRepository bookRepositoryMock;

    @Autowired
    private BookMapper bookMapper;

    @Mock
    private BookService bookServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookMockMvc;

    private Book book;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Book createEntity(EntityManager em) {
        Book book = new Book()
            .uuid(DEFAULT_UUID)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .mediaStartPage(DEFAULT_MEDIA_START_PAGE)
            .mediaEndPage(DEFAULT_MEDIA_END_PAGE);
        return book;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Book createUpdatedEntity(EntityManager em) {
        Book book = new Book()
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .mediaStartPage(UPDATED_MEDIA_START_PAGE)
            .mediaEndPage(UPDATED_MEDIA_END_PAGE);
        return book;
    }

    @BeforeEach
    public void initTest() {
        book = createEntity(em);
    }

    @Test
    @Transactional
    void createBook() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Book
        BookDTO bookDTO = bookMapper.toDto(book);
        var returnedBookDTO = om.readValue(
            restBookMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BookDTO.class
        );

        // Validate the Book in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBook = bookMapper.toEntity(returnedBookDTO);
        assertBookUpdatableFieldsEquals(returnedBook, getPersistedBook(returnedBook));
    }

    @Test
    @Transactional
    void createBookWithExistingId() throws Exception {
        // Create the Book with an existing ID
        book.setId(1L);
        BookDTO bookDTO = bookMapper.toDto(book);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Book in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBooks() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList
        restBookMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(book.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].mediaStartPage").value(hasItem(DEFAULT_MEDIA_START_PAGE)))
            .andExpect(jsonPath("$.[*].mediaEndPage").value(hasItem(DEFAULT_MEDIA_END_PAGE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBooksWithEagerRelationshipsIsEnabled() throws Exception {
        when(bookServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bookServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBooksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bookServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBookMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(bookRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBook() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get the book
        restBookMockMvc
            .perform(get(ENTITY_API_URL_ID, book.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(book.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.mediaStartPage").value(DEFAULT_MEDIA_START_PAGE))
            .andExpect(jsonPath("$.mediaEndPage").value(DEFAULT_MEDIA_END_PAGE));
    }

    @Test
    @Transactional
    void getBooksByIdFiltering() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        Long id = book.getId();

        defaultBookFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultBookFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultBookFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBooksByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where uuid equals to
        defaultBookFiltering("uuid.equals=" + DEFAULT_UUID, "uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllBooksByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where uuid in
        defaultBookFiltering("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID, "uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllBooksByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where uuid is not null
        defaultBookFiltering("uuid.specified=true", "uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where name equals to
        defaultBookFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBooksByNameIsInShouldWork() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where name in
        defaultBookFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBooksByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where name is not null
        defaultBookFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByNameContainsSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where name contains
        defaultBookFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBooksByNameNotContainsSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where name does not contain
        defaultBookFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllBooksByMediaStartPageIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where mediaStartPage equals to
        defaultBookFiltering("mediaStartPage.equals=" + DEFAULT_MEDIA_START_PAGE, "mediaStartPage.equals=" + UPDATED_MEDIA_START_PAGE);
    }

    @Test
    @Transactional
    void getAllBooksByMediaStartPageIsInShouldWork() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where mediaStartPage in
        defaultBookFiltering(
            "mediaStartPage.in=" + DEFAULT_MEDIA_START_PAGE + "," + UPDATED_MEDIA_START_PAGE,
            "mediaStartPage.in=" + UPDATED_MEDIA_START_PAGE
        );
    }

    @Test
    @Transactional
    void getAllBooksByMediaStartPageIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where mediaStartPage is not null
        defaultBookFiltering("mediaStartPage.specified=true", "mediaStartPage.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByMediaStartPageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where mediaStartPage is greater than or equal to
        defaultBookFiltering(
            "mediaStartPage.greaterThanOrEqual=" + DEFAULT_MEDIA_START_PAGE,
            "mediaStartPage.greaterThanOrEqual=" + UPDATED_MEDIA_START_PAGE
        );
    }

    @Test
    @Transactional
    void getAllBooksByMediaStartPageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where mediaStartPage is less than or equal to
        defaultBookFiltering(
            "mediaStartPage.lessThanOrEqual=" + DEFAULT_MEDIA_START_PAGE,
            "mediaStartPage.lessThanOrEqual=" + SMALLER_MEDIA_START_PAGE
        );
    }

    @Test
    @Transactional
    void getAllBooksByMediaStartPageIsLessThanSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where mediaStartPage is less than
        defaultBookFiltering("mediaStartPage.lessThan=" + UPDATED_MEDIA_START_PAGE, "mediaStartPage.lessThan=" + DEFAULT_MEDIA_START_PAGE);
    }

    @Test
    @Transactional
    void getAllBooksByMediaStartPageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where mediaStartPage is greater than
        defaultBookFiltering(
            "mediaStartPage.greaterThan=" + SMALLER_MEDIA_START_PAGE,
            "mediaStartPage.greaterThan=" + DEFAULT_MEDIA_START_PAGE
        );
    }

    @Test
    @Transactional
    void getAllBooksByMediaEndPageIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where mediaEndPage equals to
        defaultBookFiltering("mediaEndPage.equals=" + DEFAULT_MEDIA_END_PAGE, "mediaEndPage.equals=" + UPDATED_MEDIA_END_PAGE);
    }

    @Test
    @Transactional
    void getAllBooksByMediaEndPageIsInShouldWork() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where mediaEndPage in
        defaultBookFiltering(
            "mediaEndPage.in=" + DEFAULT_MEDIA_END_PAGE + "," + UPDATED_MEDIA_END_PAGE,
            "mediaEndPage.in=" + UPDATED_MEDIA_END_PAGE
        );
    }

    @Test
    @Transactional
    void getAllBooksByMediaEndPageIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where mediaEndPage is not null
        defaultBookFiltering("mediaEndPage.specified=true", "mediaEndPage.specified=false");
    }

    @Test
    @Transactional
    void getAllBooksByMediaEndPageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where mediaEndPage is greater than or equal to
        defaultBookFiltering(
            "mediaEndPage.greaterThanOrEqual=" + DEFAULT_MEDIA_END_PAGE,
            "mediaEndPage.greaterThanOrEqual=" + UPDATED_MEDIA_END_PAGE
        );
    }

    @Test
    @Transactional
    void getAllBooksByMediaEndPageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where mediaEndPage is less than or equal to
        defaultBookFiltering(
            "mediaEndPage.lessThanOrEqual=" + DEFAULT_MEDIA_END_PAGE,
            "mediaEndPage.lessThanOrEqual=" + SMALLER_MEDIA_END_PAGE
        );
    }

    @Test
    @Transactional
    void getAllBooksByMediaEndPageIsLessThanSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where mediaEndPage is less than
        defaultBookFiltering("mediaEndPage.lessThan=" + UPDATED_MEDIA_END_PAGE, "mediaEndPage.lessThan=" + DEFAULT_MEDIA_END_PAGE);
    }

    @Test
    @Transactional
    void getAllBooksByMediaEndPageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where mediaEndPage is greater than
        defaultBookFiltering("mediaEndPage.greaterThan=" + SMALLER_MEDIA_END_PAGE, "mediaEndPage.greaterThan=" + DEFAULT_MEDIA_END_PAGE);
    }

    @Test
    @Transactional
    void getAllBooksByAuthorIsEqualToSomething() throws Exception {
        Author author;
        if (TestUtil.findAll(em, Author.class).isEmpty()) {
            bookRepository.saveAndFlush(book);
            author = AuthorResourceIT.createEntity(em);
        } else {
            author = TestUtil.findAll(em, Author.class).get(0);
        }
        em.persist(author);
        em.flush();
        book.setAuthor(author);
        bookRepository.saveAndFlush(book);
        Long authorId = author.getId();
        // Get all the bookList where author equals to authorId
        defaultBookShouldBeFound("authorId.equals=" + authorId);

        // Get all the bookList where author equals to (authorId + 1)
        defaultBookShouldNotBeFound("authorId.equals=" + (authorId + 1));
    }

    @Test
    @Transactional
    void getAllBooksByMediaIsEqualToSomething() throws Exception {
        Media media;
        if (TestUtil.findAll(em, Media.class).isEmpty()) {
            bookRepository.saveAndFlush(book);
            media = MediaResourceIT.createEntity(em);
        } else {
            media = TestUtil.findAll(em, Media.class).get(0);
        }
        em.persist(media);
        em.flush();
        book.setMedia(media);
        bookRepository.saveAndFlush(book);
        Long mediaId = media.getId();
        // Get all the bookList where media equals to mediaId
        defaultBookShouldBeFound("mediaId.equals=" + mediaId);

        // Get all the bookList where media equals to (mediaId + 1)
        defaultBookShouldNotBeFound("mediaId.equals=" + (mediaId + 1));
    }

    private void defaultBookFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultBookShouldBeFound(shouldBeFound);
        defaultBookShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBookShouldBeFound(String filter) throws Exception {
        restBookMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(book.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].mediaStartPage").value(hasItem(DEFAULT_MEDIA_START_PAGE)))
            .andExpect(jsonPath("$.[*].mediaEndPage").value(hasItem(DEFAULT_MEDIA_END_PAGE)));

        // Check, that the count call also returns 1
        restBookMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBookShouldNotBeFound(String filter) throws Exception {
        restBookMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBookMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBook() throws Exception {
        // Get the book
        restBookMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBook() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the book
        Book updatedBook = bookRepository.findById(book.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBook are not directly saved in db
        em.detach(updatedBook);
        updatedBook
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .mediaStartPage(UPDATED_MEDIA_START_PAGE)
            .mediaEndPage(UPDATED_MEDIA_END_PAGE);
        BookDTO bookDTO = bookMapper.toDto(updatedBook);

        restBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookDTO))
            )
            .andExpect(status().isOk());

        // Validate the Book in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBookToMatchAllProperties(updatedBook);
    }

    @Test
    @Transactional
    void putNonExistingBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        book.setId(longCount.incrementAndGet());

        // Create the Book
        BookDTO bookDTO = bookMapper.toDto(book);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Book in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        book.setId(longCount.incrementAndGet());

        // Create the Book
        BookDTO bookDTO = bookMapper.toDto(book);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Book in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        book.setId(longCount.incrementAndGet());

        // Create the Book
        BookDTO bookDTO = bookMapper.toDto(book);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Book in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookWithPatch() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the book using partial update
        Book partialUpdatedBook = new Book();
        partialUpdatedBook.setId(book.getId());

        partialUpdatedBook.description(UPDATED_DESCRIPTION).mediaEndPage(UPDATED_MEDIA_END_PAGE);

        restBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBook.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBook))
            )
            .andExpect(status().isOk());

        // Validate the Book in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBook, book), getPersistedBook(book));
    }

    @Test
    @Transactional
    void fullUpdateBookWithPatch() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the book using partial update
        Book partialUpdatedBook = new Book();
        partialUpdatedBook.setId(book.getId());

        partialUpdatedBook
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .mediaStartPage(UPDATED_MEDIA_START_PAGE)
            .mediaEndPage(UPDATED_MEDIA_END_PAGE);

        restBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBook.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBook))
            )
            .andExpect(status().isOk());

        // Validate the Book in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookUpdatableFieldsEquals(partialUpdatedBook, getPersistedBook(partialUpdatedBook));
    }

    @Test
    @Transactional
    void patchNonExistingBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        book.setId(longCount.incrementAndGet());

        // Create the Book
        BookDTO bookDTO = bookMapper.toDto(book);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Book in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        book.setId(longCount.incrementAndGet());

        // Create the Book
        BookDTO bookDTO = bookMapper.toDto(book);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Book in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        book.setId(longCount.incrementAndGet());

        // Create the Book
        BookDTO bookDTO = bookMapper.toDto(book);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bookDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Book in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBook() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the book
        restBookMockMvc
            .perform(delete(ENTITY_API_URL_ID, book.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bookRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Book getPersistedBook(Book book) {
        return bookRepository.findById(book.getId()).orElseThrow();
    }

    protected void assertPersistedBookToMatchAllProperties(Book expectedBook) {
        assertBookAllPropertiesEquals(expectedBook, getPersistedBook(expectedBook));
    }

    protected void assertPersistedBookToMatchUpdatableProperties(Book expectedBook) {
        assertBookAllUpdatablePropertiesEquals(expectedBook, getPersistedBook(expectedBook));
    }
}
