package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.service.base.BaseUserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ExpertService
        extends BaseUserService<Expert, String> {
    @Transactional
    Expert save(String firstName, String lastName, String email, String password, String imageAddress);

    @Transactional
    Expert update(Long id, String firstName, String lastName, String email, String password, String imageAddress);

    @Transactional
    Expert uncheckedUpdate(Expert expert);

    @Transactional
    void remove(Long id);

    @Transactional
    Optional<Expert> findById(Long id);

    @Transactional
    List<Expert> findAll();


    @Transactional
    Expert approveExpert(Long id);

    @Transactional
    Expert addSkill(Long skillId, Long expertId);

    @Transactional
    Expert removeSkill(Long skillId, Long expertId);

    @Transactional
    void retrieveAndSavePhotoToFile(Long expertId, String filePath);
}
