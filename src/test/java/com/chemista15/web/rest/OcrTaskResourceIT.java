package com.chemista15.web.rest;

import static com.chemista15.domain.OcrTaskAsserts.*;
import static com.chemista15.web.rest.TestUtil.createUpdateProxyForBean;
import static com.chemista15.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chemista15.IntegrationTest;
import com.chemista15.domain.OcrTask;
import com.chemista15.repository.OcrTaskRepository;
import com.chemista15.service.dto.OcrTaskDTO;
import com.chemista15.service.mapper.OcrTaskMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OcrTaskResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OcrTaskResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final Long DEFAULT_MEDIA_ID = 1L;
    private static final Long UPDATED_MEDIA_ID = 2L;
    private static final Long SMALLER_MEDIA_ID = 1L - 1L;

    private static final Integer DEFAULT_PAGE_NUMBER = 1;
    private static final Integer UPDATED_PAGE_NUMBER = 2;
    private static final Integer SMALLER_PAGE_NUMBER = 1 - 1;

    private static final String DEFAULT_JOB_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_JOB_STATUS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_STOP_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_STOP_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_STOP_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/ocr-tasks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OcrTaskRepository ocrTaskRepository;

    @Autowired
    private OcrTaskMapper ocrTaskMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOcrTaskMockMvc;

    private OcrTask ocrTask;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OcrTask createEntity(EntityManager em) {
        OcrTask ocrTask = new OcrTask()
            .uuid(DEFAULT_UUID)
            .mediaId(DEFAULT_MEDIA_ID)
            .pageNumber(DEFAULT_PAGE_NUMBER)
            .jobStatus(DEFAULT_JOB_STATUS)
            .createTime(DEFAULT_CREATE_TIME)
            .startTime(DEFAULT_START_TIME)
            .stopTime(DEFAULT_STOP_TIME);
        return ocrTask;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OcrTask createUpdatedEntity(EntityManager em) {
        OcrTask ocrTask = new OcrTask()
            .uuid(UPDATED_UUID)
            .mediaId(UPDATED_MEDIA_ID)
            .pageNumber(UPDATED_PAGE_NUMBER)
            .jobStatus(UPDATED_JOB_STATUS)
            .createTime(UPDATED_CREATE_TIME)
            .startTime(UPDATED_START_TIME)
            .stopTime(UPDATED_STOP_TIME);
        return ocrTask;
    }

    @BeforeEach
    public void initTest() {
        ocrTask = createEntity(em);
    }

    @Test
    @Transactional
    void createOcrTask() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OcrTask
        OcrTaskDTO ocrTaskDTO = ocrTaskMapper.toDto(ocrTask);
        var returnedOcrTaskDTO = om.readValue(
            restOcrTaskMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrTaskDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OcrTaskDTO.class
        );

        // Validate the OcrTask in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOcrTask = ocrTaskMapper.toEntity(returnedOcrTaskDTO);
        assertOcrTaskUpdatableFieldsEquals(returnedOcrTask, getPersistedOcrTask(returnedOcrTask));
    }

    @Test
    @Transactional
    void createOcrTaskWithExistingId() throws Exception {
        // Create the OcrTask with an existing ID
        ocrTask.setId(1L);
        OcrTaskDTO ocrTaskDTO = ocrTaskMapper.toDto(ocrTask);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOcrTaskMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrTaskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OcrTask in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOcrTasks() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList
        restOcrTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ocrTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].mediaId").value(hasItem(DEFAULT_MEDIA_ID.intValue())))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].jobStatus").value(hasItem(DEFAULT_JOB_STATUS)))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(sameInstant(DEFAULT_CREATE_TIME))))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].stopTime").value(hasItem(sameInstant(DEFAULT_STOP_TIME))));
    }

    @Test
    @Transactional
    void getOcrTask() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get the ocrTask
        restOcrTaskMockMvc
            .perform(get(ENTITY_API_URL_ID, ocrTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ocrTask.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.mediaId").value(DEFAULT_MEDIA_ID.intValue()))
            .andExpect(jsonPath("$.pageNumber").value(DEFAULT_PAGE_NUMBER))
            .andExpect(jsonPath("$.jobStatus").value(DEFAULT_JOB_STATUS))
            .andExpect(jsonPath("$.createTime").value(sameInstant(DEFAULT_CREATE_TIME)))
            .andExpect(jsonPath("$.startTime").value(sameInstant(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.stopTime").value(sameInstant(DEFAULT_STOP_TIME)));
    }

    @Test
    @Transactional
    void getOcrTasksByIdFiltering() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        Long id = ocrTask.getId();

        defaultOcrTaskFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOcrTaskFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOcrTaskFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOcrTasksByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where uuid equals to
        defaultOcrTaskFiltering("uuid.equals=" + DEFAULT_UUID, "uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllOcrTasksByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where uuid in
        defaultOcrTaskFiltering("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID, "uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllOcrTasksByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where uuid is not null
        defaultOcrTaskFiltering("uuid.specified=true", "uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrTasksByMediaIdIsEqualToSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where mediaId equals to
        defaultOcrTaskFiltering("mediaId.equals=" + DEFAULT_MEDIA_ID, "mediaId.equals=" + UPDATED_MEDIA_ID);
    }

    @Test
    @Transactional
    void getAllOcrTasksByMediaIdIsInShouldWork() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where mediaId in
        defaultOcrTaskFiltering("mediaId.in=" + DEFAULT_MEDIA_ID + "," + UPDATED_MEDIA_ID, "mediaId.in=" + UPDATED_MEDIA_ID);
    }

    @Test
    @Transactional
    void getAllOcrTasksByMediaIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where mediaId is not null
        defaultOcrTaskFiltering("mediaId.specified=true", "mediaId.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrTasksByMediaIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where mediaId is greater than or equal to
        defaultOcrTaskFiltering("mediaId.greaterThanOrEqual=" + DEFAULT_MEDIA_ID, "mediaId.greaterThanOrEqual=" + UPDATED_MEDIA_ID);
    }

    @Test
    @Transactional
    void getAllOcrTasksByMediaIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where mediaId is less than or equal to
        defaultOcrTaskFiltering("mediaId.lessThanOrEqual=" + DEFAULT_MEDIA_ID, "mediaId.lessThanOrEqual=" + SMALLER_MEDIA_ID);
    }

    @Test
    @Transactional
    void getAllOcrTasksByMediaIdIsLessThanSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where mediaId is less than
        defaultOcrTaskFiltering("mediaId.lessThan=" + UPDATED_MEDIA_ID, "mediaId.lessThan=" + DEFAULT_MEDIA_ID);
    }

    @Test
    @Transactional
    void getAllOcrTasksByMediaIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where mediaId is greater than
        defaultOcrTaskFiltering("mediaId.greaterThan=" + SMALLER_MEDIA_ID, "mediaId.greaterThan=" + DEFAULT_MEDIA_ID);
    }

    @Test
    @Transactional
    void getAllOcrTasksByPageNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where pageNumber equals to
        defaultOcrTaskFiltering("pageNumber.equals=" + DEFAULT_PAGE_NUMBER, "pageNumber.equals=" + UPDATED_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllOcrTasksByPageNumberIsInShouldWork() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where pageNumber in
        defaultOcrTaskFiltering("pageNumber.in=" + DEFAULT_PAGE_NUMBER + "," + UPDATED_PAGE_NUMBER, "pageNumber.in=" + UPDATED_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllOcrTasksByPageNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where pageNumber is not null
        defaultOcrTaskFiltering("pageNumber.specified=true", "pageNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrTasksByPageNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where pageNumber is greater than or equal to
        defaultOcrTaskFiltering(
            "pageNumber.greaterThanOrEqual=" + DEFAULT_PAGE_NUMBER,
            "pageNumber.greaterThanOrEqual=" + UPDATED_PAGE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllOcrTasksByPageNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where pageNumber is less than or equal to
        defaultOcrTaskFiltering("pageNumber.lessThanOrEqual=" + DEFAULT_PAGE_NUMBER, "pageNumber.lessThanOrEqual=" + SMALLER_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllOcrTasksByPageNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where pageNumber is less than
        defaultOcrTaskFiltering("pageNumber.lessThan=" + UPDATED_PAGE_NUMBER, "pageNumber.lessThan=" + DEFAULT_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllOcrTasksByPageNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where pageNumber is greater than
        defaultOcrTaskFiltering("pageNumber.greaterThan=" + SMALLER_PAGE_NUMBER, "pageNumber.greaterThan=" + DEFAULT_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllOcrTasksByJobStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where jobStatus equals to
        defaultOcrTaskFiltering("jobStatus.equals=" + DEFAULT_JOB_STATUS, "jobStatus.equals=" + UPDATED_JOB_STATUS);
    }

    @Test
    @Transactional
    void getAllOcrTasksByJobStatusIsInShouldWork() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where jobStatus in
        defaultOcrTaskFiltering("jobStatus.in=" + DEFAULT_JOB_STATUS + "," + UPDATED_JOB_STATUS, "jobStatus.in=" + UPDATED_JOB_STATUS);
    }

    @Test
    @Transactional
    void getAllOcrTasksByJobStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where jobStatus is not null
        defaultOcrTaskFiltering("jobStatus.specified=true", "jobStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrTasksByJobStatusContainsSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where jobStatus contains
        defaultOcrTaskFiltering("jobStatus.contains=" + DEFAULT_JOB_STATUS, "jobStatus.contains=" + UPDATED_JOB_STATUS);
    }

    @Test
    @Transactional
    void getAllOcrTasksByJobStatusNotContainsSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where jobStatus does not contain
        defaultOcrTaskFiltering("jobStatus.doesNotContain=" + UPDATED_JOB_STATUS, "jobStatus.doesNotContain=" + DEFAULT_JOB_STATUS);
    }

    @Test
    @Transactional
    void getAllOcrTasksByCreateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where createTime equals to
        defaultOcrTaskFiltering("createTime.equals=" + DEFAULT_CREATE_TIME, "createTime.equals=" + UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllOcrTasksByCreateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where createTime in
        defaultOcrTaskFiltering("createTime.in=" + DEFAULT_CREATE_TIME + "," + UPDATED_CREATE_TIME, "createTime.in=" + UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllOcrTasksByCreateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where createTime is not null
        defaultOcrTaskFiltering("createTime.specified=true", "createTime.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrTasksByCreateTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where createTime is greater than or equal to
        defaultOcrTaskFiltering(
            "createTime.greaterThanOrEqual=" + DEFAULT_CREATE_TIME,
            "createTime.greaterThanOrEqual=" + UPDATED_CREATE_TIME
        );
    }

    @Test
    @Transactional
    void getAllOcrTasksByCreateTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where createTime is less than or equal to
        defaultOcrTaskFiltering("createTime.lessThanOrEqual=" + DEFAULT_CREATE_TIME, "createTime.lessThanOrEqual=" + SMALLER_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllOcrTasksByCreateTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where createTime is less than
        defaultOcrTaskFiltering("createTime.lessThan=" + UPDATED_CREATE_TIME, "createTime.lessThan=" + DEFAULT_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllOcrTasksByCreateTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where createTime is greater than
        defaultOcrTaskFiltering("createTime.greaterThan=" + SMALLER_CREATE_TIME, "createTime.greaterThan=" + DEFAULT_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllOcrTasksByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where startTime equals to
        defaultOcrTaskFiltering("startTime.equals=" + DEFAULT_START_TIME, "startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllOcrTasksByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where startTime in
        defaultOcrTaskFiltering("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME, "startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllOcrTasksByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where startTime is not null
        defaultOcrTaskFiltering("startTime.specified=true", "startTime.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrTasksByStartTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where startTime is greater than or equal to
        defaultOcrTaskFiltering("startTime.greaterThanOrEqual=" + DEFAULT_START_TIME, "startTime.greaterThanOrEqual=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllOcrTasksByStartTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where startTime is less than or equal to
        defaultOcrTaskFiltering("startTime.lessThanOrEqual=" + DEFAULT_START_TIME, "startTime.lessThanOrEqual=" + SMALLER_START_TIME);
    }

    @Test
    @Transactional
    void getAllOcrTasksByStartTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where startTime is less than
        defaultOcrTaskFiltering("startTime.lessThan=" + UPDATED_START_TIME, "startTime.lessThan=" + DEFAULT_START_TIME);
    }

    @Test
    @Transactional
    void getAllOcrTasksByStartTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where startTime is greater than
        defaultOcrTaskFiltering("startTime.greaterThan=" + SMALLER_START_TIME, "startTime.greaterThan=" + DEFAULT_START_TIME);
    }

    @Test
    @Transactional
    void getAllOcrTasksByStopTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where stopTime equals to
        defaultOcrTaskFiltering("stopTime.equals=" + DEFAULT_STOP_TIME, "stopTime.equals=" + UPDATED_STOP_TIME);
    }

    @Test
    @Transactional
    void getAllOcrTasksByStopTimeIsInShouldWork() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where stopTime in
        defaultOcrTaskFiltering("stopTime.in=" + DEFAULT_STOP_TIME + "," + UPDATED_STOP_TIME, "stopTime.in=" + UPDATED_STOP_TIME);
    }

    @Test
    @Transactional
    void getAllOcrTasksByStopTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where stopTime is not null
        defaultOcrTaskFiltering("stopTime.specified=true", "stopTime.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrTasksByStopTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where stopTime is greater than or equal to
        defaultOcrTaskFiltering("stopTime.greaterThanOrEqual=" + DEFAULT_STOP_TIME, "stopTime.greaterThanOrEqual=" + UPDATED_STOP_TIME);
    }

    @Test
    @Transactional
    void getAllOcrTasksByStopTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where stopTime is less than or equal to
        defaultOcrTaskFiltering("stopTime.lessThanOrEqual=" + DEFAULT_STOP_TIME, "stopTime.lessThanOrEqual=" + SMALLER_STOP_TIME);
    }

    @Test
    @Transactional
    void getAllOcrTasksByStopTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where stopTime is less than
        defaultOcrTaskFiltering("stopTime.lessThan=" + UPDATED_STOP_TIME, "stopTime.lessThan=" + DEFAULT_STOP_TIME);
    }

    @Test
    @Transactional
    void getAllOcrTasksByStopTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        // Get all the ocrTaskList where stopTime is greater than
        defaultOcrTaskFiltering("stopTime.greaterThan=" + SMALLER_STOP_TIME, "stopTime.greaterThan=" + DEFAULT_STOP_TIME);
    }

    private void defaultOcrTaskFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOcrTaskShouldBeFound(shouldBeFound);
        defaultOcrTaskShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOcrTaskShouldBeFound(String filter) throws Exception {
        restOcrTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ocrTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].mediaId").value(hasItem(DEFAULT_MEDIA_ID.intValue())))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].jobStatus").value(hasItem(DEFAULT_JOB_STATUS)))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(sameInstant(DEFAULT_CREATE_TIME))))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].stopTime").value(hasItem(sameInstant(DEFAULT_STOP_TIME))));

        // Check, that the count call also returns 1
        restOcrTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOcrTaskShouldNotBeFound(String filter) throws Exception {
        restOcrTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOcrTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOcrTask() throws Exception {
        // Get the ocrTask
        restOcrTaskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOcrTask() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ocrTask
        OcrTask updatedOcrTask = ocrTaskRepository.findById(ocrTask.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOcrTask are not directly saved in db
        em.detach(updatedOcrTask);
        updatedOcrTask
            .uuid(UPDATED_UUID)
            .mediaId(UPDATED_MEDIA_ID)
            .pageNumber(UPDATED_PAGE_NUMBER)
            .jobStatus(UPDATED_JOB_STATUS)
            .createTime(UPDATED_CREATE_TIME)
            .startTime(UPDATED_START_TIME)
            .stopTime(UPDATED_STOP_TIME);
        OcrTaskDTO ocrTaskDTO = ocrTaskMapper.toDto(updatedOcrTask);

        restOcrTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ocrTaskDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ocrTaskDTO))
            )
            .andExpect(status().isOk());

        // Validate the OcrTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOcrTaskToMatchAllProperties(updatedOcrTask);
    }

    @Test
    @Transactional
    void putNonExistingOcrTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ocrTask.setId(longCount.incrementAndGet());

        // Create the OcrTask
        OcrTaskDTO ocrTaskDTO = ocrTaskMapper.toDto(ocrTask);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOcrTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ocrTaskDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ocrTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OcrTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOcrTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ocrTask.setId(longCount.incrementAndGet());

        // Create the OcrTask
        OcrTaskDTO ocrTaskDTO = ocrTaskMapper.toDto(ocrTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ocrTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OcrTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOcrTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ocrTask.setId(longCount.incrementAndGet());

        // Create the OcrTask
        OcrTaskDTO ocrTaskDTO = ocrTaskMapper.toDto(ocrTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrTaskMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrTaskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OcrTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOcrTaskWithPatch() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ocrTask using partial update
        OcrTask partialUpdatedOcrTask = new OcrTask();
        partialUpdatedOcrTask.setId(ocrTask.getId());

        partialUpdatedOcrTask.uuid(UPDATED_UUID).pageNumber(UPDATED_PAGE_NUMBER).createTime(UPDATED_CREATE_TIME);

        restOcrTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOcrTask.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOcrTask))
            )
            .andExpect(status().isOk());

        // Validate the OcrTask in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOcrTaskUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedOcrTask, ocrTask), getPersistedOcrTask(ocrTask));
    }

    @Test
    @Transactional
    void fullUpdateOcrTaskWithPatch() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ocrTask using partial update
        OcrTask partialUpdatedOcrTask = new OcrTask();
        partialUpdatedOcrTask.setId(ocrTask.getId());

        partialUpdatedOcrTask
            .uuid(UPDATED_UUID)
            .mediaId(UPDATED_MEDIA_ID)
            .pageNumber(UPDATED_PAGE_NUMBER)
            .jobStatus(UPDATED_JOB_STATUS)
            .createTime(UPDATED_CREATE_TIME)
            .startTime(UPDATED_START_TIME)
            .stopTime(UPDATED_STOP_TIME);

        restOcrTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOcrTask.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOcrTask))
            )
            .andExpect(status().isOk());

        // Validate the OcrTask in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOcrTaskUpdatableFieldsEquals(partialUpdatedOcrTask, getPersistedOcrTask(partialUpdatedOcrTask));
    }

    @Test
    @Transactional
    void patchNonExistingOcrTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ocrTask.setId(longCount.incrementAndGet());

        // Create the OcrTask
        OcrTaskDTO ocrTaskDTO = ocrTaskMapper.toDto(ocrTask);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOcrTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ocrTaskDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ocrTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OcrTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOcrTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ocrTask.setId(longCount.incrementAndGet());

        // Create the OcrTask
        OcrTaskDTO ocrTaskDTO = ocrTaskMapper.toDto(ocrTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ocrTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OcrTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOcrTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ocrTask.setId(longCount.incrementAndGet());

        // Create the OcrTask
        OcrTaskDTO ocrTaskDTO = ocrTaskMapper.toDto(ocrTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrTaskMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ocrTaskDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OcrTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOcrTask() throws Exception {
        // Initialize the database
        ocrTaskRepository.saveAndFlush(ocrTask);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ocrTask
        restOcrTaskMockMvc
            .perform(delete(ENTITY_API_URL_ID, ocrTask.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ocrTaskRepository.count();
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

    protected OcrTask getPersistedOcrTask(OcrTask ocrTask) {
        return ocrTaskRepository.findById(ocrTask.getId()).orElseThrow();
    }

    protected void assertPersistedOcrTaskToMatchAllProperties(OcrTask expectedOcrTask) {
        assertOcrTaskAllPropertiesEquals(expectedOcrTask, getPersistedOcrTask(expectedOcrTask));
    }

    protected void assertPersistedOcrTaskToMatchUpdatableProperties(OcrTask expectedOcrTask) {
        assertOcrTaskAllUpdatablePropertiesEquals(expectedOcrTask, getPersistedOcrTask(expectedOcrTask));
    }
}
