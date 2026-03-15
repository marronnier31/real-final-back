package com.kh.trip.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "INQUIRY_MESSAGES")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class InquiryMessage {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_INQUIRY_MESSAGES")
	@SequenceGenerator(name = "SEQ_INQUIRY_MESSAGES", sequenceName = "SEQ_INQUIRY_MESSAGES", allocationSize = 1)
	@Column(name = "MESSAGE_NO")
	private Long messageNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INQUIRY_ROOM_NO", nullable = false)
	private InquiryRoom inquiryRoom;
	
	
	
	
}
