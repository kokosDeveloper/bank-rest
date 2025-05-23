package com.example.bankcards.repository.specification;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Status;
import org.springframework.data.jpa.domain.Specification;

public class CardSpecification {
    public static Specification<Card> hasLastFourDigits(String last4){
        return (root, query, cb) -> {
            if (last4 == null || last4.isEmpty())
                return null;
            return cb.like(root.get("number"), "%" + last4);
        };
    }
    public static Specification<Card> hasUserId(Long userId){
        return (root, query, cb) -> {
          if (userId == null)
              return null;
          return cb.equal(root.get("user").get("id"), userId);
        };
    }
    public static Specification<Card> hasStatus(Status status){
        return (root, query, cb) -> {
            if (status == null)
                return null;
            return cb.equal(root.get("status"), status);
        };
    }
}
