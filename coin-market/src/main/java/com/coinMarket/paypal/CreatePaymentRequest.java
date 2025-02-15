package com.coinMarket.paypal;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePaymentRequest {

	Double amount;
	String currency;
	String description;
	String cancelUrl;
	String successUrl;
}
