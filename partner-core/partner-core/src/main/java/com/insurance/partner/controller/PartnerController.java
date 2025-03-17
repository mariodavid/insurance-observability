package com.insurance.partner.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insurance.partner.entity.Partner;
import com.insurance.partner.entity.PartnerMdc;
import com.insurance.partner.service.PartnerService;

@RestController
@RequestMapping("/api/partners")
public class PartnerController {

    private static final Logger log = LoggerFactory.getLogger(PartnerController.class);
    private final PartnerService partnerService;

    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }


    @GetMapping
    public ResponseEntity<List<PartnerResponse>> findPartners(
            @RequestParam("search") String search,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        try {
            log.info("Request to search partners received.");

            // Optional: Berechne limit und offset basierend auf page und pageSize, falls benötigt
            // int calculatedOffset = (page - 1) * pageSize;
            // limit = pageSize;
            // offset = calculatedOffset;

            List<Partner> partners = partnerService.findPartners(search, limit, offset);

            if (partners.isEmpty()) {
                log.warn("No partners found for search parameter: {}", search);
                return ResponseEntity.notFound().build();
            }

            List<PartnerResponse> responses = partners.stream()
                    .map(partner -> new PartnerResponse(
                            partner.id().toString(),
                            partner.partnerNo().toString(),
                            partner.firstName(),
                            partner.lastName()
                    ))
                    .toList();

            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error searching partners with search {}.", search, e);
            return ResponseEntity.internalServerError().build();
        } finally {
            MDC.clear();
        }
    }


    @GetMapping("/{partnerNo}")
    public ResponseEntity<PartnerResponse> getPartner(
            @PathVariable String partnerNo) {
        try {
            MDC.put(PartnerMdc.PARTNER_NO.getId(), partnerNo);

            log.info("Request to get partner received.");

            Optional<Partner> potentialPartner = partnerService.findPartner(partnerNo);

            if (potentialPartner.isEmpty()) {
                log.warn("Account with partnerNo {} not found", partnerNo);
                return ResponseEntity.notFound().build();
            }

            Partner partner = potentialPartner.get();

            PartnerResponse response = new PartnerResponse(
                    partner.id().toString(),
                    partner.partnerNo().toString(),
                    partner.firstName(),
                    partner.lastName()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting partner for partnerNo {}.", partnerNo, e);
            return ResponseEntity.internalServerError().build();
        } finally {
            MDC.clear();
        }
    }

}