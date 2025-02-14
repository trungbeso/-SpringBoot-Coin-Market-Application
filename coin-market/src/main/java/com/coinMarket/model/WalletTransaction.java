package com.coinMarket.model;

import com.coinMarket.enums.WalletTransactionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WalletTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@ManyToOne
	Wallet wallet;

	WalletTransactionType type;

	LocalDate date;

	String transferId;

	String purpose;

	Long amount;
}
