package com.myproject.controller;

import com.myproject.model.Email;
import com.myproject.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emails")
@Tag(name = "Email Controller", description = "API for managing emails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping
    @Operation(summary = "Send a new email", description = "Send a new email and save it to the database")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Email sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid email data")
    })
    public ResponseEntity<Email> sendEmail(@RequestBody Email email) {
        Email savedEmail = emailService.sendEmail(email);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmail);
    }

    @GetMapping("/received/{toEmail}")
    @Operation(summary = "Get emails received by a specific user", description = "Retrieves all emails received by a specific user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved received emails"),
            @ApiResponse(responseCode = "204", description = "No emails found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Email>> getEmailsReceivedBy(@PathVariable String toEmail) {
        List<Email> emails = emailService.getEmailsReceivedBy(toEmail);
        return emails.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(emails);
    }

    @GetMapping("/sent/{fromEmail}")
    @Operation(summary = "Get emails sent by a specific user", description = "Retrieves all emails sent by a specific user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sent emails"),
            @ApiResponse(responseCode = "204", description = "No emails found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Email>> getEmailsSentBy(@PathVariable String fromEmail) {
        List<Email> emails = emailService.getEmailsSentBy(fromEmail);
        return emails.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(emails);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get email by ID", description = "Retrieve an email by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the email"),
            @ApiResponse(responseCode = "404", description = "Email not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Email> getEmailById(@PathVariable Long id) {
        Email email = emailService.getEmailById(id);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Return 404 if email not found
        }
        return ResponseEntity.ok(email);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an email", description = "Delete an email by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Email deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Email not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteEmail(@PathVariable Long id) {
        emailService.deleteEmail(id);
        return ResponseEntity.noContent().build();  // Return 204 No Content on successful deletion
    }
}
