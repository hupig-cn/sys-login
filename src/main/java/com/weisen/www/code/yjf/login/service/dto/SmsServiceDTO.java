package com.weisen.www.code.yjf.login.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.weisen.www.code.yjf.login.domain.SmsService} entity.
 */
@SuppressWarnings("serial")
public class SmsServiceDTO implements Serializable {

    private Long id;

    private String phone;

    private String code;

    private String type;

    private Long sendtime;

    private Integer number;

    private String result;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSendtime() {
        return sendtime;
    }

    public void setSendtime(Long sendtime) {
        this.sendtime = sendtime;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SmsServiceDTO smsServiceDTO = (SmsServiceDTO) o;
        if (smsServiceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smsServiceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmsServiceDTO{" +
            "id=" + getId() +
            ", phone=" + getPhone() +
            ", code=" + getCode() +
            ", type='" + getType() + "'" +
            ", sendtime=" + getSendtime() +
            ", number=" + getNumber() +
            ", result='" + getResult() + "'" +
            "}";
    }
}
