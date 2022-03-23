package com.acme.roomreservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.acme.roomreservation.domain.MeetingRoomDto;
import com.acme.roomreservation.domain.Reservation;

public interface ReservationService {

    /**
     * By supplying the start date and time and end date and time, number of attendees and required multimedia
     * capabilities the user should be able to view a list of available rooms. Specifying the building is optional.
     *
     * @param meetingStart Start dateTime of meeting
     * @param meetingEnd end dateTime of meeting
     * @param numberOfAttendees number of persons which will attend
     * @param multimediaRequired is multimedia capabilities are required
     * @param buildingName name of building where to look for meeting rooms (optional)
     * @return Result list should be ordered based off efficiency of allocation.
     */
    List<MeetingRoomDto> getAvailableMeetingRooms(LocalDateTime meetingStart, LocalDateTime meetingEnd,
        int numberOfAttendees, boolean multimediaRequired, String buildingName);

    /**
     * Operation to return list of reservations on any day.
     *
     * @param date date on which list of reservations should be retrieved
     * @return list of reservations
     */
    List<Reservation> getReservationsPerDay(LocalDate date);

    /**
     * Operation to return mapping between room and reservations for this room, for all buildings on any day.
     *
     * @param date date on which list of reservations should be retrieved
     * @return mapping between meeting room and list of reservations per one day
     */
    Map<MeetingRoomDto, List<Reservation>> getReservationsMappingPerDay(LocalDate date);


}
