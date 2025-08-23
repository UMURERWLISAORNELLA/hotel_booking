package auca.ac.hotel_booking.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import auca.ac.hotel_booking.dto.HotelDTO;
import auca.ac.hotel_booking.dto.HotelMapper;
import auca.ac.hotel_booking.model.Hotel;
import auca.ac.hotel_booking.repository.HotelRepository;
import auca.ac.hotel_booking.service.HotelService;
import auca.ac.hotel_booking.model.Region;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {
    private final HotelService hotelService;
    private final HotelRepository hotelRepository;

    @Autowired
    public HotelController(HotelService hotelService, HotelRepository hotelRepository) {
        this.hotelService = hotelService;
        this.hotelRepository = hotelRepository;
    }

   @GetMapping
    public ResponseEntity<List<HotelDTO>> getAllHotels() {
    List<Hotel> hotels = hotelService.getAllHotels();
    List<HotelDTO> dtos = hotels.stream()
        .map(HotelMapper::toHotelDTO)
        .toList();
    return new ResponseEntity<>(dtos, HttpStatus.OK);
   }
   
   @GetMapping("/admin")
   public ResponseEntity<?> getHotelsPaginated(
           @RequestParam(defaultValue = "0") int page,
           @RequestParam(defaultValue = "10") int size) {
       
       try {
           // Use Spring Data JPA pagination
           org.springframework.data.domain.PageRequest pageRequest = 
               org.springframework.data.domain.PageRequest.of(page, size);
           
           org.springframework.data.domain.Page<Hotel> hotelPage = hotelRepository.findAll(pageRequest);
           
           List<HotelDTO> hotelDTOs = hotelPage.getContent().stream()
               .map(HotelMapper::toHotelDTO)
               .collect(java.util.stream.Collectors.toList());
           
           // Create response object
           java.util.Map<String, Object> response = new java.util.HashMap<>();
           response.put("content", hotelDTOs);
           response.put("totalElements", hotelPage.getTotalElements());
           response.put("totalPages", hotelPage.getTotalPages());
           response.put("currentPage", page);
           response.put("size", size);
           
           return ResponseEntity.ok(response);
       } catch (Exception e) {
           return ResponseEntity.status(500).body("Error fetching hotels: " + e.getMessage());
       }
   }
    @GetMapping("/{id}")
    public ResponseEntity<HotelDTO> getHotelById(@PathVariable Long id) {
        return hotelService.getHotelById(id)
            .map(hotel -> ResponseEntity.ok(HotelMapper.toHotelDTO(hotel)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllHotelNames() {
        List<String> names = hotelRepository.findAll()
            .stream()
            .map(Hotel::getName)
            .toList();
        return ResponseEntity.ok(names);
    }

    @GetMapping("/search")
    public ResponseEntity<List<String>> searchHotelNames(@RequestParam("q") String query) {
        List<String> names = hotelRepository.findByNameContainingIgnoreCase(query)
            .stream()
            .map(Hotel::getName)
            .toList();
        return ResponseEntity.ok(names);
    }

    @PostMapping
    public ResponseEntity<?> createHotel(@RequestBody Hotel hotel) {
        // Handle case-insensitive region
        if (hotel.getRegion() != null) {
            try {
                String regionName = hotel.getRegion().name().toUpperCase();
                hotel.setRegion(Region.valueOf(regionName));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid region. Valid regions are: KIGALI, RUBAVU, MUSANZE, NYAGATARE");
            }
        }
        
        Hotel createdHotel = hotelService.createHotel(hotel);
        return new ResponseEntity<>(HotelMapper.toHotelDTO(createdHotel), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHotel(@PathVariable Long id, @RequestBody Hotel updatedHotel) {
        // Handle case-insensitive region
        if (updatedHotel.getRegion() != null) {
            try {
                String regionName = updatedHotel.getRegion().name().toUpperCase();
                updatedHotel.setRegion(Region.valueOf(regionName));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid region. Valid regions are: KIGALI, RUBAVU, MUSANZE, NYAGATARE");
            }
        }
        
        Hotel hotel = hotelService.updateHotel(id, updatedHotel);
        return hotel != null 
        ? ResponseEntity.ok(HotelMapper.toHotelDTO(hotel))
        : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/images")
    public ResponseEntity<?> uploadHotelImages(@PathVariable Long id, @RequestParam("files") List<MultipartFile> files) {
        try {
            String uploadDir = "uploads/images";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

            List<String> imagePaths = hotel.getImages() != null ? hotel.getImages() : new ArrayList<>();

            for (MultipartFile file : files) {
                String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filepath = Paths.get(uploadDir, filename);
                Files.copy(file.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);
                imagePaths.add("/images/" + filename);
            }

            hotel.setImages(imagePaths);
            hotelRepository.save(hotel);

            return ResponseEntity.ok("Images uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Image upload failed: " + e.getMessage());
        }
    }
}