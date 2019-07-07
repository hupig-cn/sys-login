package com.weisen.www.code.yjf.login.web.rest;

import com.weisen.www.code.yjf.login.LoginApp;
import com.weisen.www.code.yjf.login.config.SecurityBeanOverrideConfiguration;
import com.weisen.www.code.yjf.login.domain.SmsService;
import com.weisen.www.code.yjf.login.repository.SmsServiceRepository;
import com.weisen.www.code.yjf.login.service.SmsServiceService;
import com.weisen.www.code.yjf.login.service.dto.SmsServiceDTO;
import com.weisen.www.code.yjf.login.service.mapper.SmsServiceMapper;
import com.weisen.www.code.yjf.login.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.weisen.www.code.yjf.login.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link SmsServiceResource} REST controller.
 */
@SpringBootTest(classes = LoginApp.class)
public class SmsServiceResourceIT {

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_SENDTIME = 1L;
    private static final Long UPDATED_SENDTIME = 2L;

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    private static final String DEFAULT_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_RESULT = "BBBBBBBBBB";

    @Autowired
    private SmsServiceRepository smsServiceRepository;

    @Autowired
    private SmsServiceMapper smsServiceMapper;

    @Autowired
    private SmsServiceService smsServiceService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restSmsServiceMockMvc;

