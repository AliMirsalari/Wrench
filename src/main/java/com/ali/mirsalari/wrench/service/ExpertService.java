package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.exception.EmailExistException;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.NotValidEmailException;
import com.ali.mirsalari.wrench.exception.NotValidPasswordException;
import com.ali.mirsalari.wrench.service.base.CrudService;
import com.ali.mirsalari.wrench.service.base.UserService;
import org.springframework.stereotype.Service;

@Service
public interface ExpertService
        extends CrudService<Expert, Long>,
        UserService<Expert, String> {
    Expert approveExpert(Long id) throws NotFoundException, NotValidPasswordException, EmailExistException, NotValidEmailException;
    Expert addSkill(Long skillId, Expert expert) throws NotFoundException, NotValidPasswordException, EmailExistException, NotValidEmailException;
    Expert removeSkill(Long skillId, Expert expert) throws NotFoundException, NotValidPasswordException, EmailExistException, NotValidEmailException;
}
