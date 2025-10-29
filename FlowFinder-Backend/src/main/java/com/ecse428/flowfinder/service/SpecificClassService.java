package com.ecse428.flowfinder.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ecse428.flowfinder.exception.FlowFinderException;
import com.ecse428.flowfinder.model.DanceClass;
import com.ecse428.flowfinder.model.Instructor;
import com.ecse428.flowfinder.model.SpecificClass;
import com.ecse428.flowfinder.repository.SpecificClassRepository;

import jakarta.transaction.Transactional;

@Service
public class SpecificClassService {

    @Autowired
    private SpecificClassRepository specificClassRepository;

    // Create or save a SpecificClass
    @Transactional
    public SpecificClass createSpecificClass(String aLocation, LocalDate aDate, int aLimit,
            LocalTime aStart,
            LocalTime aEnd, DanceClass aClass, Instructor aInstructor) {
        if (aClass == null || aInstructor == null) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "Class or Instructor cannot be null");
        }
        if (aInstructor.getIsDeleted()) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "Instructor account is deleted");
        }
        validateLimit(aLimit);
        validateStartAndEndTime(aStart, aEnd);
        validateDateAndTime(aDate, aStart, aEnd);
        validateLocation(aLocation);

        SpecificClass specificClass = new SpecificClass(false, aLocation, aDate, aLimit, aStart, aEnd, aClass,
                aInstructor);
        return specificClassRepository.save(specificClass);
    }

    // Find by ID
    public SpecificClass getSpecificClassById(int id) {
        Optional<SpecificClass> result = specificClassRepository.findById(id);
        if (result.isEmpty()) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST,
                    "No Specific class found with ID " + id);
        }
        return result.get();
    }

    // Find all by Instructor
    public Iterable<SpecificClass> getSpecificClassesByInstructor(Instructor instructor) {
        if (instructor == null) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "Instructor cannot be null");
        }
        return specificClassRepository.findSpecificClassByInstructor(instructor);
    }

    // Find all by DanceClass
    public Iterable<SpecificClass> getSpecificClassesByDanceClass(DanceClass danceClass) {
        if (danceClass == null) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "Dance class cannot be null");
        }
        return specificClassRepository.findSpecificClassByDanceClass(danceClass);
    }

    // Find all by Location
    public Iterable<SpecificClass> getSpecificClassesByLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "Location cannot be null or empty");
        }
        return specificClassRepository.findSpecificClassByLocation(location);
    }

    // Find all by Date
    public Iterable<SpecificClass> getSpecificClassesByDate(LocalDate date) {
        if (date == null) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "Date cannot be null");
        }
        return specificClassRepository.findSpecificClassByDate(date);
    }

    // Find all between Dates
    public Iterable<SpecificClass> getSpecificClassesByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "Start and end dates cannot be null");
        }
        if (endDate.isBefore(startDate)) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "End date cannot be before start date");
        }
        return specificClassRepository.findSpecificClassByDateBetween(startDate, endDate);
    }

    // Delete by ID
    public void deleteSpecificClass(int id) {
        if (!specificClassRepository.existsById(id)) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "No Specific class found with ID " + id);
        }
        specificClassRepository.deleteById(id);
    }

    private void validateLimit(int aLimit) {
        if (aLimit <= 0) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "Class limit must be positive");
        }
    }

    private void validateDateAndTime(LocalDate date, LocalTime start, LocalTime end) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        if (date == null) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "Date cannot be null");
        }

        if (date.isBefore(today)) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "Date of class cannot be in the past");
        }
        validateStartAndEndTime(start, end);

        if (date.isEqual(today)) {
            if (!start.isAfter(now) || !end.isAfter(now)) {
                throw new FlowFinderException(HttpStatus.BAD_REQUEST,
                        "Class start and end time must be after current time");
            }

        }
    }

    public void validateStartAndEndTime(LocalTime start, LocalTime end) {
        if (start == null || end == null) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "Start and end times cannot be null");
        }

        if (!end.isAfter(start)) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "Class start and end time must be after current time");
        }
    }

    public void validateLocation(String aLocation) {
        if (aLocation == null) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "Location cannot be null");
        }
        if (aLocation.trim().isEmpty()) {
            throw new FlowFinderException(HttpStatus.BAD_REQUEST, "Location cannot be empty");
        }
    }

}
