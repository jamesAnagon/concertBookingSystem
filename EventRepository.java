package com.mycompany.concertbookingsystem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class EventRepository {
    private final DateTimeFormatter eventDateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)
            .withResolverStyle(ResolverStyle.SMART);

    public List<ConcertEvent> getFeaturedEvents() {
        return Arrays.asList(
                new ConcertEvent("KYLE - SEAOIL", "May 30, 2026"),
                new ConcertEvent("ANCIRO - NYC", "Jun 12, 2026"),
                new ConcertEvent("AMANOLLAH - SEAWALL", "Jul 03, 2026"),
                new ConcertEvent("STELLA - LANE", "Aug 21, 2026"),
                new ConcertEvent("ORBIT - DOME", "Sep 10, 2026"),
                new ConcertEvent("NOVA - RIVERA", "Oct 05, 2026")
        );
    }

    public String getTimeUntilEvent(ConcertEvent concertEvent) {
        try {
            LocalDate eventDate = LocalDate.parse(concertEvent.getDateText(), eventDateFormatter);
            LocalDate today = LocalDate.now();
            if (!eventDate.isAfter(today)) {
                return "Starts today";
            }

            long days = ChronoUnit.DAYS.between(today, eventDate);
            if (days == 1) {
                return "Starts in 1 day";
            }
            if (days < 7) {
                return "Starts in " + days + " days";
            }

            long weeks = days / 7;
            return "Starts in " + weeks + " week" + (weeks > 1 ? "s" : "");
        } catch (Exception exception) {
            return "";
        }
    }
}
