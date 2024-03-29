package com.ssafy.msg.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Oauth2Dto {
	private String nickname;
	private String emailId;
	private String provider;
	private String refreshToken;
}
