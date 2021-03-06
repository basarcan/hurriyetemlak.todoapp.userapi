package com.hurriyetemlak.todoapp.todoapp.service;

import com.hurriyetemlak.todoapp.todoapp.domain.User;
import com.hurriyetemlak.todoapp.todoapp.exception.exceptions.EmailDoesNotValidException;
import com.hurriyetemlak.todoapp.todoapp.exception.exceptions.PasswordDoesNotExistException;
import com.hurriyetemlak.todoapp.todoapp.model.request.SignInRequest;
import com.hurriyetemlak.todoapp.todoapp.model.response.SignInResponse;
import com.hurriyetemlak.todoapp.todoapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import static com.hurriyetemlak.todoapp.todoapp.utils.EmailValidation.isValidEmail;

@Service
public class SignInService {

    private final EncryptionService encryptionService;
    private final UserRepository userRepository;

    public SignInService(EncryptionService encryptionService, UserRepository userRepository) {
        this.encryptionService = encryptionService;
        this.userRepository = userRepository;
    }

    public SignInResponse signIn(SignInRequest signInRequest) throws RuntimeException {
        if (!isValidEmail(signInRequest.getEmail())) {
            throw new EmailDoesNotValidException("Girilen E-MAIL adresi hatalıdır.");
        }

        String encryptedPassword = encryptionService.encodeData(signInRequest.getPassword());
        User user = userRepository.signIn(signInRequest.getEmail(), encryptedPassword);
        if (user == null) {
            return null;
        } else if (user.getEmail() != null && user.getPassword() == null) {
            throw new PasswordDoesNotExistException("Girilen şifre yanlış");
        }
        String token = encryptionService.generateToken();
        user.setToken(token);
        userRepository.update(user);
        SignInResponse signInResponse = new SignInResponse();
        signInResponse.setFirstName(user.getFirstName());
        signInResponse.setLastName(user.getLastName());
        signInResponse.setToken(token);

        return signInResponse;
    }
}
