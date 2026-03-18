package com.kh.trip.service;

import java.util.List;

import com.kh.trip.dto.BookingDTO;
import com.kh.trip.dto.PageRequestDTO;

public interface BookingService {

	Long save(BookingDTO bookingDTO);

	PageResponseDTO<BookingDTO> findByUserId(Long userNo, PageRequestDTO pageRequestDTO);

//	List<BookingDTO> findByLodgingId(Long roomNo);

	void delete(Long bookingNo);

}
