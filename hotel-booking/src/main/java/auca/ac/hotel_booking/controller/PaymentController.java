package auca.ac.hotel_booking.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import auca.ac.hotel_booking.dto.PaymentDTO;
import auca.ac.hotel_booking.model.Payment;
import auca.ac.hotel_booking.model.PaymentStatus;
import auca.ac.hotel_booking.service.PaymentService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        List<PaymentDTO> dtos = payments.stream().map(PaymentDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long id) {
        Optional<Payment> payment = paymentService.getPaymentById(id);
        return payment.map(p -> ResponseEntity.ok(new PaymentDTO(p)))
                      .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(@Valid @RequestBody PaymentRequestDTO paymentRequestDTO) {
        Payment createdPayment = paymentService.createPayment(
                paymentRequestDTO.getBookingId(),
                paymentRequestDTO.getAmountPaid(),
                paymentRequestDTO.getPaymentMethod(),
                paymentRequestDTO.getTransactionId(),
                paymentRequestDTO.getPaymentDate(),
                paymentRequestDTO.getPaymentStatus()
        );
        return createdPayment != null
            ? new ResponseEntity<>(new PaymentDTO(createdPayment), HttpStatus.CREATED)
            : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDTO> updatePayment(@PathVariable Long id,
                                                    @Valid @RequestBody PaymentRequestDTO paymentRequestDTO) {
        Payment updatedPayment = paymentService.updatePayment(
                id,
                paymentRequestDTO.getBookingId(),
                paymentRequestDTO.getAmountPaid(),
                paymentRequestDTO.getPaymentMethod(),
                paymentRequestDTO.getTransactionId(),
                paymentRequestDTO.getPaymentDate(),
                paymentRequestDTO.getPaymentStatus()
        );
        return updatedPayment != null
            ? ResponseEntity.ok(new PaymentDTO(updatedPayment))
            : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<PaymentDTO> getPaymentByBookingId(@PathVariable Long bookingId) {
        Optional<Payment> payment = paymentService.getPaymentByBookingId(bookingId);
        return payment.map(p -> ResponseEntity.ok(new PaymentDTO(p)))
                      .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmPayment(@RequestParam("token") String token) {
        Optional<Payment> paymentOpt = paymentService.findByConfirmationToken(token);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setConfirmed(true);
            payment.setConfirmationToken(null);
            payment.setPaymentStatus(PaymentStatus.PAID);
            paymentService.save(payment);
            return ResponseEntity.ok("Payment confirmed! Thank you.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired confirmation link.");
        }
    }

    @PutMapping("/fail/{id}")
    public ResponseEntity<?> failPayment(@PathVariable Long id) {
        try {
            paymentService.markPaymentFailed(id);
            return ResponseEntity.ok("Payment marked as FAILED.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/refund/{id}")
    public ResponseEntity<?> refundPayment(@PathVariable Long id) {
        try {
            paymentService.markPaymentRefunded(id);
            return ResponseEntity.ok("Payment marked as REFUNDED.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{paymentId}/resend-confirmation")
    public ResponseEntity<?> resendConfirmation(@PathVariable Long paymentId) {
        paymentService.resendConfirmationEmail(paymentId);
        return ResponseEntity.ok("Confirmation email resent.");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByUserAndStatus(@PathVariable Long userId, @RequestParam PaymentStatus status) {
        List<Payment> payments = paymentService.findByUserIdAndStatus(userId, status);
        List<PaymentDTO> dtos = payments.stream().map(PaymentDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<PaymentDTO>> getPendingPayments(@RequestParam Long userId) {
        List<Payment> pendingPayments = paymentService.findByUserIdAndStatus(userId, PaymentStatus.PENDING);
        List<PaymentDTO> dtos = pendingPayments.stream().map(PaymentDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}