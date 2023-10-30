package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.controller.dto.UserSearchCriteria;
import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.entity.User;
import com.ali.mirsalari.wrench.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> searchUsers(UserSearchCriteria searchCriteria) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        Predicate predicate = builder.conjunction();

        if (searchCriteria.role() != null) {
            predicate = builder.and(predicate, builder.equal(root.type(), builder.literal(searchCriteria.role())));
        }
        if (searchCriteria.firstName() != null) {
            predicate = builder.and(predicate, builder.equal(root.get("firstName"), searchCriteria.firstName()));
        }
        if (searchCriteria.lastName() != null) {
            predicate = builder.and(predicate, builder.equal(root.get("lastName"), searchCriteria.lastName()));
        }
        if (searchCriteria.email() != null) {
            predicate = builder.and(predicate, builder.equal(root.get("email"), searchCriteria.email()));
        }

        if ("Expert".equals(searchCriteria.role())) {
            if (searchCriteria.skillsId() != null) {
                List<Long> skillIds = searchCriteria.skillsId();
                Join<Expert, com.ali.mirsalari.wrench.entity.Service> skills = root.join("skills");
                Expression<Long> id = skills.get("id");
                predicate = builder.and(predicate, id.in(skillIds));
                query.groupBy(root.get("id"), root.type());
                query.having(builder.equal(builder.count(root.get("id")), skillIds.size()));
            }
            if (searchCriteria.minScore() != null) {
                predicate = builder.and(predicate, builder.greaterThanOrEqualTo(root.get("score"), searchCriteria.minScore()));
            }
            if (searchCriteria.maxScore() != null) {
                predicate = builder.and(predicate, builder.lessThanOrEqualTo(root.get("score"), searchCriteria.maxScore()));
            }
        }

        query.where(predicate);
        return entityManager.createQuery(query).getResultList();
    }

}