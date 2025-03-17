package com.insurance.partner.service;

import com.insurance.partner.entity.Partner;
import com.insurance.partner.repository.PartnerRepository;
import io.micrometer.observation.annotation.Observed;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartnerService {


    private final PartnerRepository partnerRepository;

    public PartnerService(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    public Optional<Partner> findPartner(String partnerNo) {
        return partnerRepository.findByPartnerNo(partnerNo);
    }

    @Observed(contextualName = "find partners", name = "PartnerService")
    public List<Partner> findPartners(String search, int limit, int offset) {
        return partnerRepository.findBySearch(search, limit, offset);
    }
}