package com.acme.roomreservation.converter;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.acme.roomreservation.domain.MeetingRoomDto;
import com.acme.roomreservation.domain.Reservation;

import static java.util.stream.Collectors.groupingBy;

@Component
public class ReservationToReservationDtoConverter {

    public Map<MeetingRoomDto, List<Reservation>> convert(List<Reservation> reservations) {

        return reservations.stream().collect(groupingBy(r ->
            MeetingRoomDto.builder().meetingRoomName(r.getMeetingRoomName()).buildingName(r.getBuildingName()).floorNumber(r.getFloorNumber()).build()

        ));

    }

}
