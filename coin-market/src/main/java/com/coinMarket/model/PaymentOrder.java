package com.coinMarket.model;

import com.coinMarket.enums.PaymentMethod;
import com.coinMarket.enums.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	Long amount;

	PaymentOrderStatus status;

	PaymentMethod method;

	@ManyToOne
	User user;
}
