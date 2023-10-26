package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.entity.User;
import com.ali.mirsalari.wrench.service.UserService;
import com.ali.mirsalari.wrench.service.dto.UserSearchCriteria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        if ("Expert".equals(searchCriteria.role()) && searchCriteria.skillsId() != null) {
            List<Long> skillIds = searchCriteria.skillsId();

            if (skillIds.size() > 1) {
                List<Predicate> skillPredicates = new ArrayList<>();

                for (Long skillId : skillIds) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<com.ali.mirsalari.wrench.entity.Service> serviceRoot = subquery.from(com.ali.mirsalari.wrench.entity.Service.class);
                    Join<com.ali.mirsalari.wrench.entity.Service, Expert> expertServiceJoin = serviceRoot.join("experts");

                    subquery.select(serviceRoot.get("id"));
                    subquery.where(
                            expertServiceJoin.get("skills").get("id").in(skillId)
                    );

                    skillPredicates.add(builder.exists(subquery));
                }

                predicate = builder.and(predicate, builder.and(skillPredicates.toArray(new Predicate[0])));
            } else {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<com.ali.mirsalari.wrench.entity.Service> serviceRoot = subquery.from(com.ali.mirsalari.wrench.entity.Service.class);
                Join<com.ali.mirsalari.wrench.entity.Service, Expert> expertServiceJoin = serviceRoot.join("experts");

                subquery.select(serviceRoot.get("id"));
                subquery.where(
                        expertServiceJoin.get("skills").get("id").in(skillIds.get(0))
                );

                predicate = builder.and(predicate, builder.exists(subquery));
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