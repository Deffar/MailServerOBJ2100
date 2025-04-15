package com.myproject.service;


import com.myproject.model.User;
import com.myproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User newUserInfo) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(newUserInfo.getUsername());
                    user.setEmail(newUserInfo.getEmail());
                    user.setPassword(newUserInfo.getPassword());
                    return userRepository.save(user);
                }).orElseThrow(() -> new RuntimeException("User not found."));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
