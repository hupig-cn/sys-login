package com.weisen.www.code.yjf.login.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.weisen.www.code.yjf.login.domain.SmsService;
import com.weisen.www.code.yjf.login.repository.Rewrite_SmsServiceRepository;
import com.weisen.www.code.yjf.login.repository.UserRepository;
import com.weisen.www.code.yjf.login.service.dto.SmsServiceDTO;
import com.weisen.www.code.yjf.login.service.mapper.SmsServiceMapper;
import com.weisen.www.code.yjf.login.service.util.SendCode;

/**
 * Service class for managing SmsService.
 */
@Service
@Transactional
public class Rewrite_SmsServiceService {

	private final Logger log = LoggerFactory.getLogger(Rewrite_SmsServiceService.class);

	private final Rewrite_SmsServiceRepository rewrite_SmsServiceRepository;

	private final SmsServiceMapper smsServiceMapper;

	private final UserRepository userRepository;
	// 验证码位数
	private static final int DEF_COUNT = 6;
	// 每日可发送次数
	private static final int DEF_NUM = 10;
	// 验证码发送时间间隔
	private static final int DEF_INTERVAL = 60;
	// 验证码过期时间
	private static final int DEF_OVERDUE = 60 * 5;

	public Rewrite_SmsServiceService(Rewrite_SmsServiceRepository rewrite_SmsServiceRepository,
			SmsServiceMapper smsServiceMapper, UserRepository userRepository) {
		this.rewrite_SmsServiceRepository = rewrite_SmsServiceRepository;
		this.smsServiceMapper = smsServiceMapper;
		this.userRepository = userRepository;
	}

	public String save(String phone) {
		log.debug("Request to save SmsService : {}", phone);
		if (userRepository.findOneByLogin(phone).isPresent())
			return "此用户已存在。";
		SmsServiceDTO smsServiceDTO = new SmsServiceDTO();
		smsServiceDTO.setSendtime(System.currentTimeMillis());
		SmsService smsService = rewrite_SmsServiceRepository.findOneByPhone(phone);
		if (null != smsService) {
			if ((smsServiceDTO.getSendtime() - smsService.getSendtime()) / 1000 / (60 * 60 * 24) > 0) {
				smsServiceDTO.setNumber(0);
				smsServiceDTO.setId(smsService.getId());
			} else if (smsService.getNumber() < DEF_NUM) {
				if ((smsServiceDTO.getSendtime() - smsService.getSendtime()) / 1000 > DEF_INTERVAL) {
					smsServiceDTO.setNumber(smsService.getNumber());
					smsServiceDTO.setId(smsService.getId());
				} else {
					return "发送频率过高，请于60s后再尝试发送。";
				}
			} else {
				return "今日已超过最大发送信息次数。";
			}
		} else {
			smsServiceDTO.setNumber(0);
		}
		smsServiceDTO.setType("用户注册");
		smsServiceDTO.setPhone(phone);
		smsServiceDTO.setCode(RandomStringUtils.randomNumeric(DEF_COUNT));
		String result = SendCode.issue(phone, smsServiceDTO.getType(), smsServiceDTO.getCode());
		if ("发送成功".equals(result)) {
			smsServiceDTO.setNumber(smsServiceDTO.getNumber() + 1);
			smsServiceDTO.setResult(result);
			SmsService smsServices = smsServiceMapper.toEntity(smsServiceDTO);
			smsServices = rewrite_SmsServiceRepository.save(smsServices);
		}
		return result;
	}
	
	public String sendPayPasswordCode(String phone) {
		log.debug("Request to save SmsService : {}", phone);
		if (userRepository.findOneByLogin(phone).isPresent())
			return "此用户已存在。";
		SmsServiceDTO smsServiceDTO = new SmsServiceDTO();
		smsServiceDTO.setSendtime(System.currentTimeMillis());
		SmsService smsService = rewrite_SmsServiceRepository.findOneByPhone(phone);
		if (null != smsService) {
			if ((smsServiceDTO.getSendtime() - smsService.getSendtime()) / 1000 / (60 * 60 * 24) > 0) {
				smsServiceDTO.setNumber(0);
				smsServiceDTO.setId(smsService.getId());
			} else if (smsService.getNumber() < DEF_NUM) {
				if ((smsServiceDTO.getSendtime() - smsService.getSendtime()) / 1000 > DEF_INTERVAL) {
					smsServiceDTO.setNumber(smsService.getNumber());
					smsServiceDTO.setId(smsService.getId());
				} else {
					return "发送频率过高，请于60s后再尝试发送。";
				}
			} else {
				return "今日已超过最大发送信息次数。";
			}
		} else {
			smsServiceDTO.setNumber(0);
		}
		smsServiceDTO.setType("修改密码");
		smsServiceDTO.setPhone(phone);
		smsServiceDTO.setCode(RandomStringUtils.randomNumeric(DEF_COUNT));
		String result = SendCode.issue(phone, smsServiceDTO.getType(), smsServiceDTO.getCode());
		if ("发送成功".equals(result)) {
			smsServiceDTO.setNumber(smsServiceDTO.getNumber() + 1);
			smsServiceDTO.setResult(result);
			SmsService smsServices = smsServiceMapper.toEntity(smsServiceDTO);
			smsServices = rewrite_SmsServiceRepository.save(smsServices);
		}
		return result;
	}
	
	public String validateCode(String login, String vertifyCode, String type) {
		SmsService mSmsData = rewrite_SmsServiceRepository.findOneByPhoneAndType(login, type);
		log.debug(new Gson().toJson(mSmsData));
		if (mSmsData == null) {
			return "非法调用";
		} else if ((System.currentTimeMillis() - mSmsData.getSendtime()) / 1000 > DEF_OVERDUE) {
			return "验证码已失效";
		} else if (!mSmsData.getCode().equals(vertifyCode)) {
			return "验证码不正确";
		} else {
			return "操作成功";
		}
	}

//    @Transactional(readOnly = true)
//    public List<SmsServiceDTO> findAll() {
//        log.debug("Request to get all SmsServices");
//        return smsServiceRepository.findAll().stream()
//            .map(smsServiceMapper::toDto)
//            .collect(Collectors.toCollection(LinkedList::new));
//    }
//
//    @Transactional(readOnly = true)
//    public Optional<SmsServiceDTO> findOne(Long id) {
//        log.debug("Request to get SmsService : {}", id);
//        return smsServiceRepository.findById(id)
//            .map(smsServiceMapper::toDto);
//    }

}
