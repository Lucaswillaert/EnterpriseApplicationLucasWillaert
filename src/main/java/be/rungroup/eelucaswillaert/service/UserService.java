package be.rungroup.eelucaswillaert.service;

import be.rungroup.eelucaswillaert.dto.RegistrationDto;
import be.rungroup.eelucaswillaert.model.User;

public interface UserService {
    void saveUser(RegistrationDto registrationdto);

    User findByEmail(String email);
}
