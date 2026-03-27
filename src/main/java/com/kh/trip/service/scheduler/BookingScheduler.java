package com.kh.trip.service.scheduler;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kh.trip.domain.enums.BookingStatus;
import com.kh.trip.repository.BookingRepository;
import com.kh.trip.service.BookingService;

@Component
public class BookingScheduler {
	@Autowired
	private BookingRepository repository;
	
	@Scheduled(cron = "0 1 11 * * *")
	@Transactional
	public void updateBookingStatus() {
		System.out.println("스케줄러 작동 중!");
		LocalDateTime today = LocalDateTime.now();
		repository.updateStatusForCheckout(today, BookingStatus.COMPLETED, BookingStatus.CONFIRMED);
		
	}
	
}
