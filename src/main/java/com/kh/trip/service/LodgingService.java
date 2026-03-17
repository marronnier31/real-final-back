package com.kh.trip.service;

import com.kh.trip.domain.Lodging;

public interface LodgingService {

	// 숙소 등록
	public Lodging createLodging(Lodging lodging);

	// 숙소 단건 조회
	public Lodging getLodging(Long lodgingNo);

}
