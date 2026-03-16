package com.kh.trip.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kh.trip.domain.Event;
import com.kh.trip.dto.EventDTO;
import com.kh.trip.dto.PageRequestDTO;
import com.kh.trip.dto.PageResponseDTO;
import com.kh.trip.repository.EventRepository;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService{
	private final EventRepository eventRepository;
	
	@Override
	public PageResponseDTO<EventDTO> list(PageRequestDTO pageRequestDTO) {
	    
	    Pageable pageable = PageRequest.of(
	            pageRequestDTO.getPage() - 1, 
	            pageRequestDTO.getSize(),
	            Sort.by("eventNo").descending()
	    );

	    Page<Event> result = eventRepository.findAll(pageable);

	    List<EventDTO> dtoList = result.getContent().stream().map(event -> {
	    	return EventDTO.builder()
	                .eventNo(event.getEventNo())
	                .title(event.getTitle())
	                .content(event.getContent())
	                .thumbnailUrl(event.getThumbnailUrl())
	                .startDate(event.getStartDate())
	                .build();
	    }).collect(Collectors.toList());

	    long totalCount = result.getTotalElements();
	    
	    return PageResponseDTO.<EventDTO>withAll()
	            .dtoList(dtoList) // 위에서 만든 dtoList를 담아줍니다
	            .totalCount(totalCount)
	            .pageRequestDTO(pageRequestDTO)
	            .build();
	}

}
