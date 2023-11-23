package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.entity.Service;
import com.ali.mirsalari.wrench.entity.UnapprovedEmail;
import com.ali.mirsalari.wrench.entity.enumeration.ExpertStatus;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.NotValidPasswordException;
import com.ali.mirsalari.wrench.repository.ExpertRepository;
import com.ali.mirsalari.wrench.repository.ImageRepository;
import com.ali.mirsalari.wrench.repository.UnapprovedEmailRepository;
import com.ali.mirsalari.wrench.service.EmailService;
import com.ali.mirsalari.wrench.service.ServiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ExpertServiceImplTest {
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ExpertRepository expertRepository;
    @Mock
    private UserDetails userDetails;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ServiceService serviceService;
    @Mock
    private EmailService emailService;
    @Mock
    private UnapprovedEmailRepository unapprovedEmailRepository;
    @InjectMocks
    private ExpertServiceImpl underTest;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String bcryptPassword;
    private Expert expert;

    @BeforeEach
    void setUp() {
        firstName = "Seyyed Ali";
        lastName = "Mirsalari";
        email = "alimirsalari@outlook.com";
        password = "dfs456SDFS%#$";
        bcryptPassword = "$2a$12$ATUfejDerhp2LeDLbp1lAO2Px0W5vduUM770NV1GIRcKx/5DaTNMa";
        expert = new Expert(firstName, lastName, email, password, null);
    }

    @Test
    void itShouldSaveExpert() {
        //Arrange
        when(expertRepository.save(any())).thenReturn(expert);
        //Act
        Expert savedExpert = underTest.save(firstName, lastName, email, password,null);
        //Assert
        assertNotNull(savedExpert);
        assertEquals(expert.getFirstName(), savedExpert.getFirstName());
        assertEquals(expert.getLastName(), savedExpert.getLastName());
        assertEquals(expert.getEmail(), savedExpert.getEmail());
        assertEquals(expert.getPassword(), savedExpert.getPassword());
        verify(expertRepository, times(1)).save(any());
    }

    @Test
    void itShouldUpdateExpert() {
        //Arrange
        expert.setId(1L);
        when(userDetails.getUsername()).thenReturn(email);
        when(expertRepository.findExpertByEmail(email)).thenReturn(Optional.ofNullable(expert));
        when(passwordEncoder.encode(password)).thenReturn(bcryptPassword);
        when(expertRepository.save(any())).thenReturn(expert);

        String newFirstName = "Reza";
        String newLastName = "Ahmadi";
        String newEmail = "reza@gmail.com";

        //Act
        Expert updatedExpert = underTest.update(newFirstName, newLastName, newEmail, password, null, userDetails);
        //Assert
        assertEquals(newFirstName, updatedExpert.getFirstName());
        assertEquals(newLastName, updatedExpert.getLastName());
        assertEquals(newEmail, updatedExpert.getEmail());
        assertEquals(bcryptPassword, updatedExpert.getPassword());
    }

    @Test
    void itShouldUpdateExpertWithEntity() {
        //Arrange
        when(expertRepository.save(any())).thenReturn(expert);
        //Act
        underTest.updateWithEntity(expert);
        //Assert
        verify(passwordEncoder, times(1)).encode(any());
    }

    @Test
    void itShouldRemoveExpert() {
        //Act
        underTest.remove(any());
        //Assert
        verify(expertRepository, times(1)).deleteById(any());
    }

    @Test
    void itShouldFindExpertById() {
        //Arrange
        when(expertRepository.findById(any())).thenReturn(Optional.ofNullable(expert));
        //Act
        underTest.findById(any());
        //Assert
        verify(expertRepository, times(1)).findById(any());
    }

    @Test
    void itShouldFindAllExperts() {
        //Arrange
        when(expertRepository.findAll()).thenReturn(List.of(expert));
        //Act
        underTest.findAll();
        //Assert
        verify(expertRepository, times(1)).findAll();
    }

    @Test
    void itShouldThrowNotValidPasswordException() {
        //Arrange
        when(expertRepository.findExpertByEmail(any())).thenReturn(Optional.ofNullable(expert));
        //Act and Assert
        assertThrows(NotValidPasswordException.class, () ->
                underTest.changePassword(
                        "d4fs4]56FSD%#",
                        "456fdsf$^^$H",
                        email));
    }

    @Test
    void itShouldChangePassword() {
        //Arrange
        when(expertRepository.findExpertByEmail(any())).thenReturn(Optional.ofNullable(expert));
        String newPassword = "456fdsf$^^$H";
        //Act
        underTest.changePassword(newPassword,
                password,
                email);
        //Assert
        verify(expertRepository, times(1)).findExpertByEmail(email);
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(expertRepository, times(1)).save(expert);
    }

    @Test
    void itShouldApproveExpert() {
        //Arrange
        when(expertRepository.findById(any())).thenReturn(Optional.ofNullable(expert));
        //Act
        underTest.approveExpert(any());
        //Assert
        verify(expertRepository, times(1)).save(any());
    }

    @Test
    void itShouldAddSkill() {
        //Arrange
        Service skill = new Service();
        when(serviceService.findById(any())).thenReturn(skill);
        when(expertRepository.findById(any())).thenReturn(Optional.ofNullable(expert));
        //Act
        underTest.addSkill(7L, 1L);
        //Assert
        verify(expertRepository, times(1)).save(any());
    }

    @Test
    void itShouldThrowNotFoundException() {
        //Arrange
        Service skill = new Service();
        when(serviceService.findById(any())).thenReturn(skill);
        when(expertRepository.findById(any())).thenReturn(Optional.ofNullable(expert));
        //Act and Assert
        assertThrows(NotFoundException.class, ()-> underTest.removeSkill(7L, 1L));
    }
    @Test
    void itShouldRemoveSkill() {
        //Arrange
        Service skill = new Service();
        expert.setSkills(Set.of(skill));
        when(serviceService.findById(any())).thenReturn(skill);
        when(expertRepository.findById(any())).thenReturn(Optional.ofNullable(expert));

        //Act
        underTest.removeSkill(7L, 1L);
        //Assert
        verify(expertRepository, times(1)).save(any());
    }

    @Test
    void itShouldRetrieveAndSavePhotoToFile() {
        //Arrange
        when(expertRepository.findById(any())).thenReturn(Optional.ofNullable(expert));
        byte[] samplePhotoData = {(byte) 0x89, 'P', 'N', 'G'};
        expert.setImageData(samplePhotoData);
        //Act
        long expertId = 1L;
        String filePath = "test/photo.jpg";
        underTest.retrieveAndSavePhotoToFile(expertId, filePath);
        //Assert
        verify(expertRepository).findById(any());
        verify(imageRepository).writePhotoToFile(filePath, samplePhotoData);
    }
    @Test
    public void testRetrieveAndSavePhotoToFileWithNullPhotoData() {
        //Arrange
        Long expertId = 1L;
        String filePath = "test/photo.jpg";

        Expert expert = new Expert();
        expert.setId(expertId);
        expert.setImageData(null);

        when(expertRepository.findById(expertId)).thenReturn(java.util.Optional.of(expert));

        //Act and Assert
        assertThrows(NotFoundException.class,
                () -> underTest.retrieveAndSavePhotoToFile(expertId, filePath));

        verify(expertRepository).findById(expertId);

        verifyNoInteractions(imageRepository);
    }
    @Test
    void itShouldSendActivationLink() {
        // Arrange
        String email = "test@example.com";
        Expert expert = new Expert();
        expert.setEmail(email);
        expert.setExpertStatus(ExpertStatus.AWAITING_CONFIRMATION);

        when(expertRepository.findExpertByEmail(email)).thenReturn(Optional.of(expert));
        when(unapprovedEmailRepository.findUnapprovedEmailByEmail(email)).thenReturn(Optional.of(new UnapprovedEmail()));
        doNothing().when(unapprovedEmailRepository).removeByEmail(email);
        doNothing().when(emailService).sendSimpleMessage(anyString(), anyString());

        // Act
        underTest.sendActivationLink(email);

        // Assert
        verify(expertRepository, times(1)).findExpertByEmail(email);
        verify(unapprovedEmailRepository, times(1)).findUnapprovedEmailByEmail(email);
        verify(unapprovedEmailRepository, times(1)).removeByEmail(email);
        verify(unapprovedEmailRepository, times(1)).save(any(UnapprovedEmail.class));
        verify(emailService, times(1)).sendSimpleMessage(eq(email), anyString());
    }

    @Test
    void itShouldThrowIllegalStateExceptionWhenExpertIsAlreadyConfirmed() {
        // Arrange
        String email = "test@example.com";
        Expert expert = new Expert();
        expert.setEmail(email);
        expert.setExpertStatus(ExpertStatus.CONFIRMED);

        when(expertRepository.findExpertByEmail(email)).thenReturn(Optional.of(expert));

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> underTest.sendActivationLink(email));

        // Verify
        verify(expertRepository, times(1)).findExpertByEmail(email);
        verify(unapprovedEmailRepository, never()).findUnapprovedEmailByEmail(email);
        verify(unapprovedEmailRepository, never()).removeByEmail(email);
        verify(unapprovedEmailRepository, never()).save(any(UnapprovedEmail.class));
        verify(emailService, never()).sendSimpleMessage(eq(email), anyString());
    }

    @Test
    void itShouldResendActivationLinkWithOldToken() {
        // Arrange
        String email = "test@example.com";
        Expert expert = new Expert();
        expert.setEmail(email);
        expert.setExpertStatus(ExpertStatus.AWAITING_CONFIRMATION);

        UnapprovedEmail oldToken = new UnapprovedEmail(email, "oldToken");
        when(expertRepository.findExpertByEmail(email)).thenReturn(Optional.of(expert));
        when(unapprovedEmailRepository.findUnapprovedEmailByEmail(email)).thenReturn(Optional.of(oldToken));
        doNothing().when(unapprovedEmailRepository).removeByEmail(email);
        doNothing().when(emailService).sendSimpleMessage(anyString(), anyString());

        // Act
        underTest.sendActivationLink(email);

        // Assert
        verify(expertRepository, times(1)).findExpertByEmail(email);
        verify(unapprovedEmailRepository, times(1)).findUnapprovedEmailByEmail(email);
        verify(unapprovedEmailRepository, times(1)).removeByEmail(email);
        verify(unapprovedEmailRepository, times(1)).save(any(UnapprovedEmail.class));
        verify(emailService, times(1)).sendSimpleMessage(eq(email), anyString());
    }


    @Test
    void itShouldApproveEmail() {
        // Arrange
        String email = "test@example.com";
        String token = "validToken";

        UnapprovedEmail unapprovedEmail = new UnapprovedEmail(email, token);
        when(unapprovedEmailRepository.findUnapprovedEmailByEmail(email)).thenReturn(Optional.of(unapprovedEmail));

        Expert expert = new Expert();
        when(expertRepository.findExpertByEmail(email)).thenReturn(Optional.of(expert));
        when(expertRepository.save(any())).thenReturn(expert);

        // Act
        underTest.approveEmail(email, token);

        // Assert
        verify(unapprovedEmailRepository, times(1)).findUnapprovedEmailByEmail(email);
        verify(unapprovedEmailRepository, times(1)).removeByEmail(email);
        verify(expertRepository, times(1)).findExpertByEmail(email);
        verify(expertRepository, times(1)).save(expert);
    }

    @Test
    void itShouldThrowNotFoundExceptionIfUnapprovedEmailNotFound() {
        // Arrange
        String email = "test@example.com";
        String token = "validToken";

        when(unapprovedEmailRepository.findUnapprovedEmailByEmail(email)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(NotFoundException.class, () ->
                underTest.approveEmail(email, token));

        // Verify
        verify(unapprovedEmailRepository, times(1)).findUnapprovedEmailByEmail(email);
        verifyNoMoreInteractions(unapprovedEmailRepository);
        verifyNoInteractions(expertRepository);
    }

    @Test
    void itShouldThrowIllegalStateExceptionIfTokenIsInvalid() {
        // Arrange
        String email = "test@example.com";
        String token = "invalidToken";

        UnapprovedEmail unapprovedEmail = new UnapprovedEmail(email, "validToken");
        when(unapprovedEmailRepository.findUnapprovedEmailByEmail(email)).thenReturn(Optional.of(unapprovedEmail));

        // Act and Assert
        assertThrows(IllegalStateException.class, () ->
                underTest.approveEmail(email, token));

        // Verify
        verify(unapprovedEmailRepository, times(1)).findUnapprovedEmailByEmail(email);
        verifyNoMoreInteractions(unapprovedEmailRepository);
        verifyNoInteractions(expertRepository);
    }

    @Test
    void itShouldThrowIllegalStateExceptionIfTokenIsExpired() {
        // Arrange
        String email = "test@example.com";
        String token = "validToken";

        UnapprovedEmail unapprovedEmail = new UnapprovedEmail(email, token);
        unapprovedEmail.setExpireDate(Instant.now().minusSeconds(3600)); // Set expire date to one hour ago
        when(unapprovedEmailRepository.findUnapprovedEmailByEmail(email)).thenReturn(Optional.of(unapprovedEmail));

        // Act and Assert
        assertThrows(IllegalStateException.class, () ->
                underTest.approveEmail(email, token));

        // Verify
        verify(unapprovedEmailRepository, times(1)).findUnapprovedEmailByEmail(email);
        verifyNoMoreInteractions(unapprovedEmailRepository);
        verifyNoInteractions(expertRepository);
    }
}