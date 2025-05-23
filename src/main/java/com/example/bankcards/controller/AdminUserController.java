package com.example.bankcards.controller;

import com.example.bankcards.config.openApi.BasicErrorRepresentation;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.dto.UserSummaryDtoForAdmin;
import com.example.bankcards.service.UserService;
import com.example.bankcards.service.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User management")
@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content())
@ApiResponse(description = "Forbidden", responseCode = "403", content = @Content())
public class AdminUserController {
    private final UserService userService;
    private final UserMapper userMapper;
    @PreAuthorize("hasAuthority('see_all_users')")
    @GetMapping
    @Operation(
            summary = "Search users",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    )
            }
    )
    public Page<UserSummaryDtoForAdmin> searchUser(
            @Parameter(
                    description = "Email, first name or last name"
            )
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ){
        return userService.searchUsers(query, PageRequest.of(page, size))
                .map(userMapper::toUserSummaryDto);
    }


    @PreAuthorize("hasAuthority('see_all_cards')")
    @GetMapping("/{user-id}")
    @Operation(
            summary = "Get full user with cards by id",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    )
            }
    )
    public ResponseEntity<UserDto> getUserWithCardsById(
            @PathVariable("user-id") Long userId
    ){
        return ResponseEntity.ok(userMapper.toUserDto(userService.getUserById(userId)));
    }
}
