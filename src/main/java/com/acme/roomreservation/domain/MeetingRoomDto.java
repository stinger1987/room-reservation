package com.acme.roomreservation.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO Object which will hold information about meeting room to identify them uniquely, considering building name, floor
 * and room name
 */

@Data
@Builder
@EqualsAndHashCode
public class MeetingRoomDto {

    String buildingName;
    int floorNumber;
    String meetingRoomName;
    //Room utilization ratio, lower is better (means how many free seats remains after reservation)
    int efficiency;

}
