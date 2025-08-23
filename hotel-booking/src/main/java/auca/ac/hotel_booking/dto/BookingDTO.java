package auca.ac.hotel_booking.dto;

import java.time.LocalDate;

import auca.ac.hotel_booking.model.Booking;

public class BookingDTO {
    private Long bookingId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDate bookingDate;
    private double totalPrice;
    private String bookingStatus;
    private Long userId;
    private Long roomId;
    private Long hotelId;
    
    // Additional user-friendly fields
    private String guestName;
    private String hotelName;
    private String roomNumber;
    private String roomType;

    public BookingDTO(Booking booking) {
        this.bookingId = booking.getBookingId();
        this.checkInDate = booking.getCheckInDate();
        this.checkOutDate = booking.getCheckOutDate();
        this.bookingDate = booking.getBookingDate();
        this.totalPrice = booking.getTotalPrice();
        this.bookingStatus = booking.getBookingStatus() != null ? booking.getBookingStatus().name() : null;
        this.userId = booking.getUser() != null ? booking.getUser().getId() : null;
        this.roomId = booking.getRoom() != null ? booking.getRoom().getRoomId() : null;
        this.hotelId = booking.getHotel() != null ? booking.getHotel().getHotelId() : null;
        
        // Set user-friendly names
        if (booking.getUser() != null) {
            this.guestName = booking.getUser().getFullName();
        }
        if (booking.getHotel() != null) {
            this.hotelName = booking.getHotel().getName();
        }
        if (booking.getRoom() != null) {
            this.roomNumber = booking.getRoom().getRoomNumber();
            this.roomType = booking.getRoom().getRoomType() != null ? booking.getRoom().getRoomType().name() : null;
        }
    }

    // Getters for technical fields (for backend use only)
    public Long getBookingId() { return bookingId; }
    public Long getUserId() { return userId; }
    public Long getRoomId() { return roomId; }
    public Long getHotelId() { return hotelId; }

    // Getters for user-friendly fields (for frontend display)
    public String getGuestName() { return guestName; }
    public String getHotelName() { return hotelName; }
    public String getRoomNumber() { return roomNumber; }
    public String getRoomType() { return roomType; }
    
    // Additional getters for backward compatibility
    public Long getId() { return bookingId; }
    public String getGuest() { return guestName; }
    public String getRoom() { 
        if (roomNumber != null && roomType != null) {
            return roomNumber + " (" + roomType + ")";
        }
        return roomNumber;
    }

    // Other getters
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public LocalDate getBookingDate() { return bookingDate; }
    public double getTotalPrice() { return totalPrice; }
    public String getBookingStatus() { return bookingStatus; }

    // Setters
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }
    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    public void setHotelId(Long hotelId) { this.hotelId = hotelId; }
    public void setGuestName(String guestName) { this.guestName = guestName; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
}