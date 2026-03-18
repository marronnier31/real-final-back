package com.kh.trip.controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.trip.dto.BookingDTO;
import com.kh.trip.dto.PageRequestDTO;
import com.kh.trip.dto.PageResponseDTO;
import com.kh.trip.dto.UserDTO;
import com.kh.trip.security.AuthUserPrincipal;
import com.kh.trip.service.BookingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;



@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/booking")
public class BookingController {
	private final BookingService service;
	
	@PostMapping("/")
	public Map<String, Long> save(@RequestBody BookingDTO bookingDTO) {
		log.info("save()" + bookingDTO);
		Long bno = service.save(bookingDTO);
		return Map.of("result",bno);
	}
	
	@GetMapping("/userlist")
	public PageResponseDTO<BookingDTO> findByUserId(@AuthenticationPrincipal AuthUserPrincipal principal,PageRequestDTO pageRequestDTO) {
		log.info("findByUserId() = ");
		if (principal == null) {
	        throw new RuntimeException("인증 정보가 없습니다. 다시 로그인해 주세요.");
	    }
		return service.findByUserId(principal.getUserNo(),pageRequestDTO);
	}
	
	@GetMapping("/lodginglist")
	public List<BookingDTO> findByLodgingId(@RequestBody BookingDTO bookingDTO) {
		log.info("findByLodgingId() = " + bookingDTO);
		return null;
	}
	
	@DeleteMapping("/{bno}")
	public Map<String, String> delete(@PathVariable(name = "bno") Long bookingNo){
		log.info("findByUserId() = " + bookingNo);
		service.delete(bookingNo);
		return Map.of("result", "SUCCESS");
	}
}
