package com.chemista15.web.rest;

import static com.chemista15.domain.MediaAsserts.*;
import static com.chemista15.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chemista15.IntegrationTest;
import com.chemista15.domain.Collection;
import com.chemista15.domain.Media;
import com.chemista15.repository.MediaRepository;
import com.chemista15.service.MediaService;
import com.chemista15.service.dto.MediaDTO;
import com.chemista15.service.mapper.MediaMapper;
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
 * Integration tests for the {@link MediaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MediaResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FILE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_DESC = "AAAAAAAAAA";
    private static final String UPDATED_FILE_DESC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/media";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MediaRepository mediaRepository;

    @Mock
    private MediaRepository mediaRepositoryMock;

    @Autowired
    private MediaMapper mediaMapper;

    @Mock
    private MediaService mediaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMediaMockMvc;

    private Media media;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Media createEntity(EntityManager em) {
        Media media = new Media().uuid(DEFAULT_UUID).fileName(DEFAULT_FILE_NAME).fileType(DEFAULT_FILE_TYPE).fileDesc(DEFAULT_FILE_DESC);
        return media;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Media createUpdatedEntity(EntityManager em) {
        Media media = new Media().uuid(UPDATED_UUID).fileName(UPDATED_FILE_NAME).fileType(UPDATED_FILE_TYPE).fileDesc(UPDATED_FILE_DESC);
        return media;
    }

    @BeforeEach
    public void initTest() {
        media = createEntity(em);
    }

    @Test
    @Transactional
    void createMedia() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(media);
        var returnedMediaDTO = om.readValue(
            restMediaMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mediaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MediaDTO.class
        );

        // Validate the Media in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedia = mediaMapper.toEntity(returnedMediaDTO);
        assertMediaUpdatableFieldsEquals(returnedMedia, getPersistedMedia(returnedMedia));
    }

    @Test
    @Transactional
    void createMediaWithExistingId() throws Exception {
        // Create the Media with an existing ID
        media.setId(1L);
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMediaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mediaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Media in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMedia() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList
        restMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(media.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE)))
            .andExpect(jsonPath("$.[*].fileDesc").value(hasItem(DEFAULT_FILE_DESC)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMediaWithEagerRelationshipsIsEnabled() throws Exception {
        when(mediaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMediaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(mediaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMediaWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(mediaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMediaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(mediaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMedia() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get the media
        restMediaMockMvc
            .perform(get(ENTITY_API_URL_ID, media.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(media.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.fileType").value(DEFAULT_FILE_TYPE))
            .andExpect(jsonPath("$.fileDesc").value(DEFAULT_FILE_DESC));
    }

    @Test
    @Transactional
    void getMediaByIdFiltering() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        Long id = media.getId();

        defaultMediaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMediaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMediaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMediaByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where uuid equals to
        defaultMediaFiltering("uuid.equals=" + DEFAULT_UUID, "uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllMediaByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where uuid in
        defaultMediaFiltering("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID, "uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllMediaByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where uuid is not null
        defaultMediaFiltering("uuid.specified=true", "uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllMediaByFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileName equals to
        defaultMediaFiltering("fileName.equals=" + DEFAULT_FILE_NAME, "fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllMediaByFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileName in
        defaultMediaFiltering("fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME, "fileName.in=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllMediaByFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileName is not null
        defaultMediaFiltering("fileName.specified=true", "fileName.specified=false");
    }

    @Test
    @Transactional
    void getAllMediaByFileNameContainsSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileName contains
        defaultMediaFiltering("fileName.contains=" + DEFAULT_FILE_NAME, "fileName.contains=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllMediaByFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileName does not contain
        defaultMediaFiltering("fileName.doesNotContain=" + UPDATED_FILE_NAME, "fileName.doesNotContain=" + DEFAULT_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllMediaByFileTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileType equals to
        defaultMediaFiltering("fileType.equals=" + DEFAULT_FILE_TYPE, "fileType.equals=" + UPDATED_FILE_TYPE);
    }

    @Test
    @Transactional
    void getAllMediaByFileTypeIsInShouldWork() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileType in
        defaultMediaFiltering("fileType.in=" + DEFAULT_FILE_TYPE + "," + UPDATED_FILE_TYPE, "fileType.in=" + UPDATED_FILE_TYPE);
    }

    @Test
    @Transactional
    void getAllMediaByFileTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileType is not null
        defaultMediaFiltering("fileType.specified=true", "fileType.specified=false");
    }

    @Test
    @Transactional
    void getAllMediaByFileTypeContainsSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileType contains
        defaultMediaFiltering("fileType.contains=" + DEFAULT_FILE_TYPE, "fileType.contains=" + UPDATED_FILE_TYPE);
    }

    @Test
    @Transactional
    void getAllMediaByFileTypeNotContainsSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileType does not contain
        defaultMediaFiltering("fileType.doesNotContain=" + UPDATED_FILE_TYPE, "fileType.doesNotContain=" + DEFAULT_FILE_TYPE);
    }

    @Test
    @Transactional
    void getAllMediaByFileDescIsEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileDesc equals to
        defaultMediaFiltering("fileDesc.equals=" + DEFAULT_FILE_DESC, "fileDesc.equals=" + UPDATED_FILE_DESC);
    }

    @Test
    @Transactional
    void getAllMediaByFileDescIsInShouldWork() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileDesc in
        defaultMediaFiltering("fileDesc.in=" + DEFAULT_FILE_DESC + "," + UPDATED_FILE_DESC, "fileDesc.in=" + UPDATED_FILE_DESC);
    }

    @Test
    @Transactional
    void getAllMediaByFileDescIsNullOrNotNull() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileDesc is not null
        defaultMediaFiltering("fileDesc.specified=true", "fileDesc.specified=false");
    }

    @Test
    @Transactional
    void getAllMediaByFileDescContainsSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileDesc contains
        defaultMediaFiltering("fileDesc.contains=" + DEFAULT_FILE_DESC, "fileDesc.contains=" + UPDATED_FILE_DESC);
    }

    @Test
    @Transactional
    void getAllMediaByFileDescNotContainsSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileDesc does not contain
        defaultMediaFiltering("fileDesc.doesNotContain=" + UPDATED_FILE_DESC, "fileDesc.doesNotContain=" + DEFAULT_FILE_DESC);
    }

    @Test
    @Transactional
    void getAllMediaByCollectionIsEqualToSomething() throws Exception {
        Collection collection;
        if (TestUtil.findAll(em, Collection.class).isEmpty()) {
            mediaRepository.saveAndFlush(media);
            collection = CollectionResourceIT.createEntity(em);
        } else {
            collection = TestUtil.findAll(em, Collection.class).get(0);
        }
        em.persist(collection);
        em.flush();
        media.setCollection(collection);
        mediaRepository.saveAndFlush(media);
        Long collectionId = collection.getId();
        // Get all the mediaList where collection equals to collectionId
        defaultMediaShouldBeFound("collectionId.equals=" + collectionId);

        // Get all the mediaList where collection equals to (collectionId + 1)
        defaultMediaShouldNotBeFound("collectionId.equals=" + (collectionId + 1));
    }

    private void defaultMediaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMediaShouldBeFound(shouldBeFound);
        defaultMediaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMediaShouldBeFound(String filter) throws Exception {
        restMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(media.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE)))
            .andExpect(jsonPath("$.[*].fileDesc").value(hasItem(DEFAULT_FILE_DESC)));

        // Check, that the count call also returns 1
        restMediaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMediaShouldNotBeFound(String filter) throws Exception {
        restMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMediaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMedia() throws Exception {
        // Get the media
        restMediaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedia() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the media
        Media updatedMedia = mediaRepository.findById(media.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedia are not directly saved in db
        em.detach(updatedMedia);
        updatedMedia.uuid(UPDATED_UUID).fileName(UPDATED_FILE_NAME).fileType(UPDATED_FILE_TYPE).fileDesc(UPDATED_FILE_DESC);
        MediaDTO mediaDTO = mediaMapper.toDto(updatedMedia);

        restMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mediaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mediaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Media in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMediaToMatchAllProperties(updatedMedia);
    }

    @Test
    @Transactional
    void putNonExistingMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        media.setId(longCount.incrementAndGet());

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mediaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Media in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        media.setId(longCount.incrementAndGet());

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Media in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        media.setId(longCount.incrementAndGet());

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMediaMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mediaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Media in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMediaWithPatch() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the media using partial update
        Media partialUpdatedMedia = new Media();
        partialUpdatedMedia.setId(media.getId());

        partialUpdatedMedia.fileName(UPDATED_FILE_NAME).fileType(UPDATED_FILE_TYPE);

        restMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedia.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedia))
            )
            .andExpect(status().isOk());

        // Validate the Media in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMediaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMedia, media), getPersistedMedia(media));
    }

    @Test
    @Transactional
    void fullUpdateMediaWithPatch() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the media using partial update
        Media partialUpdatedMedia = new Media();
        partialUpdatedMedia.setId(media.getId());

        partialUpdatedMedia.uuid(UPDATED_UUID).fileName(UPDATED_FILE_NAME).fileType(UPDATED_FILE_TYPE).fileDesc(UPDATED_FILE_DESC);

        restMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedia.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedia))
            )
            .andExpect(status().isOk());

        // Validate the Media in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMediaUpdatableFieldsEquals(partialUpdatedMedia, getPersistedMedia(partialUpdatedMedia));
    }

    @Test
    @Transactional
    void patchNonExistingMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        media.setId(longCount.incrementAndGet());

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mediaDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Media in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        media.setId(longCount.incrementAndGet());

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Media in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        media.setId(longCount.incrementAndGet());

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMediaMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(mediaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Media in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedia() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the media
        restMediaMockMvc
            .perform(delete(ENTITY_API_URL_ID, media.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return mediaRepository.count();
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

    protected Media getPersistedMedia(Media media) {
        return mediaRepository.findById(media.getId()).orElseThrow();
    }

    protected void assertPersistedMediaToMatchAllProperties(Media expectedMedia) {
        assertMediaAllPropertiesEquals(expectedMedia, getPersistedMedia(expectedMedia));
    }

    protected void assertPersistedMediaToMatchUpdatableProperties(Media expectedMedia) {
        assertMediaAllUpdatablePropertiesEquals(expectedMedia, getPersistedMedia(expectedMedia));
    }
}
