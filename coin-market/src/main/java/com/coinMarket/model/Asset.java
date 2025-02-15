package com.coinMarket.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Asset {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	double quantity;

	double buyPrice;

	@ManyToOne
	Coin coin;

	@ManyToOne
	User user;
}
