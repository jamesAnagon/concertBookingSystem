package com.mycompany.concertbookingsystem;

// INHERITANCE: RegularTicket inherits from Ticket
public class RegularTicket extends Ticket {
    
    public RegularTicket(String customerName, String concertName, double basePrice) {
        super(customerName, concertName, basePrice);
    }

    @Override
    public double calculateFinalPrice() {
        return getBasePrice(); // Regular tickets have no extra fees
    }

    @Override
    public String getTicketType() { return "Regular"; }

    @Override
    public String getSpecialPerk() { return "Standard Seat Entry"; }
}
