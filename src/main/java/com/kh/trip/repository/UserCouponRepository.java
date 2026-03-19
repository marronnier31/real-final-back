package com.kh.trip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kh.trip.domain.UserCoupon;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long>{
	@Query("select uc from UserCoupon uc where uc.user.userNo = :userNo and uc.coupon.couponNo = :couponNo")
	boolean existenceCheck(@Param("userNo") Long userNo,@Param("couponNo") Long couponNo);
}
