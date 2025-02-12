package com.coinMarket.model;

import com.coinMarket.enums.USER_ROLE;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	String fullName;

	String email;

	//ignore when get user data
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	String password;

	@Embedded
	TwoFactorAuth twoFactorAuth = new TwoFactorAuth();

	USER_ROLE role = USER_ROLE.CUSTOMER;


}
