package com.ssafy.msg.user.exception;

public class TokenInvalidException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public TokenInvalidException() {
		super("InvalidTokenException");
	}
}
