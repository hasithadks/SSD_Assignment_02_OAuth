package com.ssd.ssd_assignment_02_v1.dto;

import com.ssd.ssd_assignment_02_v1.model.CalendarEvent;
import lombok.Data;

import java.util.List;


@Data
public class EventListDTO {
    private List<CalendarEvent> calendarEventList;
}
