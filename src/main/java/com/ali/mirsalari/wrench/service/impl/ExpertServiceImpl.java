package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.entity.Service;
import com.ali.mirsalari.wrench.entity.enumeration.ExpertStatus;
import com.ali.mirsalari.wrench.exception.DuplicateException;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.NotValidEmailException;
import com.ali.mirsalari.wrench.exception.NotValidPasswordException;
import com.ali.mirsalari.wrench.repository.ExpertRepository;
import com.ali.mirsalari.wrench.repository.ImageRepository;
import com.ali.mirsalari.wrench.repository.ServiceRepository;
import com.ali.mirsalari.wrench.service.ExpertService;
import com.ali.mirsalari.wrench.util.Validator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@org.springframework.stereotype.Service
@Transactional
@RequiredArgsConstructor
public class ExpertServiceImpl implements ExpertService {
    private final ExpertRepository expertRepository;
    private final ServiceRepository serviceRepository;
    private final ImageRepository imageRepository;


    @Override
    public Expert save(String firstName, String lastName, String email, String password, String imageAddress){
        Expert expert = new Expert(firstName, lastName, email, password, imageAddress);
        validation(expert);
        return expertRepository.save(expert);
    }

    @Override
    public Expert update(Long id, String firstName, String lastName, String email, String password, String imageAddress) {
        Expert expert = findById(id).orElseThrow(() -> new NotFoundException("Expert is not found!"));
        expert.setFirstName(firstName);
        expert.setLastName(lastName);
        expert.setEmail(email);
        expert.setPassword(password);
        expert.setImageData(imageAddress);
        return expertRepository.save(expert);
    }
    @Override
    public Expert uncheckedUpdate(Expert expert) {
        return  expertRepository.save(expert);
    }

    @Override
    public void remove(Long id) {
        expertRepository.deleteById(id);
    }

    @Override
    public Optional<Expert> findById(Long id) {
        return expertRepository.findById(id);
    }

    @Override
    public List<Expert> findAll() {
        return expertRepository.findAll();
    }

    @Override
    public Optional<Expert> findByEmail(String email) {
        return expertRepository.findExpertByEmail(email);
    }

    @Override
    public Expert changePassword(String newPassword, String oldPassword, Long userId) {
        Expert expert = expertRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Expert with ID " + userId + " is not found."));
        if (!Objects.equals(expert.getPassword(), oldPassword)) {
            throw new NotValidPasswordException("The entered password is not the same as the password!");
        }
        expert.setPassword(newPassword);
        return uncheckedUpdate(expert);
    }

    @Override
    public Expert approveExpert(Long id) {
        Expert expert = findById(id).orElseThrow(() -> new NotFoundException("Expert is not found!"));
        expert.setExpertStatus(ExpertStatus.CONFIRMED);
        return uncheckedUpdate(expert);
    }

    @Override
    public Expert addSkill(Long skillId, Long expertId) {
        Service skill = serviceRepository.findById(skillId).orElseThrow(()-> new NotFoundException("Skill is not found!"));
        Expert expert = findById(expertId).orElseThrow(()->new NotFoundException("Expert is not found!"));
        expert.getSkills().add(skill);
        return expertRepository.save(expert);
    }


    @Override
    public Expert removeSkill(Long skillId, Long expertId){
        Service skill = serviceRepository.findById(skillId).orElseThrow(()-> new NotFoundException("Skill is not found!"));
        Expert expert = findById(expertId).orElseThrow(()->new NotFoundException("Expert is not found!"));

        if (!expert.getSkills().contains(skill)) {
            throw new NotFoundException("Skill is not found!");
        }
        expert.getSkills().remove(skill);
        return expertRepository.save(expert);
    }

    @Override
    public void retrieveAndSavePhotoToFile(Long expertId, String filePath) {
        Expert expert = findById(expertId).orElseThrow(()->new NotFoundException("Expert is not found!"));
         byte[] photoData = expert.getImageData();
            if (photoData != null) {
                    imageRepository.writePhotoToFile(filePath,photoData);
            } else {
                throw new NotFoundException("Photo data is null for the expert.");
            }
    }

    @Override
    public void validation(Expert expert) {
        if (findByEmail(expert.getEmail()).isPresent()) {
            throw new DuplicateException("Email already exists");
        }
        if (!Validator.isValidPassword(expert.getPassword())) {
            throw new NotValidPasswordException("Password is not good!");
        }
        if (!Validator.isValidEmail(expert.getEmail())) {
            throw new NotValidEmailException("Email is not good!");
        }
    }

}
