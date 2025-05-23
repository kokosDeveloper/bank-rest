package com.example.bankcards.controller;

import com.example.bankcards.config.openApi.BasicErrorRepresentation;
import com.example.bankcards.config.openApi.ValidationErrorRepresentation;
import com.example.bankcards.dto.AuthenticationRequest;
import com.example.bankcards.dto.AuthenticationResponse;
import com.example.bankcards.dto.ErrorResponse;
import com.example.bankcards.dto.RegistrationRequest;
import com.example.bankcards.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    @Operation(
            summary = "Create an account",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "202"
                    ),
                    @ApiResponse(
                            description = "Validation error",
                            responseCode = "400",
                            content = @Content(
                                    schema = @Schema(implementation = ValidationErrorRepresentation.class, type = "application/json")
                            )
                    ),
                    @ApiResponse(
                            description = "Already exists",
                            responseCode = "409",
                            content = @Content(
                                    schema = @Schema(implementation = BasicErrorRepresentation.class, type = "application/json")
                            )
                    )
            }
    )
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegistrationRequest request
    ){
        authenticationService.register(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    @Operation(
            summary = "Get JWT token",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Validation error",
                            responseCode = "400",
                            content = @Content(
                                  schema = @Schema(implementation = ValidationErrorRepresentation.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Bad credentials",
                            responseCode = "401",
                            content = @Content(
                                    schema = @Schema(implementation = BasicErrorRepresentation.class)
                            )
                    )
            }
    )

    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

}
