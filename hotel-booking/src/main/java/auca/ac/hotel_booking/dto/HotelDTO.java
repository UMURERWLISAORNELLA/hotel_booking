package auca.ac.hotel_booking.dto;

import java.util.List;

import auca.ac.hotel_booking.model.Hotel;

public class HotelDTO {

    private Long hotelId;
    private String name;
    private String description;
    private String address;
    private String contactDetails;
    private String region;
    private List<String> amenities;
    private List<String> images;
    public List<RoomDTO> rooms; 

    // Constructor
    public HotelDTO(Hotel hotel) {
        this.hotelId = hotel.getHotelId();
        this.name = hotel.getName();
        this.description = hotel.getDescription();
        this.address = hotel.getAddress();
        this.contactDetails = hotel.getContactDetails();
        this.region = hotel.getRegion() != null ? hotel.getRegion().name() : null;
        this.amenities = hotel.getAmenities();
        this.images = hotel.getImages();
        this.rooms = hotel.getRooms() != null
            ? hotel.getRooms().stream().map(RoomDTO::new).toList()
            : List.of();
    }
    
    public HotelDTO(Long hotelId, String name, String description, String address, String contactDetails,
                    String region, List<String> amenities, List<String> images, List<RoomDTO> rooms) {
        this.hotelId = hotelId;
        this.name = name;
        this.description = description;
        this.address = address;
        this.contactDetails = contactDetails;
        this.region = region;
        this.amenities = amenities;
        this.images = images;
        this.rooms = rooms;
    }
    
    // Getters
    public Long getHotelId() { return hotelId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getAddress() { return address; }
    public String getContactDetails() { return contactDetails; }
    public String getRegion() { return region; }
    public List<String> getAmenities() { return amenities; }
    public List<String> getImages() { return images; }
    public List<RoomDTO> getRooms() { return rooms; }
    
    // Additional getters for frontend-friendly names
    public Long getId() { return hotelId; } // For backward compatibility
    public String getHotelName() { return name; } // Alternative name
    
    // Setters
    public void setHotelId(Long hotelId) { this.hotelId = hotelId; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setAddress(String address) { this.address = address; }
    public void setContactDetails(String contactDetails) { this.contactDetails = contactDetails; }
    public void setRegion(String region) { this.region = region; }
    public void setAmenities(List<String> amenities) { this.amenities = amenities; }
    public void setImages(List<String> images) { this.images = images; }
    public void setRooms(List<RoomDTO> rooms) { this.rooms = rooms; }
}
    

