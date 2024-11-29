package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.model.User;
import com.rekrutmen.rest_api.model.Login;
import com.rekrutmen.rest_api.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    private final LoginRepository loginRepository;

    @Autowired
    public LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public void updateUser(Login login) {
        loginRepository.save(login);
    }

    public Optional<User> getUserByEmail(String email) {
        return loginRepository.findByEmail(email);
    }
}
