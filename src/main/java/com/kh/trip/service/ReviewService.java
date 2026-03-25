package com.kh.trip.service;

import java.util.List;

import com.kh.trip.dto.ReviewCreateDTO;
import com.kh.trip.dto.ReviewStatsDTO;
import com.kh.trip.dto.ReviewSummaryDTO;
import com.kh.trip.dto.ReviewUpdateDTO;

public interface ReviewService {

	// 리뷰 작성
	ReviewSummaryDTO createReview(Long loginUserNo, ReviewCreateDTO reviewCreateDTO);

	// 리뷰 수정
	ReviewSummaryDTO updateReview(Long loginUserNo, Long reviewNo, ReviewUpdateDTO reviewUpdateDTO);

	// 리뷰 삭제
	void deleteReview(Long loginUserNo, Long reviewNo);

	// 숙소별 리뷰 목록 조회
	List<ReviewSummaryDTO> getReviewsByLodging(Long lodgingNo);

	// 숙소별 리뷰 통계 조회
	ReviewStatsDTO getReviewStatsByLodging(Long lodgingNo);

}
