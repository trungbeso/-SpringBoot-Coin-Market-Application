package com.coinMarket.model;

import com.coinMarket.enums.WithdrawalStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Withdraw {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	WithdrawalStatus status;

	Long amount;

	@ManyToOne
	User user;

	LocalDateTime date = LocalDateTime.now();
}
