package com.weisen.www.code.yjf.login.service.dto;

public class Rewrite_200_PayPasswordCodeDTO {
	
	private String login;

    private String vertifyCode;
    
    public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getVertifyCode() {
        return vertifyCode;
    }

    public void setVertifyCode(String vertifyCode) {
        this.vertifyCode = vertifyCode;
    }
}
