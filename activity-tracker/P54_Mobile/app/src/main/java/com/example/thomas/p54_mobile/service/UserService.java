package com.example.thomas.p54_mobile.service;

import com.example.thomas.p54_mobile.exception.HttpException;
import com.example.thomas.p54_mobile.model.TokenInfo;
import com.example.thomas.p54_mobile.model.User;
import com.example.thomas.p54_mobile.model.UserCredentials;
import com.example.thomas.p54_mobile.model.UserModify;
import com.example.thomas.p54_mobile.model.UserRegister;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UserService
{
	private static Date expire;
	private static User user;
	
	public static boolean isLogged()
	{
		return user != null && expire.after(Calendar.getInstance().getTime());
	}

	public static User identity()
	{
		if(!isLogged())
			return null;
		return user;
	}

	public static User refreshIdentity() throws HttpException
	{
		user = RestService.get("/member/user/whoami",User.class);
		return user;
	}

	public static User signin(UserCredentials credentials) throws HttpException
	{
		TokenInfo info = RestService.post("/user/signin",credentials,TokenInfo.class);
		RestService.setHeader("Authorization","Bearer " + info.getToken());
		user = RestService.get("/member/user/whoami",User.class);
		expire = info.getExpire();
		return user;
	}

	public static User register(UserRegister register) throws HttpException
	{
		RestService.post("/user/register",register,User.class);
		return signin(new UserCredentials(register.getLogin(),register.getPassword()));
	}

	public static User modifyUser(UserModify modify) throws HttpException
	{
		RestService.put("/member/user",modify,User.class);
		return signin(new UserCredentials(modify.getLogin(),modify.getPassword()));
	}
}
