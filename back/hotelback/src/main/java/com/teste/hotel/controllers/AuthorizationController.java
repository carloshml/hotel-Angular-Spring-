package com.teste.hotel.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationController {
	@GetMapping("/authorization")
	public ResponseEntity authorization() {
		var authorizationOk = new AuthorizationOk();
		return ResponseEntity.ok(authorizationOk);
	}
	
	@PostMapping("/notification")
	public ResponseEntity notification() {
		var authorizationOk = new AuthorizationOk();
		return ResponseEntity.ok(authorizationOk);
	}

	public class AuthorizationOk {

		public boolean authorized = true;

		public AuthorizationOk() {

		}

		public boolean isAuthorized() {
			return authorized;
		}

		public void setAuthorized(boolean authorized) {
			this.authorized = authorized;
		}

	}
}
