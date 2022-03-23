package com.acme.roomreservation.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Reservation {

    LocalDateTime reservationStart;
    LocalDateTime reservationEnd;
    String meetingRoomName;
    int floorNumber;
    String buildingName;
    int persons;
    // Rooms have a clean-up time proportional to their size (5 minutes base + 1 min per room seat). Full end is time considering cleanup.
    LocalDateTime fullReservationEnd;


}
