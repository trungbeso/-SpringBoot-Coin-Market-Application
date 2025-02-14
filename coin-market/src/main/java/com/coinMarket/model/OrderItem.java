package com.coinMarket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	double quantity;

	@ManyToOne
	Coin coin;

	double buyPrice;

	double sellPrice;

	@JsonIgnore
	@OneToOne
	Order order;
}
