package com.kh.trip.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kh.trip.domain.HostProfiles;

public interface HostProfilesRepository extends JpaRepository<HostProfiles, Long> {
	Optional<HostProfiles> findByUser_UserNo(Long userNo);
}
