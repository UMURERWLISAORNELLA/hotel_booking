package auca.ac.hotel_booking.dto;

import java.util.List;

import auca.ac.hotel_booking.model.Room;

public class RoomDTO {
    private Long roomId;
    private String roomNumber;
    private String roomType;
    private int capacity;
    private double pricePerNight;
    private String roomStatus;
    private List<String> images;
    private Long hotelId;
    
    // Additional user-friendly fields
    private String hotelName;

    public RoomDTO(Room room) {
        this.roomId = room.getRoomId();
        this.roomNumber = room.getRoomNumber();
        this.roomType = room.getRoomType() != null ? room.getRoomType().name() : null;
        this.capacity = room.getCapacity();
        this.pricePerNight = room.getPricePerNight();
        this.roomStatus = room.getRoomStatus() != null ? room.getRoomStatus().name() : null;
        this.images = room.getImages();
        this.hotelId = room.getHotel() != null ? room.getHotel().getHotelId() : null;
        
        // Set user-friendly names
        if (room.getHotel() != null) {
            this.hotelName = room.getHotel().getName();
        }
    }

    // Getters for technical fields (for backend use only)
    public Long getRoomId() { return roomId; }
    public Long getHotelId() { return hotelId; }

    // Getters for user-friendly fields (for frontend display)
    public String getHotelName() { return hotelName; }
    
    // Additional getters for backward compatibility
    public Long getId() { return roomId; }
    public String getDisplayName() { 
        if (roomNumber != null && roomType != null) {
            return roomNumber + " (" + roomType + ")";
        }
        return roomNumber;
    }

    // Other getters
    public String getRoomNumber() { return roomNumber; }
    public String getRoomType() { return roomType; }
    public int getCapacity() { return capacity; }
    public double getPricePerNight() { return pricePerNight; }
    public String getRoomStatus() { return roomStatus; }
    public List<String> getImages() { return images; }

    // Setters
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }
    public void setRoomStatus(String roomStatus) { this.roomStatus = roomStatus; }
    public void setImages(List<String> images) { this.images = images; }
    public void setHotelId(Long hotelId) { this.hotelId = hotelId; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }
}
