package com.weisen.www.code.yjf.login.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weisen.www.code.yjf.login.domain.SmsService;
import com.weisen.www.code.yjf.login.repository.SmsServiceRepository;
import com.weisen.www.code.yjf.login.service.dto.SmsServiceDTO;
import com.weisen.www.code.yjf.login.service.mapper.SmsServiceMapper;

/**
 * Service class for managing SmsService.
 */
@Service
@Transactional
public class SmsServiceService {
	
    private final Logger log = LoggerFactory.getLogger(SmsServiceService.class);

    private final SmsServiceRepository smsServiceRepository;

    private final SmsServiceMapper smsServiceMapper;
    
    public SmsServiceService(SmsServiceRepository smsServiceRepository, SmsServiceMapper smsServiceMapper) {
        this.smsServiceRepository = smsServiceRepository;
        this.smsServiceMapper = smsServiceMapper;
    }
    
    public SmsServiceDTO save(SmsServiceDTO smsServiceDTO) {
	    log.debug("Request to save SmsService : {}", smsServiceDTO);
	    SmsService smsService = smsServiceMapper.toEntity(smsServiceDTO);
	    smsService = smsServiceRepository.save(smsService);
	    return smsServiceMapper.toDto(smsService);
    }
    
    @Transactional(readOnly = true)
    public List<SmsServiceDTO> findAll() {
        log.debug("Request to get all SmsServices");
        return smsServiceRepository.findAll().stream()
            .map(smsServiceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public Optional<SmsServiceDTO> findOne(Long id) {
        log.debug("Request to get SmsService : {}", id);
        return smsServiceRepository.findById(id)
            .map(smsServiceMapper::toDto);
    }
    
    public void delete(Long id) {
        log.debug("Request to delete SmsService : {}", id);
        smsServiceRepository.deleteById(id);
    }
}
