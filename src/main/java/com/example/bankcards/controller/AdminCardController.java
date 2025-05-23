package com.example.bankcards.controller;

import com.example.bankcards.config.openApi.BasicErrorRepresentation;
import com.example.bankcards.config.openApi.ValidationErrorRepresentation;
import com.example.bankcards.dto.CardCreateUpdateRequest;
import com.example.bankcards.dto.CardDtoForAdmin;
import com.example.bankcards.dto.CardDtoForUser;
import com.example.bankcards.dto.MoneyDepositRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Status;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/cards")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Card management for admins")
@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content())
@ApiResponse(description = "Forbidden", responseCode = "403", content = @Content())
public class AdminCardController {
    private final CardService cardService;
    private final CardMapper cardMapper;

    @PostMapping
    @PreAuthorize("hasAuthority('create_card_for_user')")
    @Operation(
            summary = "Create card for user",
            responses = {
                    @ApiResponse(
                            description = "Created",
                            responseCode = "201"
                    ),
                    @ApiResponse(
                            description = "User not found",
                            responseCode = "404",
                            content = @Content(schema = @Schema(implementation = BasicErrorRepresentation.class))
                    ),
                    @ApiResponse(
                            description = "Validation error",
                            responseCode = "400",
                            content = @Content(
                                    schema = @Schema(implementation = ValidationErrorRepresentation.class)
                            )
                    ),
            }
    )
    public ResponseEntity<CardDtoForAdmin> createCardForUser(
            @Valid @RequestBody CardCreateUpdateRequest request
    ) {
        CardDtoForAdmin cardDto = cardMapper.toCardDtoForAdmin(cardService.createCard(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cardDto);
    }

    @PatchMapping("/change-status/{card-id}")
    @PreAuthorize("hasAuthority('change_users_card_status')")
    @Operation(
            summary = "Change user cards status",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Card not found",
                            responseCode = "404",
                            content = @Content(schema = @Schema(implementation = BasicErrorRepresentation.class))
                    ),
                    @ApiResponse(
                            description = "Invalid parameter",
                            responseCode = "400",
                            content = @Content(schema = @Schema(implementation = BasicErrorRepresentation.class))
                    ),
            }
    )
    public ResponseEntity<CardDtoForAdmin> changeCardStatus(
            @PathVariable("card-id") Long cardId,
            @RequestParam Status status
    ) {
        Card card = cardService.changeStatus(cardId, status);
        return ResponseEntity.ok(cardMapper.toCardDtoForAdmin(card));
    }

    @DeleteMapping("/{card-id}")
    @PreAuthorize("hasAuthority('delete_users_card')")
    @Operation(
            summary = "Delete users card",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            description = "Delete not allowed",
                            responseCode = "400",
                            content = @Content(schema = @Schema(implementation = BasicErrorRepresentation.class))
                    )
            }
    )
    public ResponseEntity<Void> deleteCard(
            @PathVariable("card-id") Long cardId
    ){
        cardService.deleteCard(cardId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('deposit_money_on_users_card')")
    @PostMapping("/deposit")
    @Operation(
            summary = "Deposit money on users card",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Provided card not found",
                            responseCode = "404",
                            content = @Content(schema = @Schema(implementation = BasicErrorRepresentation.class))
                    ),
                    @ApiResponse(
                            description = "Deposit not allowed",
                            responseCode = "400",
                            content = @Content(schema = @Schema(implementation = BasicErrorRepresentation.class))
                    )
            }
    )
    public ResponseEntity<CardDtoForAdmin> depositMoney(
            @Valid @RequestBody MoneyDepositRequest depositRequest
    ){
        CardDtoForAdmin dto = cardMapper.toCardDtoForAdmin(cardService.depositMoney(depositRequest));
        return ResponseEntity.ok(dto);
    }

}
