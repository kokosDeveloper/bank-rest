package com.example.bankcards.repository.specification;

import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> hasEmailLike(String email){
        return (root, query, cb) -> {
            if (email == null)
                return null;
            return cb.like(root.get("email"), "%" + email + "%");
        };
    }
    public static Specification<User> hasFirstNameLike(String firstName){
        return (root, query, cb) -> {
            if (firstName == null)
                return null;
            return cb.like(root.get("firstName"), "%" + firstName + "%");
        };
    }
    public static Specification<User> hasLastNameLike(String lastName){
        return (root, query, cb) -> {
            if (lastName == null)
                return null;
            return cb.like(root.get("firstName"), "%" + lastName + "%");
        };
    }
    public static Specification<User> hasRoleEqual(Role role){
        return (root, query, cb) -> {
            if (role == null)
                return null;
            return cb.like(root.get("role"), "%" + role + "%");
        };
    }

}
