package com.kh.trip.domain.common;

import java.time.LocalDateTime;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class BaseTimeEntity {

	private LocalDateTime regDate;

	private LocalDateTime updDate;

	@PrePersist
	public void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		this.regDate = now;
		this.updDate = now;
	}

	@PreUpdate
	public void preUpdate() {
		this.updDate = LocalDateTime.now();
	}

}
