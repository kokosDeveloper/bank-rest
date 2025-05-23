package com.example.bankcards.service.mapper;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.dto.UserSummaryDtoForAdmin;
import com.example.bankcards.entity.User;

public interface UserMapper {
    UserSummaryDtoForAdmin toUserSummaryDto(User user);
    UserDto toUserDto(User user);
}
