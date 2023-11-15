package com.store.gift.controller;

import com.store.gift.dto.CertificateDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class CertificateResponse {
    private Embedded _embedded;
    private Map<String, Object> _links;

    @Data
    public static class Embedded {
        private List<CertificateDto> certificateDtoList;
    }
}
