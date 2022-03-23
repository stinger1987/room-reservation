package com.acme.roomreservation.domain;

import lombok.Data;

@Data
public class MeetingRoom {
    String name;
    int maxAllocation;
    boolean haveMultimedia;

}
