package com.kh.trip.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kh.trip.domain.Event;
import com.kh.trip.domain.enums.EventStatus;

public interface EventRepository extends JpaRepository<Event, Long>{

	@Query("select e from Event e where e.status != :status")
	Page<Event> findAll(Pageable pageable,@Param("status")EventStatus status);
	
	boolean existsByTitle(String title);

}
