package com.coinMarket.model;

import com.coinMarket.enums.VerificationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerificationCode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	String otp;

	@OneToOne
	User user;

	String email;

	String mobile;

	VerificationType verificationType;
}
