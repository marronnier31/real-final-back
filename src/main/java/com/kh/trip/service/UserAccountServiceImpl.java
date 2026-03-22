package com.kh.trip.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.trip.domain.User;
import com.kh.trip.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAccountServiceImpl implements UserAccountService {

	private final UserRepository userRepository;

	@Override
	public void withdraw(Long userNo) {
		User user = userRepository.findById(userNo).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

		user.changeEnabled("0");
	}

	@Override
	public void restore(Long userNo) {
		User user = userRepository.findById(userNo).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

		user.changeEnabled("1");

	}

}
