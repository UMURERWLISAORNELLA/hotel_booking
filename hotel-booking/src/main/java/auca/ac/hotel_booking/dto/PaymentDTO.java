package auca.ac.hotel_booking.dto;

import java.time.LocalDate;

import auca.ac.hotel_booking.model.Payment;

public class PaymentDTO {
    private Long paymentId;
    private String paymentMethod;
    private double amountPaid;
    private String transactionId;
    private LocalDate paymentDate;
    private boolean confirmed;
    private String paymentStatus;
    private Long bookingId;
    
    // Additional user-friendly fields
    private String guestName;
    private String hotelName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    public PaymentDTO(Payment payment) {
        this.paymentId = payment.getPaymentId();
        this.paymentMethod = payment.getPaymentMethod() != null ? payment.getPaymentMethod().name() : null;
        this.amountPaid = payment.getAmountPaid();
        this.transactionId = payment.getTransactionId();
        this.paymentDate = payment.getPaymentDate();
        this.confirmed = payment.isConfirmed();
        this.paymentStatus = payment.getPaymentStatus() != null ? payment.getPaymentStatus().name() : null;
        this.bookingId = payment.getBooking() != null ? payment.getBooking().getBookingId() : null;
        
        // Set user-friendly names
        if (payment.getBooking() != null) {
            if (payment.getBooking().getUser() != null) {
                this.guestName = payment.getBooking().getUser().getFullName();
            }
            if (payment.getBooking().getHotel() != null) {
                this.hotelName = payment.getBooking().getHotel().getName();
            }
            this.checkInDate = payment.getBooking().getCheckInDate();
            this.checkOutDate = payment.getBooking().getCheckOutDate();
        }
    }

    // Getters for technical fields (for backend use only)
    public Long getPaymentId() { return paymentId; }
    public Long getBookingId() { return bookingId; }

    // Getters for user-friendly fields (for frontend display)
    public String getGuestName() { return guestName; }
    public String getHotelName() { return hotelName; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    
    // Additional getters for backward compatibility
    public Long getId() { return paymentId; }
    public String getGuest() { return guestName; }
    public String getHotel() { return hotelName; }
    public String getBookingDetails() { 
        if (checkInDate != null && checkOutDate != null) {
            return checkInDate + " to " + checkOutDate;
        }
        return "N/A";
    }

    // Other getters
    public String getPaymentMethod() { return paymentMethod; }
    public double getAmountPaid() { return amountPaid; }
    public String getTransactionId() { return transactionId; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public boolean isConfirmed() { return confirmed; }
    public String getPaymentStatus() { return paymentStatus; }

    // Setters
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
    public void setConfirmed(boolean confirmed) { this.confirmed = confirmed; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public void setGuestName(String guestName) { this.guestName = guestName; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }
    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }
}