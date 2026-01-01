package com.preshow.showquery.enums;

public enum SeatStatus {
    AVAILABLE,   // Seat is free to book
    RESERVED,    // Temporarily held (e.g., during checkout)
    BOOKED,      // Successfully booked/paid
    BLOCKED      // Admin/Management blocked (cannot be booked)
}