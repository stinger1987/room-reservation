package com.acme.roomreservation.domain;

import java.util.List;

import lombok.Data;

@Data
public class Company {

    String name;
    List<Building> buildings;

}
