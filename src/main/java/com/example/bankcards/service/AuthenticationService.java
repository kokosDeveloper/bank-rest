package com.example.bankcards.service;

import com.example.bankcards.dto.AuthenticationRequest;
import com.example.bankcards.dto.AuthenticationResponse;
import com.example.bankcards.dto.RegistrationRequest;

public interface AuthenticationService {
    void register(RegistrationRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
