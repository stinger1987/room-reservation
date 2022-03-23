package com.acme.roomreservation.repository;

import java.util.List;

import com.acme.roomreservation.domain.Reservation;

public interface ReservationRepository {

    /**
     * Loads from storage list of all reservations
     *
     * @return list of all reservations
     */
    List<Reservation> getAllReservations();


}
