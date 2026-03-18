package com.kh.trip.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.trip.domain.Booking;
import com.kh.trip.domain.User;
import com.kh.trip.domain.UserCoupon;
import com.kh.trip.dto.BookingDTO;
import com.kh.trip.dto.PageRequestDTO;
import com.kh.trip.dto.PageResponseDTO;
import com.kh.trip.repository.BookingRepository;
import com.kh.trip.repository.UserCouponRepository;
import com.kh.trip.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService{
	private final BookingRepository repository;
	private final UserCouponRepository userCouponRepository;
	private final UserRepository userRepository;
	
	@Override
	public Long save(BookingDTO bookingDTO) {
		User user = userRepository.findById(bookingDTO.getUserNo()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 정보입니다.")); 
		UserCoupon userCoupon = userCouponRepository.findById(bookingDTO.getUserCouponNo()).orElseThrow(() -> new IllegalArgumentException("회원이 갖고 있지 않는 쿠폰번호입니다."));
		
		Booking booking = Booking.builder().bookingNo(bookingDTO.getBookingNo()).user(user).userCoupon(userCoupon)
				.checkInDate(bookingDTO.getCheckInDate()).checkOutDate(bookingDTO.getCheckOutDate())
				.guestCount(bookingDTO.getGuestCount()).pricePerNight(bookingDTO.getPricePerNight()).totalPrice(bookingDTO.getTotalPrice())
				.regDate(bookingDTO.getRegDate()).build();
		
		return repository.save(booking).getBookingNo();
	}

	@Override
	public PageResponseDTO<BookingDTO> findByUserId(Long userNo, PageRequestDTO pageRequestDTO) {
		Pageable pageable = PageRequest.of(pageRequestDTO.getPage()-1, pageRequestDTO.getSize(),Sort.by("bookingNo").descending());
		User user = userRepository.findById(userNo).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 정보입니다.")); 
		Page<Booking> result = repository.findByUserId(userNo, pageable);
		
		List<BookingDTO> dtoList = result.get().map(booking -> BookingDTO.builder()
				.bookingNo(booking.getBookingNo()).userNo(userNo).userCouponNo(booking.getUserCoupon().getUserCouponNo())
				.checkInDate(booking.getCheckInDate()).checkOutDate(booking.getCheckOutDate()).guestCount(booking.getGuestCount())
				.pricePerNight(booking.getPricePerNight()).discountAmount(booking.getDiscountAmount()).totalPrice(booking.getTotalPrice())
				.status(booking.getStatus()).requestMessage(booking.getRequestMessage()).regDate(booking.getRegDate()).build())
				.collect(Collectors.toList());
		return PageResponseDTO.<BookingDTO>withAll().dtoList(dtoList).pageRequestDTO(pageRequestDTO).totalCount(result.getTotalElements())
				.build();
	}

//	@Override
//	public List<BookingDTO> findByLodgingId(Long roomNo) {
//		
//		return null;
//	}

	@Override
	public void delete(Long bookingNo) {
		Optional<Booking> result = repository.findById(bookingNo); 
		Booking booking = result.orElseThrow();
		booking.cancel();
		repository.save(booking);
	}

}
