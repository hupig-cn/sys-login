package com.weisen.www.code.yjf.login.web.rest;

import com.weisen.www.code.yjf.login.service.SmsServiceService;
import com.weisen.www.code.yjf.login.web.rest.errors.BadRequestAlertException;
import com.weisen.www.code.yjf.login.service.dto.SmsServiceDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.weisen.www.code.yjf.login.domain.SmsService}.
 */
@RestController
@RequestMapping("/api")
public class SmsServiceResource {

    private final Logger log = LoggerFactory.getLogger(SmsServiceResource.class);

    private static final String ENTITY_NAME = "smsService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SmsServiceService smsServiceService;

    public SmsServiceResource(SmsServiceService smsServiceService) {
        this.smsServiceService = smsServiceService;
    }

    /**
     * {@code POST  /sms-services} : Create a new smsService.
     *
     * @param smsServiceDTO the smsServiceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new smsServiceDTO, or with status {@code 400 (Bad Request)} if the smsService has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sms-services")
    public ResponseEntity<SmsServiceDTO> createSmsService(@RequestBody SmsServiceDTO smsServiceDTO) throws URISyntaxException {
        log.debug("REST request to save SmsService : {}", smsServiceDTO);
        if (smsServiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new smsService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SmsServiceDTO result = smsServiceService.save(smsServiceDTO);
        return ResponseEntity.created(new URI("/api/sms-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sms-services} : Updates an existing smsService.
     *
     * @param smsServiceDTO the smsServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smsServiceDTO,
     * or with status {@code 400 (Bad Request)} if the smsServiceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the smsServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sms-services")
    public ResponseEntity<SmsServiceDTO> updateSmsService(@RequestBody SmsServiceDTO smsServiceDTO) throws URISyntaxException {
        log.debug("REST request to update SmsService : {}", smsServiceDTO);
        if (smsServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SmsServiceDTO result = smsServiceService.save(smsServiceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, smsServiceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sms-services} : get all the smsServices.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of smsServices in body.
     */
    @GetMapping("/sms-services")
    public List<SmsServiceDTO> getAllSmsServices() {
        log.debug("REST request to get all SmsServices");
        return smsServiceService.findAll();
    }

    /**
     * {@code GET  /sms-services/:id} : get the "id" smsService.
     *
     * @param id the id of the smsServiceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the smsServiceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sms-services/{id}")
    public ResponseEntity<SmsServiceDTO> getSmsService(@PathVariable Long id) {
        log.debug("REST request to get SmsService : {}", id);
        Optional<SmsServiceDTO> smsServiceDTO = smsServiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(smsServiceDTO);
    }

    /**
     * {@code DELETE  /sms-services/:id} : delete the "id" smsService.
     *
     * @param id the id of the smsServiceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sms-services/{id}")
    public ResponseEntity<Void> deleteSmsService(@PathVariable Long id) {
        log.debug("REST request to delete SmsService : {}", id);
        smsServiceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
