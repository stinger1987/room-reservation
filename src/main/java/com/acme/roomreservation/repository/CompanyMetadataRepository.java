package com.acme.roomreservation.repository;

import com.acme.roomreservation.domain.Company;

public interface CompanyMetadataRepository {

    /**
     * Loads from underlying storage company metadata with its internal structure: buildings, floors and meeting rooms
     *
     * @return company
     */
    Company getCompanyMetadata();

}
