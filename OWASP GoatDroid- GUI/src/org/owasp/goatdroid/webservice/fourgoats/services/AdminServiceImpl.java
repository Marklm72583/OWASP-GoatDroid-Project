/**
 * OWASP GoatDroid Project
 * 
 * This file is part of the Open Web Application Security Project (OWASP)
 * GoatDroid project. For details, please see
 * https://www.owasp.org/index.php/Projects/OWASP_GoatDroid_Project
 *
 * Copyright (c) 2012 - The OWASP Foundation
 * 
 * GoatDroid is published by OWASP under the GPLv3 license. You should read and accept the
 * LICENSE before you use, modify, and/or redistribute this software.
 * 
 * @author Jack Mannino (Jack.Mannino@owasp.org https://www.owasp.org/index.php/User:Jack_Mannino)
 * @created 2012
 */
package org.owasp.goatdroid.webservice.fourgoats.services;

import java.util.ArrayList;
import org.owasp.goatdroid.webservice.fourgoats.Constants;
import org.owasp.goatdroid.webservice.fourgoats.Validators;
import org.owasp.goatdroid.webservice.fourgoats.bean.AdminBean;
import org.owasp.goatdroid.webservice.fourgoats.bean.GetUsersAdminBean;
import org.owasp.goatdroid.webservice.fourgoats.dao.AdminDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

	AdminDaoImpl dao;

	public AdminServiceImpl() {
		dao = new AdminDaoImpl();
	}

	public AdminBean deleteUser(String sessionToken, String userName) {

		AdminBean bean = new AdminBean();
		ArrayList<String> errors = new ArrayList<String>();

		try {
			if (!dao.isSessionValid(sessionToken)
					|| !Validators.validateSessionTokenFormat(sessionToken))
				errors.add(Constants.INVALID_SESSION);
			else if (!Validators.validateUserNameFormat(userName))
				errors.add(Constants.USERNAME_FORMAT_INVALID);

			if (errors.size() == 0) {
				/*
				 * If the user has the admin role then we proceed
				 */
				if (dao.isAdmin(sessionToken)) {
					dao.deleteUser(userName);
					bean.setSuccess(true);
				} else {
					errors.add(Constants.NOT_AUTHORIZED);
				}
			}
		} catch (Exception e) {
			errors.add(Constants.UNEXPECTED_ERROR);
		} finally {
			bean.setErrors(errors);
		}
		return bean;
	}

	public AdminBean resetPassword(String sessionToken, String userName,
			String newPassword) {

		AdminBean bean = new AdminBean();
		ArrayList<String> errors = new ArrayList<String>();

		try {
			if (!dao.isSessionValid(sessionToken)
					|| !Validators.validateSessionTokenFormat(sessionToken))
				errors.add(Constants.INVALID_SESSION);
			else if (!Validators.validateUserNameFormat(userName))
				errors.add(Constants.USERNAME_FORMAT_INVALID);
			else if (!Validators.validatePasswordLength(newPassword))
				errors.add(Constants.PASSWORD_FORMAT_INVALID);

			if (errors.size() == 0) {
				/*
				 * If the user has the admin role then we proceed
				 */
				if (dao.isAdmin(sessionToken)) {
					dao.updatePassword(userName, newPassword);
					bean.setSuccess(true);
				} else {
					errors.add(Constants.NOT_AUTHORIZED);
				}
			}
		} catch (Exception e) {
			errors.add(Constants.UNEXPECTED_ERROR);
		} finally {
			bean.setErrors(errors);
		}
		return bean;
	}

	public GetUsersAdminBean getUsers(String sessionToken) {

		GetUsersAdminBean bean = new GetUsersAdminBean();
		ArrayList<String> errors = new ArrayList<String>();
		try {
			if (!dao.isSessionValid(sessionToken)
					|| !Validators.validateSessionTokenFormat(sessionToken))
				errors.add(Constants.INVALID_SESSION);

			if (errors.size() == 0) {
				/*
				 * If the user has the admin role then we proceed
				 */
				if (dao.isAdmin(sessionToken)) {
					bean.setUsers(dao.getUsers());
					bean.setSuccess(true);
				} else {
					errors.add(Constants.NOT_AUTHORIZED);
				}
			}
		} catch (Exception e) {
			errors.add(Constants.UNEXPECTED_ERROR);
		} finally {
			bean.setErrors(errors);
		}
		return bean;
	}
}
