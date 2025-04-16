package com.myproject.controller;

import com.myproject.dto.EmailBasicDTO;
import com.myproject.dto.EmailDetailDTO;
import com.myproject.exception.EmailNotFoundException;
import com.myproject.mapper.EmailMapper;
import com.myproject.model.Email;
import com.myproject.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
@Tag(name = "Email Management API", description = "APIs for managing emails")
public class EmailController {

    private final EmailService emailService;
    private final EmailMapper emailMapper;

    @PostMapping
    @Operation(summary = "Send a new email", description = "Send a new email and save it to the database")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Email sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid email data")
    })
    public ResponseEntity<EmailDetailDTO> sendEmail(@RequestBody Email email) {
        Email savedEmail = emailService.sendEmail(email);
        return ResponseEntity.status(HttpStatus.CREATED).body(emailMapper.toEmailDetailDTO(savedEmail));
    }

    @GetMapping("/received/{toEmail}")
    @Operation(summary = "Get received emails", description = "Retrieve all emails received by a specific email address")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved emails"),
            @ApiResponse(responseCode = "204", description = "No emails found")
    })
    public ResponseEntity<List<EmailBasicDTO>> getEmailsReceivedBy(@PathVariable String toEmail) {
        List<Email> emails = emailService.getEmailsReceivedBy(toEmail);
        return emails.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(emailMapper.toEmailBasicDTOs(emails));
    }

    @GetMapping("/sent/{fromEmail}")
    @Operation(summary = "Get sent emails", description = "Retrieve all emails sent by a specific email address")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved emails"),
            @ApiResponse(responseCode = "204", description = "No emails found")
    })
    public ResponseEntity<List<EmailBasicDTO>> getEmailsSentBy(@PathVariable String fromEmail) {
        List<Email> emails = emailService.getEmailsSentBy(fromEmail);
        return emails.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(emailMapper.toEmailBasicDTOs(emails));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get email by ID", description = "Retrieve detailed email info by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved email"),
            @ApiResponse(responseCode = "404", description = "Email not found")
    })
    public ResponseEntity<EmailDetailDTO> getEmailById(@PathVariable Long id) {
        Email email = emailService.getEmailById(id);
        if (email == null) {
            throw new EmailNotFoundException("Email with ID " + id + " not found");
        }
        return ResponseEntity.ok(emailMapper.toEmailDetailDTO(email));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete email by ID", description = "Delete an email using its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Email deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Email not found")
    })
    public ResponseEntity<Void> deleteEmail(@PathVariable Long id) {
        emailService.deleteEmail(id);
        return ResponseEntity.noContent().build();
    }

    // Exception handler
    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<String> handleEmailNotFoundException(EmailNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
