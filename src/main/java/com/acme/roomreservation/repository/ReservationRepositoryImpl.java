package com.acme.roomreservation.repository;

import java.io.File;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.acme.roomreservation.domain.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ReservationRepositoryImpl implements ReservationRepository {

    private final ObjectMapper objectMapper;

    @Override
    public List<Reservation> getAllReservations() {

        final File resource;
        try {
            resource = new ClassPathResource("init-data/reservations.json").getFile();
            final String json = new String(Files.readAllBytes(resource.toPath()));
            List<Reservation> reservations = this.objectMapper.readValue(json, new TypeReference<>() {
            });
            reservations.forEach(this::setFullReservationEnd);
            return reservations;
        } catch (Exception e) {
            log.error("Cannot read reservations from file", e);
        }

        return Collections.emptyList();

    }

    private void setFullReservationEnd(Reservation reservation) {
        reservation.setFullReservationEnd(
            reservation.getReservationEnd().plus(Duration.ofMinutes(5 + reservation.getPersons())));
    }

}
