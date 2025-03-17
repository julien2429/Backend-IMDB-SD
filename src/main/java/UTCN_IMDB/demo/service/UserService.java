package UTCN_IMDB.demo.service;

import UTCN_IMDB.demo.config.CompileTimeException;
import UTCN_IMDB.demo.DTO.UserDTO;
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


    public User addUser(UserDTO userDTO) throws CompileTimeException  {
        User user = new User();

        ///validations
        if(userRepository.findByEmail(userDTO.getEmail()).isPresent())
        {
            throw new CompileTimeException("Email already taken");
        }

        if(userRepository.findByUsername(userDTO.getUsername()).isPresent())
        {
            throw new CompileTimeException("Username already taken");
        }

        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        return userRepository.save(user);
    }

    public User updateUser(UUID uuid, UserDTO userDTO) throws CompileTimeException {
        User existingUser =
                userRepository.findById(uuid).orElseThrow(
                        () -> new CompileTimeException("User with uuid " + uuid + " not found"));

        ///validations

        if(userRepository.findByEmail(userDTO.getEmail()).isPresent() && !userRepository.findByEmail(userDTO.getEmail()).get().getUserId().equals(uuid))
        {
            throw new CompileTimeException("Email already taken");
        }

        if(userRepository.findByUsername(userDTO.getUsername()).isPresent() && !userRepository.findByUsername(userDTO.getUsername()).get().getUserId().equals(uuid))
        {
            throw new CompileTimeException("Username already taken");
        }

        existingUser.setEmail(userDTO.getEmail());
        existingUser.setRole(userDTO.getRole());
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setPassword(userDTO.getPassword());
        return userRepository.save(existingUser);
    }

    public void deleteUser(UUID uuid) {
        userRepository.deleteById(uuid);
    }

    public User getUserByEmail(String email) throws CompileTimeException {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new CompileTimeException("User with email " + email + " not found"));
    }

    public User getUserById(UUID uuid) throws CompileTimeException {
        return userRepository.findById(uuid).orElseThrow(
                () -> new CompileTimeException("User with id " + uuid + " not found"));
    }

    public User getUserByLogin(String email, String password) throws CompileTimeException
    {
        return userRepository.findByEmailAndPassword(email,password).orElseThrow(
                () -> new CompileTimeException("User login failed"));
    }
}
