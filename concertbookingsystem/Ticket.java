package com.mycompany.concertbookingsystem;

// ABSTRACTION: This class cannot be instantiated directly
public abstract class Ticket {
    // ENCAPSULATION: private variables protected from direct outside modification
    private int id;
    private String customerName;
    private String concertName;
    private double basePrice;

    public Ticket(String customerName, String concertName, double basePrice) {
        this.customerName = customerName;
        this.concertName = concertName;
        this.basePrice = basePrice;
    }

    // ABSTRACTION: Subclasses must implement their own price calculation logic
    public abstract double calculateFinalPrice();
    public abstract String getTicketType();
    public abstract String getSpecialPerk();

    // ENCAPSULATION: Public getters and setters to access private data safely
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getCustomerName() { return customerName; }
    public String getConcertName() { return concertName; }
    public double getBasePrice() { return basePrice; }
}
