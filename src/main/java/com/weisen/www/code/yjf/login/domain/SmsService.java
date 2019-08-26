package com.weisen.www.code.yjf.login.domain;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SmsService.
 */
@Entity
@Table(name = "sms_service")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SmsService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "code")
    private String code;

    @Column(name = "jhi_type")
    private String type;

    @Column(name = "sendtime")
    private Long sendtime;

    @Column(name = "jhi_number")
    private Integer number;

    @Column(name = "result")
    private String result;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public SmsService phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public SmsService code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public SmsService type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSendtime() {
        return sendtime;
    }

    public SmsService sendtime(Long sendtime) {
        this.sendtime = sendtime;
        return this;
    }

    public void setSendtime(Long sendtime) {
        this.sendtime = sendtime;
    }

    public Integer getNumber() {
        return number;
    }

    public SmsService number(Integer number) {
        this.number = number;
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getResult() {
        return result;
    }

    public SmsService result(String result) {
        this.result = result;
        return this;
    }

    public void setResult(String result) {
        this.result = result;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SmsService)) {
            return false;
        }
        return id != null && id.equals(((SmsService) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SmsService{" +
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
