package com.acme.roomreservation.repository;

import java.io.File;
import java.nio.file.Files;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.acme.roomreservation.domain.Company;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyMetadataRepositoryImpl implements CompanyMetadataRepository {

    private final ObjectMapper objectMapper;

    @Override
    public Company getCompanyMetadata() {
        final File resource;
        try {
            resource = new ClassPathResource("init-data/company.json").getFile();
            final String json = new String(Files.readAllBytes(resource.toPath()));
            return this.objectMapper.readValue(json, Company.class);
        } catch (Exception e) {
            log.error("Cannot read company metadata from file", e);
        }

        return null;
    }
}
