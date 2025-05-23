package com.example.bankcards.service.impl;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.dto.UserSummaryDtoForAdmin;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.repository.specification.UserSpecification;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public Page<User> searchUsers(String query, Pageable pageable) {
        Specification<User> specification = Specification
                .where(UserSpecification.hasEmailLike(query))
                .or(UserSpecification.hasFirstNameLike(query))
                .or(UserSpecification.hasLastNameLike(query))
                .and(UserSpecification.hasRoleEqual(Role.USER));
        return userRepository.findAll(specification, pageable);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not foud with the provided id: " + userId));
    }
}
