package com.kh.trip.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.trip.dto.LodgingDTO;
import com.kh.trip.service.LodgingService;
import com.kh.trip.util.CustomFileUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lodgings")
@RequiredArgsConstructor
public class LodgingController {

	private final LodgingService lodgingService;
	private final CustomFileUtil fileUtil;

	@PostMapping("/")
	@ResponseStatus(HttpStatus.CREATED)
	public LodgingDTO createLodging(@ModelAttribute LodgingDTO lodgingDTO) {
		List<MultipartFile> files = lodgingDTO.getFiles();
		List<String> uploadFileNames = fileUtil.saveFiles(files);
		lodgingDTO.setUploadFileNames(uploadFileNames);
		return lodgingService.createLodging(lodgingDTO);
	}

	@GetMapping("/view/{fileName}")
	public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName) {
		return fileUtil.getFile(fileName);
	}
	
	@GetMapping("/{lodgingNo}")
	public LodgingDTO getLodging(@PathVariable Long lodgingNo) {
		return lodgingService.getLodging(lodgingNo);
	}

	@GetMapping("/list")
	public List<LodgingDTO> getAllLodgings() {
		return lodgingService.getAllLodgings();
	}

	@GetMapping("/region")
	public List<LodgingDTO> getLodgingsByRegion(@RequestParam String region) {
		return lodgingService.getLodgingsByRegion(region);
	}

	@GetMapping("/search")
	public List<LodgingDTO> searchLodgingsByName(@RequestParam String keyword) {
		return lodgingService.searchLodgingsByName(keyword);
	}

	@PatchMapping("/{lodgingNo}")
	public LodgingDTO updateLodging(@PathVariable Long lodgingNo, @ModelAttribute LodgingDTO lodgingDTO) {
		lodgingDTO.setLodgingNo(lodgingNo);

		// 현재 있는 파일의정보 pno= 120L pname="aaa" pprice=10000 pdesc="aaaa" pfile=[]
		// imgStr="sjfk_aaaa.jpg"
		LodgingDTO oldLodgingDTO = lodgingService.getLodgingDetail(lodgingNo);

		// 기존파일들 (데이터베이스에 존재하는 파일들-수정 과정에서 삭제되었을 수 있음)
		// sjfk_aaaa.jpg
		List<String> oldFileNames = oldLodgingDTO.getUploadFileNames();

		// 새로 업로드 해야 하는 파일들(0, 0), (X, 0), (X, X), (0, X)
		// bbb.jpg
		// bbb.jpg
		// 새로된파일없음
		// 새로된파일없음
		List<MultipartFile> files = lodgingDTO.getFiles();

		// 새로 업로드되어서 만들어진 파일 이름들
		// bbb.jpg 내부폴더에저장하고, safsa_bbb,jpg 리턴
		// bbb.jpg 내부폴더에저장하고, safsb_bbb,jpg 리턴
		// 새로운파일이 없어서 null 리턴
		// 새로운파일이 없어서 null 리턴
		List<String> currentUploadFileNames = null;
		if (files != null && !files.get(0).isEmpty()) {
			currentUploadFileNames = fileUtil.saveFiles(files);
		}

		// 화면에서 변화 없이 계속 유지된 파일들
		// sjfk_aaaa.jpg
		// sjfk_aaaa.jpg (삭제)
		// sjfk_aaaa.jpg (삭제)
		// sjfk_aaaa.jpg
		List<String> uploadedFileNames = lodgingDTO.getUploadFileNames();

		// 유지되는 파일들 + 새로 업로드된 파일 이름들이 저장해야 하는 파일 목록이 됨
		if (currentUploadFileNames != null && !currentUploadFileNames.isEmpty()) {
			// {기존: sjfk_aaaa.jpg, 추가: safsa_bbb,jpg}
			// {기존삭제: sjfk_aaaa.jpg, 추가: safsb_bbb,jpg}
			// 이문장이 실행안됨.
			// 이문장이 실행안됨.
			uploadedFileNames.addAll(currentUploadFileNames);
		}
		// 수정 작업
		LodgingDTO newDTO = lodgingService.updateLodging(lodgingNo, lodgingDTO);

		// sjfk_aaaa.jpg
		if (oldFileNames != null && !oldFileNames.isEmpty()) {
			// 지워야 하는 파일 목록 찾기
			// 예전 파일들 중에서 지워져야 하는 파일이름들
			List<String> removeFiles = oldFileNames.stream()
					.filter(fileName -> uploadedFileNames.indexOf(fileName) == -1).collect(Collectors.toList());
			// 실제 파일 삭제
			fileUtil.deleteFiles(removeFiles);
		}
		return newDTO;
	}

	@DeleteMapping("/{lodgingNo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteLodging(@PathVariable Long lodgingNo) {
		// 삭제해야 할 파일들 알아내기
				List<String> oldFileNames = lodgingService.getLodgingDetail(lodgingNo).getUploadFileNames();
				//테이블 flag = true update
				lodgingService.deleteLodging(lodgingNo);
				
				//기존이미지는 삭제함. 
				fileUtil.deleteFiles(oldFileNames);
	}

	@GetMapping("/{lodgingNo}/detail") // 상세보기 전용 API
	public LodgingDTO getLodgingDetail(@PathVariable Long lodgingNo) {
		return lodgingService.getLodgingDetail(lodgingNo); // 상세 DTO 반환
	}

}
