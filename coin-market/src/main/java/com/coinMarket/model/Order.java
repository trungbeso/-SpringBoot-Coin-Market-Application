package com.coinMarket.model;

import com.coinMarket.enums.OrderStatus;
import com.coinMarket.enums.OrderType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@ManyToOne
	User user;

	@Column(nullable = false )
	OrderType orderType;

	@Column(nullable = false )
	BigDecimal price;

	LocalDateTime timeStamp = LocalDateTime.now();

	@Column(nullable = false )
	OrderStatus status;

	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
	OrderItem orderItem;
}
