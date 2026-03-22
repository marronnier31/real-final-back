package com.kh.trip.service;

public interface UserAccountService {
	void withdraw(Long userNo);

	void restore(Long userNo);
}
