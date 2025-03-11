package UTCN_IMDB.demo.service;

import UTCN_IMDB.demo.model.User;
import UTCN_IMDB.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(UUID uuid, User user) {
        User existingUser =
                userRepository.findById(uuid).orElseThrow(
                        () -> new IllegalStateException("User with uuid " + uuid + " not found"));
        existingUser.setEmail(user.getEmail());
        existingUser.setUserId(user.getUserId());
        existingUser.setRole(user.getRole());
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());


        return userRepository.save(existingUser);
    }

    public void deleteUser(UUID uuid) {
        userRepository.deleteById(uuid);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalStateException("User with email " + email + " not found"));
    }

    public User getUserById(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(
                () -> new IllegalStateException("User with id " + uuid + " not found"));
    }

    public User getUserByLogin(String email, String password)
    {
        return userRepository.findByEmailAndPassword(email,password).orElseThrow(
                () -> new IllegalStateException("User login failed"));
    }
}
