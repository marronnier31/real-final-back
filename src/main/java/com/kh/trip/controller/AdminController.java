package com.kh.trip.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.kh.trip.domain.User;
import com.kh.trip.domain.UserRole;
import com.kh.trip.repository.UserRepository;
import com.kh.trip.repository.UserRoleRepository;
import com.kh.trip.security.AuthUserPrincipal;
import com.kh.trip.service.UserAccountService;

import lombok.RequiredArgsConstructor;
import com.kh.trip.repository.UserRepository;
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

	private final UserRepository userRepository;
	private final UserRoleRepository userRoleRepository;
	private final UserAccountService userAccountService;

	@GetMapping("/users/withdrawn")
	public List<Map<String, Object>> getWithdrawnUsers(@AuthenticationPrincipal AuthUserPrincipal principal) {
		requireAdmin(principal);
		return userRepository.findByEnabled("0").stream().map(this::toUserMap).toList();
	}

	@PatchMapping("/users/{id}/restore")
	public Map<String, String> restoreUser(@PathVariable Long id,
			@AuthenticationPrincipal AuthUserPrincipal principal) {
		requireAdmin(principal);
		userAccountService.restore(id);
		return Map.of("result", "SUCCESS");
	}

	private void requireAdmin(AuthUserPrincipal principal) {
		if (principal == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication is required.");
		}

		boolean isAdmin = principal.getRoleNames() != null && principal.getRoleNames().contains("ROLE_ADMIN");

		if (!isAdmin) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin access is required.");
		}
	}

	private Map<String, Object> toUserMap(User user) {
		List<UserRole> roles = userRoleRepository.findByUserNo(user.getUserNo());
		String role = roles.isEmpty() ? "" : roles.get(0).getRoleCode().replace("ROLE_", "");
		String status = "1".equals(user.getEnabled()) ? "ACTIVE" : "WITHDRAWN";
		String createdAt = user.getRegDate() != null ? user.getRegDate().toLocalDate().toString() : "";

		return Map.of("userId", user.getUserNo(), "name", user.getUserName(), "email", user.getEmail(), "role", role,
				"status", status, "createdAt", createdAt);
	}
}
