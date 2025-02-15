package com.coinMarket.configuration;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PaypalConfiguration {

	@Value("${paypal.client.id}")
	private String clientId;

	@Value("${paypal.client.secret}")
	private String clientSecret;

	@Value("${paypal.mode}")
	private String mode;

//	@Bean
//	public Map<String, String> paypalSdkConfiguration() {
//		Map<String, String> configMap = new HashMap<>();
//		configMap.put("paypal.client.id", clientId);
//		configMap.put("paypal.client.secret", clientSecret);
//		configMap.put("paypal.mode", mode);
//		return configMap;
//	}
//
//	@Bean
//	public OAuthTokenCredential getOAuthTokenCredential() {
//		return new OAuthTokenCredential(clientId, clientSecret, paypalSdkConfiguration());
//	}

	@Bean
	public APIContext apiContext() throws PayPalRESTException {
		return new APIContext(clientId, clientSecret, mode);
	}
}
