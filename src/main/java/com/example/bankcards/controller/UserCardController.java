package com.example.bankcards.controller;

import com.example.bankcards.config.openApi.BasicErrorRepresentation;
import com.example.bankcards.config.openApi.ValidationErrorRepresentation;
import com.example.bankcards.dto.CardBlockRequestDto;
import com.example.bankcards.dto.CardDtoForUser;
import com.example.bankcards.dto.MoneyTransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Status;
import com.example.bankcards.entity.User;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.mapper.CardMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/cards")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Card management for users")
@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content())
@ApiResponse(description = "Forbidden", responseCode = "403", content = @Content())
public class UserCardController {
    private final CardService cardService;
    private final CardMapper cardMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('see_cards_which_possess')")
    @Operation(
            summary = "Search cards by status or last4 numbers",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    )
            }
    )
    public Page<CardDtoForUser> searchCards(
            @RequestParam(required = false) String last4,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            Authentication authentication
    ) {
        Page<Card> searchResults = cardService.searchCards(
                authentication, last4, status, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "moneyAmount")));
        return searchResults.map(cardMapper::toCardDtoForUser);
    }

    @PreAuthorize("hasAuthority('transfer_money_between_carts_which_possess')")
    @PostMapping("/transfer")
    @Operation(
            summary = "Transfer money between cards",
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
                            description = "Card not found",
                            responseCode = "404",
                            content = @Content(
                                    schema = @Schema(implementation = BasicErrorRepresentation.class)
                            )
                    )
            }
    )
    public ResponseEntity<Void> transferMoney(
            @Valid @RequestBody MoneyTransferRequest request,
            Authentication authentication
    ) {
        cardService.transferMoney(request, authentication);
        return ResponseEntity.ok().build();
    }
}
