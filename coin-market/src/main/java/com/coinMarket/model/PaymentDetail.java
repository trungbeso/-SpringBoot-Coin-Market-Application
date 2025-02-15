package com.coinMarket.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	String accountNumber;

	String accountHolderName;

	String ifsc;

	String bankName;

	@OneToOne
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	User user;
}
