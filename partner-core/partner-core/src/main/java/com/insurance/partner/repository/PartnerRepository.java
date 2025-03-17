package com.insurance.partner.repository;

import com.insurance.partner.entity.Partner;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PartnerRepository extends CrudRepository<Partner, UUID> {
    Optional<Partner> findByPartnerNo(String partnerNo);

    @Query("SELECT * FROM partner " +
            "WHERE (first_name ILIKE '%' || :search || '%' OR last_name ILIKE '%' || :search || '%' OR partner_no ILIKE '%' || :search || '%') " +
            "ORDER BY partner_no_number " +
            "LIMIT :limit OFFSET :offset")
    List<Partner> findBySearch(@Param("search") String search,
                               @Param("limit") int limit,
                               @Param("offset") int offset);
}