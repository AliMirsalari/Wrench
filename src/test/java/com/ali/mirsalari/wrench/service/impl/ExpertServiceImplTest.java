package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.entity.Service;
import com.ali.mirsalari.wrench.entity.enumeration.ExpertStatus;
import com.ali.mirsalari.wrench.exception.*;
import com.ali.mirsalari.wrench.repository.ExpertRepository;
import com.ali.mirsalari.wrench.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpertServiceImplTest {

    @Mock
    private ExpertRepository expertRepository;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private ServiceServiceImpl serviceService;
    @InjectMocks
    private ExpertServiceImpl underTest;
    private Expert expert;
    private Service service;
    @BeforeEach
    void setUp() {
        expert = new Expert(
                "Hashem",
                "Momen",
                "hashem@gmail.com",
                "4s6]5rf4FSDF#%#",
                0L,
                Instant.now(),
                0,
                new HashSet<>(),
                null,
                null
        );
        service = new Service(7L,
                "IT",
                1_000_000L,
                "IT services",
                null,
                null,
                null);
        Set <Service> services = expert.getSkills();
        services.add(service);
    }


    @Test
    void itShouldSaveAnExpert() {
        //Arrange
        when(expertRepository.save(any())).thenReturn(expert);
        //Act
        Expert tempExpert = underTest.save(expert);
        //Assert
        assertNotNull(tempExpert);
    }

    @Test
    void itShouldThrowEmailExistException() {
        //Arrange
        when(expertRepository.findExpertByEmail(any())).thenReturn(Optional.ofNullable(expert));
        //Act and Assert
        assertThrows(DuplicateException.class,() ->{
            underTest.save(expert);
        });
    }
    @Test
    void itShouldThrowNotValidPasswordException() {

        expert.setPassword("AAAA");
        //Act and Assert
        assertThrows(NotValidPasswordException.class,() ->{
            underTest.save(expert);
        });
    }
    @Test
    void itShouldThrowNotValidEmailException() {

        expert.setEmail("AAAA");
        //Act and Assert
        assertThrows(NotValidEmailException.class,() ->{
            underTest.save(expert);
        });
    }

    @Test
    void itShouldUpdateAnExpert() {
        //Arrange
        when(expertRepository.save(any())).thenReturn(expert);
        //Act
        Expert tempExpert = underTest.update(expert);
        //Assert
        assertNotNull(tempExpert);
    }

    @Test
    void itShouldDeleteExpertById() {
        //Act
        underTest.remove(1L);
        //Assert
        verify(expertRepository, times(1)).deleteById(1L);
    }
    @Test
    void itShouldFindAnExpertById() {
        //Arrange
        when(expertRepository.findById(any())).thenReturn(Optional.ofNullable(expert));
        //Act and Assert
        if (underTest.findById(1L).isPresent()) {
            Expert tempExpert = underTest.findById(1L).get();
            assertNotNull(tempExpert);
        }
    }

    @Test
    void itShouldFindAnExpertByEmail() {
        //Arrange
        when(expertRepository.findExpertByEmail(any())).thenReturn(Optional.ofNullable(expert));
        //Act and Assert
        if (underTest.findByEmail(expert.getEmail()).isPresent()) {
            Expert tempExpert = underTest.findByEmail(expert.getEmail()).get();
            assertNotNull(tempExpert);
        }
    }

    @Test
    void itShouldFindExperts() {
        //Arrange
        when(expertRepository.findAll()).thenReturn(List.of(expert,expert,expert));
        //Act
        List<Expert> experts = underTest.findAll();
        //Assert
        assertNotNull(experts);
    }

    @Test
    void itShouldThrowNotFoundException() {
//        //Arrange
        expert.setId(null);
        //Act and Assert
        assertThrows(NotFoundException.class, () -> {
            underTest.changePassword(expert.getPassword(), expert);
        });
    }

    @Test
    void itShouldThrowNotValidPasswordExceptionWhenNewPasswordIsNotValid() {
        //Arrange
        when(expertRepository.findById(any())).thenReturn(Optional.ofNullable(expert));
        expert.setId(1L);
        //Act and Assert
        assertThrows(NotValidPasswordException.class, () -> {
            underTest.changePassword("aaaa", expert);
        });
    }
    @Test
    void itShouldChangeThePassword() {
        //Arrange
        when(expertRepository.findById(any())).thenReturn(Optional.ofNullable(expert));
        expert.setId(1L);
        //Act
        String newPassword = "sdf54s]64SFDS%%#";
        underTest.changePassword(newPassword, expert);
//      Assert
        assertEquals(newPassword, expert.getPassword());
    }

    @Test
    void itShouldThrowNotFoundExceptionInApproveExpert() {
        //Arrange
        when(expertRepository.findById(any())).thenReturn(Optional.empty());
        //Act and Assert
        assertThrows(NotFoundException.class, () -> {
            underTest.approveExpert(1L);
        });
    }

    @Test
    void itShouldApproveAnExpert() {
        //Arrange
        when(expertRepository.findById(any())).thenReturn(Optional.ofNullable(expert));
        //Act
        underTest.approveExpert(1L);
        //Assert
        assertEquals(ExpertStatus.CONFIRMED, expert.getExpertStatus());
    }
    @Test
    void itShouldThrowNotFoundExceptionInAddSkillWhenSkillIsNotFound() {
        //Arrange
        when(serviceService.findById(any())).thenReturn(Optional.empty());
        //Act and Assert
        assertThrows(NotFoundException.class, () -> {
            underTest.addSkill(7L, expert);
        });
    }

    @Test
    void itShouldThrowNotFoundExceptionInAddSkillWhenExpertIsNull() {
        //Arrange
        when(serviceService.findById(any())).thenReturn(Optional.of(new Service()));
        //Act and Assert
        assertThrows(NotFoundException.class, () -> {
            underTest.addSkill(7L, null);
        });

    }

    @Test
    void itShouldAddSkill() {
        //Arrange
        when(serviceService.findById(any())).thenReturn(Optional.of(service));
        when(expertRepository.save(any())).thenAnswer((callMethod)
                -> callMethod.getArgument(0, Expert.class));
        //Act
        Expert tempExpert = underTest.addSkill(7L, expert);
        //Assert
        assertNotNull(tempExpert);
        assertNotNull(tempExpert.getSkills());
    }

    @Test
    void itShouldThrowNotFoundExceptionInRemoveSkillWhenSkillIsNotFound() {
        //Arrange
        when(serviceService.findById(any())).thenReturn(Optional.empty());
        //Act and Assert
        assertThrows(NotFoundException.class, () -> {
            underTest.removeSkill(7L, expert);
        });
    }

    @Test
    void itShouldThrowNotFoundExceptionInRemoveSkillWhenExpertIsNull() {
        //Arrange
        when(serviceService.findById(any())).thenReturn(Optional.of(new Service()));
        //Act and Assert
        assertThrows(NotFoundException.class, () -> {
            underTest.removeSkill(7L, null);
        });

    }
    @Test
    void itShouldRemoveSkill() {
        //Arrange
        when(serviceService.findById(any())).thenReturn(Optional.of(service));
        when(expertRepository.save(any())).thenAnswer((callMethod)
                -> callMethod.getArgument(0, Expert.class));
        //Act
        Expert tempExpert = underTest.removeSkill(7L, expert);
        //Assert
        assertNotNull(tempExpert);
        assertTrue(tempExpert.getSkills().isEmpty());
    }
    @Test
    void itShouldThrowNotFoundExceptionInRemoveSkill() {
        //Arrange
        Set<Service> skills = expert.getSkills();
        skills.remove(service);
        expert.setSkills(skills);
        when(serviceService.findById(any())).thenReturn(Optional.of(service));
        //Act and Assert
        assertThrows(NotFoundException.class,() ->{
            underTest.removeSkill(7L, expert);
        });
    }

    @Test
    void itShouldThrowNotFoundExceptionWhenExpertIsNull() {
        //Act and Assert
        assertThrows(NotFoundException.class, () -> {
            underTest.retrieveAndSavePhotoToFile(null,
                    "src/main/resources/images/Avatar.jpg");
        });
    }

    @Test
    void itShouldGetRetrievePhotoData() {
        //Arrange
        expert.setImageData("src/main/resources/images/Avatar.jpg");
        //Act
        underTest.retrieveAndSavePhotoToFile(expert,
                "src/main/resources/images/Avatar.jpg");
        //Assert
        assertNotNull(expert.getImageData());
    }
    @Test
    void itShouldThrowNotFoundExceptionWhenPhotoDataIsNull() {
        //Act and Assert
        assertThrows(NotFoundException.class, () -> {
            underTest.retrieveAndSavePhotoToFile(expert,
                    "src/main/resources/images/Avatar.jpg");
        });
    }
}