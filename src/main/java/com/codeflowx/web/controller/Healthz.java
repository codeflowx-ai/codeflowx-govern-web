package com.codeflowx.web.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Healthz {

	public Healthz() {
		//healthz
	}
	@RequestMapping("/healthz")
	public String healtchek(HttpServletRequest request) {
		//Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		
		return "ok";
	}

}
