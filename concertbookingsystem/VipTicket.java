package com.mycompany.concertbookingsystem;

public class VipTicket extends Ticket {
    private String loungeAccessPass;

    public VipTicket(String customerName, String concertName, double basePrice, String loungeAccessPass) {
        super(customerName, concertName, basePrice);
        this.loungeAccessPass = loungeAccessPass;
    }

    @Override
    public double calculateFinalPrice() {
        return getBasePrice() + 50.0; // VIP tax/fee added
    }

    @Override
    public String getTicketType() { return "VIP"; }

    @Override
    public String getSpecialPerk() { return this.loungeAccessPass; }
}
