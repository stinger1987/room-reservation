package com.acme.roomreservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.acme.roomreservation.converter.BuildingToMeetingRoomDtoConverter;
import com.acme.roomreservation.converter.ReservationToReservationDtoConverter;
import com.acme.roomreservation.domain.Building;
import com.acme.roomreservation.domain.Company;
import com.acme.roomreservation.domain.MeetingRoomDto;
import com.acme.roomreservation.domain.Reservation;
import com.acme.roomreservation.repository.CompanyMetadataRepository;
import com.acme.roomreservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    private final CompanyMetadataRepository companyMetadataRepository;

    private final BuildingToMeetingRoomDtoConverter buildingToMeetingRoomDtoConverter;

    private final ReservationToReservationDtoConverter reservationToReservationDtoConverter;

    @Override
    public List<MeetingRoomDto> getAvailableMeetingRooms(LocalDateTime meetingStart, LocalDateTime meetingEnd,
        int numberOfAttendees, boolean multimediaRequired, String buildingName) {
        log.info("Start retrieving available meeting rooms");
        Company company = companyMetadataRepository.getCompanyMetadata();
        List<Building> buildings;
        if (buildingName != null) {
            buildings = company.getBuildings().stream().filter(b -> b.getName().equals(buildingName))
                .collect(Collectors.toList());

        } else {
            buildings = company.getBuildings();
        }

        List<MeetingRoomDto> meetingRoomDtos = buildingToMeetingRoomDtoConverter.convert(buildings, numberOfAttendees,
            multimediaRequired);

        List<Reservation> reservations = getRoomReservationsPerPeriod(meetingStart, meetingEnd);

        List<MeetingRoomDto> filteredByReservationDtos = meetingRoomDtos.stream().filter(meetingRoomDto -> {
            Reservation reservation = reservations.stream().filter(r ->
                r.getMeetingRoomName().equals(meetingRoomDto.getMeetingRoomName())
                    && r.getFloorNumber() == meetingRoomDto.getFloorNumber()
                    && r.getBuildingName().equals(meetingRoomDto.getBuildingName())).findFirst().orElse(null);

            return reservation == null;

        }).collect(Collectors.toList());

        log.info("Found {} available meeting rooms", filteredByReservationDtos.size());
        return filteredByReservationDtos;

    }

    @Override
    public List<Reservation> getReservationsPerDay(LocalDate date) {
        log.info("Starting retrieving reservations for date={}", date);

        List<Reservation> allReservations = reservationRepository.getAllReservations();
        List<Reservation> filteredReservations = allReservations.stream().filter(r -> {
            LocalDateTime reservationStart = r.getReservationStart();
            LocalDateTime reservationEnd = r.getReservationEnd();

            return date.equals(reservationStart.toLocalDate()) || date.equals(reservationEnd.toLocalDate());
        }).collect(Collectors.toList());

        log.info("Found {} reservations for date {}", filteredReservations.size(), date);
        return filteredReservations;
    }

    @Override
    public Map<MeetingRoomDto, List<Reservation>> getReservationsMappingPerDay(LocalDate date) {
        List<Reservation> reservations = getReservationsPerDay(date);
        return reservationToReservationDtoConverter.convert(reservations);
    }

    private List<Reservation> getRoomReservationsPerPeriod(LocalDateTime start, LocalDateTime end) {
        log.info("Starting retrieving reservations for period start={} and end={}", start, end);

        List<Reservation> allReservations = reservationRepository.getAllReservations();
        List<Reservation> filteredReservations = allReservations.stream().filter(r -> {
            LocalDateTime reservationStart = r.getReservationStart();
            LocalDateTime reservationEnd = r.getFullReservationEnd();
            return !(start.isBefore(reservationStart) || start.isAfter(reservationEnd)) || (!(
                end.isBefore(reservationStart) || end.isAfter(reservationEnd)));
        }).collect(Collectors.toList());

        log.info("Found {} reservations for period start={} and end={}", filteredReservations.size(), start, end);
        return filteredReservations;
    }

}
