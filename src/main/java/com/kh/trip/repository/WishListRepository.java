package com.kh.trip.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kh.trip.domain.Lodging;
import com.kh.trip.domain.User;
import com.kh.trip.domain.WishList;


public interface WishListRepository extends JpaRepository<WishList, Long> {
	Optional<WishList> findByUserAndLodging(User user, Lodging lodging);

	@EntityGraph(attributePaths = { "lodging", "lodging.imageList" })
	@Query("select w from WishList w where w.user.userNo = :userNo order by w.regDate desc")
	java.util.List<WishList> findMypageWishlist(@Param("userNo") Long userNo);
}
