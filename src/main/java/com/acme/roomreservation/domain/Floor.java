package com.acme.roomreservation.domain;

import java.util.List;

import lombok.Data;

@Data
public class Floor {
    int number;
    List<MeetingRoom> meetingRooms;

}
