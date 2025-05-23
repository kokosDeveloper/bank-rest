package com.example.bankcards.controller;

import com.example.bankcards.config.openApi.BasicErrorRepresentation;
import com.example.bankcards.config.openApi.ValidationErrorRepresentation;
import com.example.bankcards.dto.CardBlockRequestDto;
import com.example.bankcards.dto.CardBlockRequestResponseDtoForAdmin;
import com.example.bankcards.dto.CardBlockRequestResponseDtoForUser;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.mapper.CardBlockRequestMapper;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards/block")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Block cards")
@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content())
@ApiResponse(description = "Forbidden", responseCode = "403", content = @Content())
public class BlockCardController {
    private final CardService cardService;
    private final CardBlockRequestMapper cardBlockRequestMapper;

    @PreAuthorize("hasAuthority('request_block_card_which_possess')")
    @PostMapping("/request")
    @Operation(
            summary = "Request for blocking card",
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
                            description = "Card for block not found",
                            responseCode = "404",
                            content = @Content(
                                    schema = @Schema(implementation = BasicErrorRepresentation.class)
                            )
                    )
            }
    )
    public ResponseEntity<CardBlockRequestResponseDtoForUser> requestBlockCard(
            @Valid @RequestBody CardBlockRequestDto blockRequestDto,
            Authentication authentication
    ){
        CardBlockRequest blockRequest = cardService.requestBlockCard(blockRequestDto, authentication);
        return ResponseEntity.ok(cardBlockRequestMapper.toDtoForUser(blockRequest));
    }

    @PreAuthorize("hasAuthority('request_block_card_which_possess')")
    @GetMapping("/my-requests")
    @Operation(
            summary = "Get users requested for block cards",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    )
            }
    )
    public List<CardBlockRequestResponseDtoForUser> getUsersRequestsForBlockCard(
            Authentication authentication
    ){
        List<CardBlockRequest> blockRequests = cardService.getUsersRequestsForBlock(authentication);
        return blockRequests.stream()
                .map(cardBlockRequestMapper::toDtoForUser)
                .toList();
    }
    @PreAuthorize("hasAuthority('change_users_card_status')")
    @GetMapping("/pending-block-requests")
    @Operation(
            summary = "Get all requested for block cards",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    )
            }
    )
    public Page<CardBlockRequestResponseDtoForAdmin> getPendingBlockRequests(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ){
        Page<CardBlockRequest> blockRequests = cardService.getPendingBlockRequests(
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "requestedAt"))
        );
        return blockRequests.map(cardBlockRequestMapper::toDtoForAdmin);
    }

    @PreAuthorize("hasAuthority('change_users_card_status')")
    @PostMapping("/process/{block-request-id}")
    @Operation(
            summary = "Process requested for block cards",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Block request not found",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Already processed",
                            responseCode = "400"
                    )
            }
    )
    public ResponseEntity<CardBlockRequestResponseDtoForAdmin> processBlockRequest(
            @PathVariable("block-request-id") Long id,
            @RequestParam boolean approve
    ){
        CardBlockRequest blockRequest = cardService.processBlockRequest(id, approve);
        return ResponseEntity.ok(cardBlockRequestMapper.toDtoForAdmin(blockRequest));
    }

}
