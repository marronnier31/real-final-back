package com.kh.trip.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.kh.trip.dto.UserUpdateRequestDTO;
import com.kh.trip.dto.WithdrawRequestDTO;
import com.kh.trip.repository.UserRepository;
import com.kh.trip.security.AuthUserPrincipal;
import com.kh.trip.service.UserAccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
	private final UserRepository userRepository;
	private final UserAccountService userAccountService;

	@PatchMapping("/{id}") // PATCH 요청 처리 (부분 업데이트)
	public Map<String, String> updateUser(@PathVariable Long id, // URL 경로에서 사용자 ID 추출
			@Valid @RequestBody UserUpdateRequestDTO request, // 요청 바디(JSON) 검증 후 DTO 매핑
			@AuthenticationPrincipal AuthUserPrincipal principal) // 인증된 사용자 정보 주입
	{
		// 인증 정보 확인 (로그인 여부)
		AuthUserPrincipal authUser = requirePrincipal(principal);

		// 요청한 id와 로그인한 사용자 id가 다르면 권한 없음 (403)
		if (!authUser.getUserNo().equals(id)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 정보에만 접근할수 있습니다.");
		}

		// DB에서 사용자 조회, 없으면 404 반환
		var user = userRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
		// 사용자 이름 변경
		user.changeName(request.getName());
		// 변경된 사용자 정보 저장
		userRepository.save(user);
		// 성공 응답 반환
		return Map.of("result", "SUCCESS");
	}

	@PatchMapping("/{id}/withdraw")
	public Map<String, String> withdraw(
			// URL 경로에서 {id} 값을 Long 타입으로 매핑
			@PathVariable Long id,
			// 요청 본문을 WithdrawRequestDTO로 매핑하고 유효성 검사 수행
			@Valid @RequestBody WithdrawRequestDTO request,
			// 인증된 사용자 정보를 가져옴
			@AuthenticationPrincipal AuthUserPrincipal principal) {
		// principal이 null일 경우 예외 처리, 인증된 사용자 정보 필수
		AuthUserPrincipal authUser = requirePrincipal(principal);
		// 요청한 id와 인증된 사용자의 id가 다르면 권한 없음 예외 발생
		if (!authUser.getUserNo().equals(id)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only withdraw your own account.");
		}
		// 사용자 계정 탈퇴 처리 서비스 호출
		userAccountService.withdraw(id);
		return Map.of("result", "SUCCESS");
	}

	// 인증 정보가 없을 경우 401 반환
	private AuthUserPrincipal requirePrincipal(AuthUserPrincipal principal) {
		if (principal == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "접근 권한이 필요합니다");
		}
		return principal;
	}

}
