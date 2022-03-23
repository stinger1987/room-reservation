package com.acme.roomreservation.converter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.acme.roomreservation.domain.Building;
import com.acme.roomreservation.domain.Floor;
import com.acme.roomreservation.domain.MeetingRoom;
import com.acme.roomreservation.domain.MeetingRoomDto;

/**
 * Converts and filter meeting rooms suitable for reservation considering multimedia and number of persons
 */

@Component
public class BuildingToMeetingRoomDtoConverter {

    public List<MeetingRoomDto> convert(List<Building> buildings, int numberOfAttendees, boolean multimediaRequired) {
        List<MeetingRoomDto> meetingRoomDtos = new ArrayList<>();
        for (Building b : buildings) {
            for (Floor f : b.getFloors()) {
                for (MeetingRoom m : f.getMeetingRooms()) {
                    if ((!multimediaRequired || m.isHaveMultimedia()) && m.getMaxAllocation() >= numberOfAttendees) {
                        meetingRoomDtos.add(
                            MeetingRoomDto.builder()
                                .meetingRoomName(m.getName())
                                .buildingName(b.getName())
                                .floorNumber(f.getNumber())
                                .efficiency(m.getMaxAllocation() - numberOfAttendees)
                                .build());
                    }
                }

            }
        }

        meetingRoomDtos.sort(Comparator.comparingInt(MeetingRoomDto::getEfficiency));
        return meetingRoomDtos;

    }

}
