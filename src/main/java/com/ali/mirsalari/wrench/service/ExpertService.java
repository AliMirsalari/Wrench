package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.service.base.BaseUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ExpertService
        extends BaseUserService<Expert, String> {


    Expert save(String firstName, String lastName, String email, String password, byte[] imageData);

    Expert update(Long id, String firstName, String lastName, String email, String password, byte[] imageData);

    Expert updateWithEntity(Expert expert);

    void remove(Long id);

    Optional<Expert> findById(Long id);

    List<Expert> findAll();


    void approveExpert(Long id);

    void addSkill(Long skillId, Long expertId);

    void removeSkill(Long skillId, Long expertId);

    void retrieveAndSavePhotoToFile(Long expertId, String filePath);

    void sendActivationLink(String email);

    void approveEmail(String email, String token);
}
