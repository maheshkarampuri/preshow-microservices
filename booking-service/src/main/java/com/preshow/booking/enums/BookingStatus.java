package com.preshow.booking.enums;

public enum BookingStatus {
    INITIATED,  // Seat locked, waiting for payment
    CONFIRMED,  // Payment success, seats booked
    CANCELLED,  // User or payment cancelled
    EXPIRED     // Payment timeout (e.g. 10 min)
}