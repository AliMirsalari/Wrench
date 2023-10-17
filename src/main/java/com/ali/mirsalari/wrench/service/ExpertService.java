package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.exception.DuplicateException;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.NotValidEmailException;
import com.ali.mirsalari.wrench.exception.NotValidPasswordException;
import com.ali.mirsalari.wrench.service.base.CrudService;
import com.ali.mirsalari.wrench.service.base.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public interface ExpertService
        extends CrudService<Expert, Long>,
        UserService<Expert, String> {
    @Transactional
    Expert approveExpert(Long id);
    @Transactional
    Expert addSkill(Long skillId, Expert expert);
    @Transactional
    Expert removeSkill(Long skillId, Expert expert);
    @Transactional
    void retrieveAndSavePhotoToFile(Expert expert, String filePath);
}
