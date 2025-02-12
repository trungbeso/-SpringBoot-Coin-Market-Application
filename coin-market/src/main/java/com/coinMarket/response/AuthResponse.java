package com.coinMarket.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

	private String jwt;

	private boolean status;

	private String message;

	private boolean isTwoFactorAuthEnabled;

	private String session;
}
