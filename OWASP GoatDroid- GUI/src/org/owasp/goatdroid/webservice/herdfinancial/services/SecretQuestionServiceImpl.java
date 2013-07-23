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
package org.owasp.goatdroid.webservice.herdfinancial.services;

import java.util.ArrayList;
import org.owasp.goatdroid.webservice.herdfinancial.Constants;
import org.owasp.goatdroid.webservice.herdfinancial.Validators;
import org.owasp.goatdroid.webservice.herdfinancial.bean.SecretQuestionBean;
import org.owasp.goatdroid.webservice.herdfinancial.dao.SecretQuestionDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecretQuestionServiceImpl implements SecretQuestionService {

	SecretQuestionDaoImpl dao;

	public SecretQuestionServiceImpl() {
		dao = new SecretQuestionDaoImpl();
	}

	public SecretQuestionBean setSecretQuestions(int sessionToken,
			String answer1, String answer2, String answer3) {

		SecretQuestionBean bean = new SecretQuestionBean();
		ArrayList<String> errors = new ArrayList<String>();
		LoginServiceImpl loginService = new LoginServiceImpl();
		if (!loginService.isSessionValid(sessionToken))
			errors.add(Constants.SESSION_EXPIRED);

		else if (!Validators.validateSecretQuestionAnswers(answer1, answer2,
				answer3))
			errors.add(Constants.SECRET_QUESTION_ANSWER_TOO_LONG);

		try {
			if (errors.size() == 0) {
				dao.updateAnswers(sessionToken, answer1, answer2, answer3);
				bean.setSuccess(true);
			}
		} catch (Exception e) {
			errors.add(Constants.UNEXPECTED_ERROR);
		} finally {
			bean.setErrors(errors);
		}
		return bean;
	}
}