    private SmsService smsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SmsServiceResource smsServiceResource = new SmsServiceResource(smsServiceService);
        this.restSmsServiceMockMvc = MockMvcBuilders.standaloneSetup(smsServiceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SmsService createEntity(EntityManager em) {
        SmsService smsService = new SmsService()
            .phone(DEFAULT_PHONE)
            .code(DEFAULT_CODE)
            .type(DEFAULT_TYPE)
            .sendtime(DEFAULT_SENDTIME)
            .number(DEFAULT_NUMBER)
            .result(DEFAULT_RESULT);
        return smsService;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SmsService createUpdatedEntity(EntityManager em) {
        SmsService smsService = new SmsService()
            .phone(UPDATED_PHONE)
            .code(UPDATED_CODE)
            .type(UPDATED_TYPE)
            .sendtime(UPDATED_SENDTIME)
            .number(UPDATED_NUMBER)
            .result(UPDATED_RESULT);
        return smsService;
    }

    @BeforeEach
    public void initTest() {
        smsService = createEntity(em);
    }

    @Test
    @Transactional
    public void createSmsService() throws Exception {
        int databaseSizeBeforeCreate = smsServiceRepository.findAll().size();

        // Create the SmsService
        SmsServiceDTO smsServiceDTO = smsServiceMapper.toDto(smsService);
        restSmsServiceMockMvc.perform(post("/api/sms-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsServiceDTO)))
            .andExpect(status().isCreated());

        // Validate the SmsService in the database
        List<SmsService> smsServiceList = smsServiceRepository.findAll();
        assertThat(smsServiceList).hasSize(databaseSizeBeforeCreate + 1);
        SmsService testSmsService = smsServiceList.get(smsServiceList.size() - 1);
        assertThat(testSmsService.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testSmsService.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSmsService.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSmsService.getSendtime()).isEqualTo(DEFAULT_SENDTIME);
        assertThat(testSmsService.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testSmsService.getResult()).isEqualTo(DEFAULT_RESULT);
    }

    @Test
    @Transactional
    public void createSmsServiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = smsServiceRepository.findAll().size();

        // Create the SmsService with an existing ID
        smsService.setId(1L);
        SmsServiceDTO smsServiceDTO = smsServiceMapper.toDto(smsService);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmsServiceMockMvc.perform(post("/api/sms-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsServiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmsService in the database
        List<SmsService> smsServiceList = smsServiceRepository.findAll();
        assertThat(smsServiceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSmsServices() throws Exception {
        // Initialize the database
        smsServiceRepository.saveAndFlush(smsService);

        // Get all the smsServiceList
        restSmsServiceMockMvc.perform(get("/api/sms-services?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smsService.getId().intValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].sendtime").value(hasItem(DEFAULT_SENDTIME.intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.toString())));
    }
    
    @Test
    @Transactional
    public void getSmsService() throws Exception {
        // Initialize the database
        smsServiceRepository.saveAndFlush(smsService);

        // Get the smsService
        restSmsServiceMockMvc.perform(get("/api/sms-services/{id}", smsService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(smsService.getId().intValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.sendtime").value(DEFAULT_SENDTIME.intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSmsService() throws Exception {
        // Get the smsService
        restSmsServiceMockMvc.perform(get("/api/sms-services/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSmsService() throws Exception {
        // Initialize the database
        smsServiceRepository.saveAndFlush(smsService);

        int databaseSizeBeforeUpdate = smsServiceRepository.findAll().size();

        // Update the smsService
        SmsService updatedSmsService = smsServiceRepository.findById(smsService.getId()).get();
        // Disconnect from session so that the updates on updatedSmsService are not directly saved in db
        em.detach(updatedSmsService);
        updatedSmsService
            .phone(UPDATED_PHONE)
            .code(UPDATED_CODE)
            .type(UPDATED_TYPE)
            .sendtime(UPDATED_SENDTIME)
            .number(UPDATED_NUMBER)
            .result(UPDATED_RESULT);
        SmsServiceDTO smsServiceDTO = smsServiceMapper.toDto(updatedSmsService);

        restSmsServiceMockMvc.perform(put("/api/sms-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsServiceDTO)))
            .andExpect(status().isOk());

        // Validate the SmsService in the database
        List<SmsService> smsServiceList = smsServiceRepository.findAll();
        assertThat(smsServiceList).hasSize(databaseSizeBeforeUpdate);
        SmsService testSmsService = smsServiceList.get(smsServiceList.size() - 1);
        assertThat(testSmsService.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testSmsService.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSmsService.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSmsService.getSendtime()).isEqualTo(UPDATED_SENDTIME);
        assertThat(testSmsService.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testSmsService.getResult()).isEqualTo(UPDATED_RESULT);
    }

    @Test
    @Transactional
    public void updateNonExistingSmsService() throws Exception {
        int databaseSizeBeforeUpdate = smsServiceRepository.findAll().size();

        // Create the SmsService
        SmsServiceDTO smsServiceDTO = smsServiceMapper.toDto(smsService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmsServiceMockMvc.perform(put("/api/sms-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsServiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmsService in the database
        List<SmsService> smsServiceList = smsServiceRepository.findAll();
        assertThat(smsServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSmsService() throws Exception {
        // Initialize the database
        smsServiceRepository.saveAndFlush(smsService);

        int databaseSizeBeforeDelete = smsServiceRepository.findAll().size();

        // Delete the smsService
        restSmsServiceMockMvc.perform(delete("/api/sms-services/{id}", smsService.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<SmsService> smsServiceList = smsServiceRepository.findAll();
        assertThat(smsServiceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmsService.class);
        SmsService smsService1 = new SmsService();
        smsService1.setId(1L);
        SmsService smsService2 = new SmsService();
        smsService2.setId(smsService1.getId());
        assertThat(smsService1).isEqualTo(smsService2);
        smsService2.setId(2L);
        assertThat(smsService1).isNotEqualTo(smsService2);
        smsService1.setId(null);
        assertThat(smsService1).isNotEqualTo(smsService2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmsServiceDTO.class);
        SmsServiceDTO smsServiceDTO1 = new SmsServiceDTO();
        smsServiceDTO1.setId(1L);
        SmsServiceDTO smsServiceDTO2 = new SmsServiceDTO();
        assertThat(smsServiceDTO1).isNotEqualTo(smsServiceDTO2);
        smsServiceDTO2.setId(smsServiceDTO1.getId());
        assertThat(smsServiceDTO1).isEqualTo(smsServiceDTO2);
        smsServiceDTO2.setId(2L);
        assertThat(smsServiceDTO1).isNotEqualTo(smsServiceDTO2);
        smsServiceDTO1.setId(null);
        assertThat(smsServiceDTO1).isNotEqualTo(smsServiceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(smsServiceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(smsServiceMapper.fromId(null)).isNull();
    }
}
