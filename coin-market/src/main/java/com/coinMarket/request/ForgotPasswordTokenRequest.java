package com.coinMarket.request;

import com.coinMarket.enums.VerificationType;
import lombok.Data;

@Data
public class ForgotPasswordTokenRequest {
	private String sendTo;

	private String otp;

	private VerificationType verificationType;

}
