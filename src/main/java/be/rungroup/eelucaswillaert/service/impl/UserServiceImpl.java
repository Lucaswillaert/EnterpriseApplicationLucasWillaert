package be.rungroup.eelucaswillaert.service.impl;

import be.rungroup.eelucaswillaert.dto.RegistrationDto;
import be.rungroup.eelucaswillaert.model.User;
import be.rungroup.eelucaswillaert.repository.UserRepository;
import be.rungroup.eelucaswillaert.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public void saveUser(RegistrationDto registrationDto) {
        User user = new User();
        user.setEmail(String.valueOf(registrationDto.getEmail()));
        user.setPassword(registrationDto.getPassword());
        user.setAdmin(false); // this makes the user you create a admin, needs to be changed manually in the database
        userRepository.save(user);
    }


    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}