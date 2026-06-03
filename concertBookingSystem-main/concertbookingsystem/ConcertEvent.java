package com.mycompany.concertbookingsystem;

public class ConcertEvent {
    private final String name;
    private final String dateText;

    public ConcertEvent(String name, String dateText) {
        this.name = name;
        this.dateText = dateText;
    }

    public String getName() {
        return name;
    }

    public String getDateText() {
        return dateText;
    }
}
