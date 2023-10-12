package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.entity.Service;
import com.ali.mirsalari.wrench.entity.enumeration.ExpertStatus;
import com.ali.mirsalari.wrench.exception.EmailExistException;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.NotValidEmailException;
import com.ali.mirsalari.wrench.exception.NotValidPasswordException;
import com.ali.mirsalari.wrench.repository.ExpertRepository;
import com.ali.mirsalari.wrench.service.ExpertService;
import com.ali.mirsalari.wrench.service.ServiceService;
import com.ali.mirsalari.wrench.util.Validator;
import jakarta.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ExpertServiceImpl implements ExpertService {
    private final ExpertRepository expertRepository;
    private final ServiceService serviceService;


    @Override
    @Transactional
    public Expert save(Expert expert) throws EmailExistException, NotValidPasswordException, NotValidEmailException {
        if (findByEmail(expert.getEmail()).isPresent()) {
            throw new EmailExistException("Email already exists");
        }
        if (!Validator.isValidPassword(expert.getPassword())) {
            throw new NotValidPasswordException("Password is not valid!");
        }
        if (!Validator.isValidEmail(expert.getEmail())) {
            throw new NotValidEmailException("Email is not good!");
        }

        return expertRepository.save(expert);

    }

    @Override
    @Transactional
    public Expert update(Expert expert) throws NotFoundException, NotValidPasswordException, NotValidEmailException {
        if (expert.getId() == null || expertRepository.findById(expert.getId()).isEmpty()) {
            throw new NotFoundException("Expert with id: " + expert.getId() + " is not found.");
        }
        if (!Validator.isValidPassword(expert.getPassword())) {
            throw new NotValidPasswordException("Password is not valid!");
        }
        if (!Validator.isValidEmail(expert.getEmail())) {
            throw new NotValidEmailException("Email is not good!");
        }

            return expertRepository.save(expert);

    }

    @Override
    @Transactional
    public void remove(Long id) {
            expertRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<Expert> findById(Long id) {
            return expertRepository.findById(id);
    }

    @Override
    @Transactional
    public List<Expert> findAll() {
        return expertRepository.findAll();
    }
    @Override
    @Transactional
    public Optional<Expert> findByEmail(String email) {
            return expertRepository.findExpertByEmail(email);
    }

    @Override
    @Transactional
    public Expert changePassword(String newPassword, Expert expert) throws NotFoundException, NotValidPasswordException, NotValidEmailException {
        if (expert.getId() == null || expertRepository.findById(expert.getId()).isEmpty()) {
            throw new NotFoundException("Expert with id: " + expert.getId() + " is not found.");
        }
        if (!Validator.isValidPassword(newPassword)) {
            throw new NotValidPasswordException("Password is not valid!");
        }
        expert.setPassword(newPassword);
        return update(expert);
    }

    @Override
    @Transactional
    public Expert approveExpert(Long id) throws NotFoundException, NotValidPasswordException, EmailExistException, NotValidEmailException {
        Optional<Expert> optionalExpert = findById(id);
        if (optionalExpert.isEmpty()) {
            throw new NotFoundException("Expert is not found!");
        }

        Expert expert = optionalExpert.get();
        expert.setExpertStatus(ExpertStatus.CONFIRMED);
        return update(expert);
    }

    @Override
    @Transactional
    public Expert addSkill(Long skillId, Expert expert) throws NotFoundException, NotValidPasswordException, EmailExistException, NotValidEmailException {
        Optional<Service> skillOptional = serviceService.findById(skillId);
        if (skillOptional.isEmpty()) {
            throw new NotFoundException("Skill is not found!");
        }
        if (expert == null) {
            throw new NotFoundException("Expert is not found!");
        }

        Service skill = skillOptional.get();
        expert.getSkills().add(skill);
        return update(expert);
    }


    @Override
    @Transactional
    public Expert removeSkill(Long skillId, Expert expert) throws NotFoundException, NotValidPasswordException, EmailExistException, NotValidEmailException {
        Optional<Service> skillOptional = serviceService.findById(skillId);
        if (skillOptional.isEmpty()) {
            throw new NotFoundException("Skill is not found!");
        }
        if (expert == null) {
            throw new NotFoundException("Expert is not found!");
        }

        Service skill = skillOptional.get(); // Extract the skill from the Optional

        if (expert.getSkills().contains(skill)) {
            expert.getSkills().remove(skill);
        } else {
            throw new NotFoundException("Skill is not found!");
        }
        return update(expert);
    }



}
