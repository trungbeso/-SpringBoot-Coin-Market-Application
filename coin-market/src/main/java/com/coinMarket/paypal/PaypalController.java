package com.coinMarket.paypal;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("${api.prefix}/paypal")
public class PaypalController {

	IPaypalService paypalService;

	@PostMapping
	public ResponseEntity<?> createPayment(@RequestBody CreatePaymentRequest request) {
		try {
			String backendSuccessUrl = buildBackendUrl("api/v1/paypal/execute", request.getSuccessUrl());
			String backendCancelUrl = buildBackendUrl("api/v1/paypal/cancel", request.getCancelUrl());

			Payment payment = paypalService.createPayment(
				  request.getAmount(),
				  request.getCurrency(),
				  "paypal",
				  "sale",
				  request.getDescription(),
				  backendCancelUrl,
				  backendSuccessUrl
			);

			String approvalUrl = payment.getLinks().stream()
				  .filter(link -> "approve_url".equalsIgnoreCase(link.getRel()))
				  .findFirst()
				  .orElseThrow(() -> new RuntimeException("Approval URL not found"))
				  .getHref();

			return ResponseEntity.ok(Collections.singletonMap("approval_url", approvalUrl));
		} catch (PayPalRESTException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				  .body(Collections.singletonMap("error", e.getMessage()));
		}
	}

	@GetMapping("/execute")
	public ResponseEntity<Void> executePayment(
		  @RequestParam("paymentId") String paymentId,
		  @RequestParam("payerId") String payerId,
		  @RequestParam("redirectUrl") String redirectUrl) {
		try {
			Payment executedPayment = paypalService.executePayment(paymentId, payerId);
			String statusUrl = buildFrontendRedirectUrl(redirectUrl, "success", executedPayment.getId());
			return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(statusUrl)).build();
		} catch (PayPalRESTException e) {
			String errorUrl = buildFrontendRedirectUrl(redirectUrl, "error", paymentId);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).location(URI.create(errorUrl)).build();
		}
	}

	@GetMapping("/cancel")
	public ResponseEntity<Void> cancelPayment(@RequestParam String redirectUrl) {
		String cancelUrl = UriComponentsBuilder
			  .fromUriString(redirectUrl)
			  .queryParam("status", "cancelled")
			  .build().toUriString();
		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(cancelUrl)).build();
	}

	private String buildBackendUrl(String path, String frontendUrl) {
		return ServletUriComponentsBuilder.fromCurrentContextPath()
			  .path(path)
			  .queryParam("redirectUrl", frontendUrl)
			  .build()
			  .encode()
			  .toUriString();
	}

	private String buildFrontendRedirectUrl(String baseUrl, String status, String param) {
		return UriComponentsBuilder.fromUriString(baseUrl)
			  .queryParam("status", status)
			  .queryParam(status.equals("success") ? "paymentId" : "error", param)
			  .build()
			  .encode()
			  .toUriString();
	}


}
