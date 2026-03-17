package com.kh.trip.domain;

import com.kh.trip.domain.common.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//DB 테이블과 매핑되는 클래스
@Entity

//LODGINGS 테이블과 매핑된다.
@Table(name = "LODGINGS")

@Getter

@NoArgsConstructor(access = AccessLevel.PROTECTED)

@AllArgsConstructor

@Builder

public class Lodging extends BaseTimeEntity {

	@Id

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_lodgings")
	// 기본키 값을 자동 생성
	// Oracle에서는 Sequence

	@SequenceGenerator(name = "seq_lodgings", // JPA에서 사용할 시퀀스 이름
			sequenceName = "SEQ_LODGINGS", // 실제 DB에 있는 시퀀스 이름
			allocationSize = 1 // 1씩 증가
	)

	@Column(name = "LODGING_NO")
	// DB 컬럼 이름과 매핑
	// LODGING_NO 컬럼과 연결된다.

	private Long lodgingNo;
	// 숙소 번호 (Primary Key)
	// DB에서 숙소를 구분하는 고유 ID

	@Column(name = "HOST_NO", nullable = false)
	// 숙소를 등록한 호스트(판매자)의 ID
	// null이면 안 되기 때문에 nullable=false

	private Long hostNo;

	@Column(name = "LODGING_NAME", nullable = false, length = 200)
	// 숙소 이름
	// 최대 200자까지 저장 가능
	// 반드시 값이 있어야 한다.

	private String lodgingName;

	@Column(name = "LODGING_TYPE", nullable = false, length = 50)
	// 숙소 유형
	// 예: HOTEL, PENSION, MOTEL, GUESTHOUSE 등

	private String lodgingType;

	@Column(name = "REGION", nullable = false, length = 100)
	// 숙소 지역
	// 예: 서울, 부산, 제주

	private String region;

	@Column(name = "ADDRESS", nullable = false, length = 300)
	// 숙소 기본 주소
	// 예: 서울시 강남구 테헤란로

	private String address;

	@Column(name = "DETAIL_ADDRESS", length = 300)
	// 상세 주소
	// 예: 101호, 3층

	private String detailAddress;

	@Column(name = "ZIP_CODE", length = 20)
	// 우편번호

	private String zipCode;

	@Column(name = "LATITUDE")
	// 위도 (지도 위치 표시용)

	private Double latitude;

	@Column(name = "LONGITUDE")
	// 경도 (지도 위치 표시용)

	private Double longitude;

	@Column(name = "DESCRIPTION", length = 2000)
	// 숙소 설명
	// 예: 바다가 보이는 호텔입니다.

	private String description;

	@Column(name = "CHECK_IN_TIME", length = 20)
	// 체크인 시간
	// 예: 15:00

	private String checkInTime;

	@Column(name = "CHECK_OUT_TIME", length = 20)
	// 체크아웃 시간
	// 예: 11:00

	private String checkOutTime;

	@Builder.Default
	// Builder 사용 시 기본값 설정
	// Builder로 객체 생성할 때 status 값을 따로 지정하지 않으면 "ACTIVE"가 들어간다.

	@Column(name = "STATUS", nullable = false, length = 20)
	// 숙소 상태
	// 예:
	// ACTIVE → 정상 운영
	// INACTIVE → 비활성화
	// DELETED → 삭제

	private String status = "ACTIVE";
}