package com.acme.roomreservation.domain;

import java.util.List;

import lombok.Data;

@Data
public class Building {

    String name;
    List<Floor> floors;

}
