package com.kh.trip.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.trip.domain.enums.LodgingStatus;
import com.kh.trip.domain.enums.LodgingType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

public class LodgingDTO {

	private Long lodgingNo;
	private Long hostNo;
	private String lodgingName;
	private LodgingType lodgingType;
	private String region;
	private String address;
	private String detailAddress;
	private String zipCode;
	private Double latitude;
	private Double longitude;
	private String description;
	private String checkInTime;
	private String checkOutTime;
	private LodgingStatus status;
	@Builder.Default 
	private List<MultipartFile> files = new ArrayList<MultipartFile>(); 

	@Builder.Default 
	private List<String> uploadFileNames = new ArrayList<String>();
	private List<RoomSummaryDTO> roomDTO;

}
