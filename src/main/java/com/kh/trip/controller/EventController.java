package com.kh.trip.controller;

import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.trip.dto.EventDTO;
import com.kh.trip.dto.PageRequestDTO;
import com.kh.trip.dto.PageResponseDTO;
import com.kh.trip.service.EventService;
import com.kh.trip.util.CustomFileUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event")
public class EventController {

	private final EventService service;
	private final CustomFileUtil fileUtil; 
	
	// 가로로 보이는 이벤트 리스트
	@GetMapping("/list")
	public PageResponseDTO<EventDTO> findAll(PageRequestDTO pageRequestDTO) {
		log.info(pageRequestDTO);
		return service.findAll(pageRequestDTO);
	}

	@GetMapping("/view/{fileName}")
	public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName) {
		return fileUtil.getFile(fileName);
	}
	
	// 이벤트변경,새로운거 저장
	@PostMapping("/")
	public Map<String, Long> save(EventDTO eventDTO) {
		log.info("EventDTO:" + eventDTO);
		MultipartFile file = eventDTO.getFile();
		String uploadFileName = fileUtil.saveFile(file);
		eventDTO.setThumbnailUrl(uploadFileName);
		
		Long eno = service.save(eventDTO);
		return Map.of("ENO", eno);
	}

	// 수정
	@PutMapping("/{eno}")
	public Map<String, String> update(@PathVariable Long eno,  EventDTO eventDTO) {
		eventDTO.setEventNo(eno);
		log.info("Update:" + eventDTO);
		
		EventDTO oldEventDTO = service.findById(eno);
		String oldFileName = oldEventDTO.getThumbnailUrl();
		MultipartFile file = eventDTO.getFile();
		String currentUploadFileName = null;
		if(file != null && !file.isEmpty()) currentUploadFileName = fileUtil.saveFile(file);
		String uploadedFileName = eventDTO.getThumbnailUrl();
		if(currentUploadFileName != null && !currentUploadFileName.isEmpty()) uploadedFileName = currentUploadFileName;
		eventDTO.setThumbnailUrl(uploadedFileName);
		
		service.update(eventDTO);
		
		if(oldFileName != null && !oldFileName.isEmpty() && !oldFileName.equals(uploadedFileName)) {
			fileUtil.deleteFile(oldFileName);
		}
		return Map.of("RESULT", "SUCCESS");
	}

	// 삭제
	@DeleteMapping("/{eno}")
	public Map<String, String> delete(@PathVariable Long eno) {
		log.info("Delete: " + eno);
		String oldFileName = service.findById(eno).getThumbnailUrl();
		service.delete(eno);
		fileUtil.deleteFile(oldFileName);
		return Map.of("RESULT", "SUCCESS");
	}
	//하나의 이벤트만 가져옴
		@GetMapping("/{eno}")
		public EventDTO findById(@PathVariable Long eno) {
			return service.findById(eno);
		}
}