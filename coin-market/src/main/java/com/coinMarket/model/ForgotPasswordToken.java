package com.coinMarket.model;

import com.coinMarket.enums.VerificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ForgotPasswordToken {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	String id;

	String otp;

	@OneToOne
	User user;

	VerificationType verificationType;

	String sendTo;


}
