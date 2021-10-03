package com.ssd.ssd_assignment_02_v1.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.calendar.model.Events;
import com.ssd.ssd_assignment_02_v1.dto.AccessTokenDTO;
import com.ssd.ssd_assignment_02_v1.dto.EventListDTO;
import com.ssd.ssd_assignment_02_v1.model.CalendarEvent;
import com.ssd.ssd_assignment_02_v1.model.NewEvent;
import com.ssd.ssd_assignment_02_v1.util.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CalendarEventService {
    private static final String APPLICATION_NAME = "Event Manager";
    private static final String CALENDAR_ID = "primary";

    @Autowired
    private RestClient restClient;

    @Autowired
    AccessTokenDTO accessTokenDTO;

    @Value("${google.listEventUrl}")
    private String listEventUrl;


    public List<CalendarEvent> viewAllEvents(boolean isUpcomingEvents) throws IOException {
        // Creating a new credential by setting the access token
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessTokenDTO.getAccessToken());

        // Creating a calendar service with the credential object created
        Calendar service = new Calendar.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME).build();
        // List upcoming events - sorted with starting time
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events;
        if (isUpcomingEvents) {
            events = service.events().list(CALENDAR_ID).setTimeMin(now).setOrderBy("startTime").setSingleEvents(true)
                    .execute();
        } else {
            events = service.events().list(CALENDAR_ID).setOrderBy("startTime").setSingleEvents(true)
                    .execute();
        }
        List<Event> items = events.getItems();
        List<CalendarEvent> eventList = new ArrayList<>();
        /* map events to simpler models */
        for (Event event : items) {

            DateTime start = event.getStart().getDateTime();
            if (start == null) {
                start = event.getStart().getDate();
            }

            DateTime end = event.getEnd().getDateTime();
            if (end == null) {
                end = event.getEnd().getDate();
            }
            CalendarEvent calendarEvent = new CalendarEvent();
            calendarEvent.setId(event.getId());
            calendarEvent.setSummary(event.getSummary());
            calendarEvent.setDescription(event.getDescription());
            calendarEvent.setStartDateTime(start.toString());
            calendarEvent.setEndDateTime(end.toString());
            eventList.add(calendarEvent);
        }
        return eventList;
    }

    public EventListDTO getAllEvents(boolean isIncludePastEvents) {
        if (isIncludePastEvents) {
            return restClient.restExchange(listEventUrl, accessTokenDTO.getAccessToken(), EventListDTO.class).getBody();
        } else {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(listEventUrl)
                    .queryParam("timeMin", new DateTime(System.currentTimeMillis()));
            return restClient.restExchangeWithParams(builder.build().encode().toUri(), accessTokenDTO.getAccessToken(),
                    EventListDTO.class).getBody();
        }

    }

    public Event addEvent(NewEvent newEvent) throws IOException {
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessTokenDTO.getAccessToken());
        Calendar service = new Calendar.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME).build();
        Event event = new Event().setSummary(newEvent.getSummary()).setDescription(newEvent.getDescription());
        String timeZone = "+05:30";
        String strStartDateTime = newEvent.getStartDate() + "T" + newEvent.getStartTime() + ":00" + timeZone;
        DateTime startDateTime = new DateTime(strStartDateTime);
        EventDateTime start = new EventDateTime().setDateTime(startDateTime);
        event.setStart(start);

        String strEndDateTime = newEvent.getEndDate() + "T" + newEvent.getEndTime() + ":00" + timeZone;

        DateTime endDateTime = new DateTime(strEndDateTime);
        EventDateTime end = new EventDateTime().setDateTime(endDateTime);
        event.setEnd(end);

        EventReminder[] reminderOverrides = new EventReminder[]{
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),};
        Event.Reminders reminders = new Event.Reminders().setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);

        return service.events().insert(CALENDAR_ID, event).execute();
    }
}
