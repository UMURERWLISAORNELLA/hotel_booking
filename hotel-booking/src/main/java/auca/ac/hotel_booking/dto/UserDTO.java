package auca.ac.hotel_booking.dto;

import java.util.Set;
import java.util.stream.Collectors;

import auca.ac.hotel_booking.model.Role;
import auca.ac.hotel_booking.model.User;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private Set<String> roles;
    
    // Additional fields for better frontend handling
    private String firstName;
    private String lastName;
    
    // Constructor
    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.roles = user.getRoles().stream().map(Role::name).collect(Collectors.toSet());
        
        // Parse fullName into firstName and lastName
        if (user.getFullName() != null && user.getFullName().contains(" ")) {
            String[] nameParts = user.getFullName().split(" ", 2);
            this.firstName = nameParts[0];
            this.lastName = nameParts.length > 1 ? nameParts[1] : "";
        } else {
            this.firstName = user.getFullName();
            this.lastName = "";
        }
    }
    
    // Getters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public Set<String> getRoles() { return roles; }
    
    // Additional getters for frontend-friendly names
    public Long getUserId() { return id; } // For backward compatibility
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    
    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setRoles(Set<String> roles) { this.roles = roles; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}
