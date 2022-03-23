package com.acme.roomreservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acme.roomreservation.domain.MeetingRoomDto;
import com.acme.roomreservation.domain.Reservation;
import com.acme.roomreservation.service.ReservationService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ReservationServiceTest {

    @Autowired
    ReservationService reservationService;

    //No reservations on this date and time
    @Test
    public void getAvailableMeetingRoomsWithoutReservationTest() {
        List<MeetingRoomDto> dtos = reservationService.getAvailableMeetingRooms(LocalDateTime.of(2022, 3, 23, 10, 0, 0),
            LocalDateTime.of(2022, 3, 23, 10, 30, 0), 4, true, null);

        assertNotNull(dtos);
        assertEquals(13, dtos.size());
        assertEquals("Room 312", dtos.get(0).getMeetingRoomName());
        assertEquals(3, dtos.get(0).getFloorNumber());
        assertEquals(0, dtos.get(0).getEfficiency());

    }

    //Room 312 was booked on this time, so next from list should be proposed
    @Test
    public void getAvailableMeetingRoomsWithReservationTest() {
        List<MeetingRoomDto> dtos = reservationService.getAvailableMeetingRooms(LocalDateTime.of(2022, 3, 25, 10, 0, 0),
            LocalDateTime.of(2022, 3, 25, 10, 30, 0), 4, true, null);

        assertNotNull(dtos);
        assertEquals(12, dtos.size());
        assertEquals("Room 1001", dtos.get(0).getMeetingRoomName());
        assertEquals(10, dtos.get(0).getFloorNumber());
        assertEquals(1, dtos.get(0).getEfficiency());

    }

    //Room 312 was booked on time with partial intersection, so should not be available
    @Test
    public void getAvailableMeetingRoomsWithTimeIntersectionTest() {
        List<MeetingRoomDto> dtos = reservationService.getAvailableMeetingRooms(
            LocalDateTime.of(2022, 3, 25, 10, 20, 0),
            LocalDateTime.of(2022, 3, 25, 12, 0, 0), 4, true, null);

        assertNotNull(dtos);
        assertEquals(12, dtos.size());
        assertEquals("Room 1001", dtos.get(0).getMeetingRoomName());
        assertEquals(10, dtos.get(0).getFloorNumber());
        assertEquals(1, dtos.get(0).getEfficiency());

    }

    //Reservation will not be available until room is cleaned up
    @Test
    public void getAvailableMeetingRoomsBeforeCleanupTest() {
        List<MeetingRoomDto> dtos = reservationService.getAvailableMeetingRooms(
            LocalDateTime.of(2022, 3, 25, 10, 37, 0),
            LocalDateTime.of(2022, 3, 25, 12, 0, 0), 4, true, null);

        assertNotNull(dtos);
        assertEquals(12, dtos.size());
        assertEquals("Room 1001", dtos.get(0).getMeetingRoomName());
        assertEquals(10, dtos.get(0).getFloorNumber());
        assertEquals(1, dtos.get(0).getEfficiency());

    }

    //Reservation will be available after room is cleaned up
    @Test
    public void getAvailableMeetingRoomsAfterCleanupTest() {
        List<MeetingRoomDto> dtos = reservationService.getAvailableMeetingRooms(
            LocalDateTime.of(2022, 3, 25, 10, 38, 0),
            LocalDateTime.of(2022, 3, 25, 12, 0, 0), 4, true, null);

        assertNotNull(dtos);
        assertEquals(13, dtos.size());
        assertEquals("Room 312", dtos.get(0).getMeetingRoomName());
        assertEquals(3, dtos.get(0).getFloorNumber());
        assertEquals(0, dtos.get(0).getEfficiency());

    }

    //Specify optional parameter block A, so all rooms should be from this block
    @Test
    public void getAvailableMeetingRoomsWithBuildingSpecifiedTest() {
        List<MeetingRoomDto> dtos = reservationService.getAvailableMeetingRooms(LocalDateTime.of(2022, 3, 25, 10, 0, 0),
            LocalDateTime.of(2022, 3, 25, 10, 30, 0), 4, true, "Block A");

        assertNotNull(dtos);
        assertEquals(6, dtos.size());
        assertEquals("Room 205", dtos.get(0).getMeetingRoomName());
        assertEquals(2, dtos.get(0).getFloorNumber());
        assertEquals(2, dtos.get(0).getEfficiency());
        for (MeetingRoomDto dto : dtos) {
            assertEquals("Block A", dto.getBuildingName());
        }

    }

    //Get reservations per room for 1 day
    @Test
    public void getReservationsMappingPerDayTest() {
        Map<MeetingRoomDto, List<Reservation>> reservationsMappingPerDay = reservationService.getReservationsMappingPerDay(LocalDate.of(2022, 3, 25));

        assertNotNull(reservationsMappingPerDay);
        assertEquals(2, reservationsMappingPerDay.size());
        MeetingRoomDto dto = MeetingRoomDto.builder().meetingRoomName("Room 312").buildingName("Block A").floorNumber(3).build();
        List<Reservation> reservations = reservationsMappingPerDay.get(dto);
        assertNotNull(reservations);
        assertEquals(3, reservations.size());
    }

    //Get reservations per room for 1 day without reservations
    @Test
    public void getReservationsMappingEmptyTest() {
        Map<MeetingRoomDto, List<Reservation>> reservationsMappingPerDay = reservationService.getReservationsMappingPerDay(LocalDate.of(2022, 3, 28));

        assertNotNull(reservationsMappingPerDay);
        assertEquals(0, reservationsMappingPerDay.size());

    }



}
