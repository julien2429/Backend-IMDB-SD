package UTCN_IMDB.demo.service;

import UTCN_IMDB.demo.DTO.UserDTO;
import UTCN_IMDB.demo.config.BCryptHashing;
import UTCN_IMDB.demo.config.CompileTimeException;
import UTCN_IMDB.demo.enums.UserRole;
import UTCN_IMDB.demo.model.LoginResponse;
import UTCN_IMDB.demo.model.User;
import UTCN_IMDB.demo.repository.UserRepository;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest{

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService; // Assuming you have a UserService

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsers() {
        // given:
        List<User> users = List.of(new User(), new User());

        // when:
        when(userRepository.findAll()).thenReturn(users);
        List<User> result = userService.getUsers(); // Assuming a getUsers method

        // then:
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
        assertEquals(users, result);
    }

    @Test
    void testAddUser() throws CompileTimeException {
        // given:
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("john@example.com");
        userDTO.setUsername("john123");
        userDTO.setPassword("password");
        userDTO.setRole(UserRole.NORMAL); // Or any default role

        User userToSave = new User();
        userToSave.setEmail(userDTO.getEmail());
        userToSave.setUsername(userDTO.getUsername());
        userToSave.setPassword(userDTO.getPassword());
        userToSave.setRole(userDTO.getRole());

        User savedUser = new User();
        savedUser.setUserId(UUID.randomUUID());
        savedUser.setEmail(userDTO.getEmail());
        savedUser.setUsername(userDTO.getUsername());
        savedUser.setPassword(userDTO.getPassword());
        savedUser.setRole(userDTO.getRole());

        // when:
        when(userRepository.save(any(User.class))).thenReturn(savedUser); // Use any(User.class)
        User result = userService.addUser(userDTO);  // Now passing userDTO

        // then:
        assertEquals(savedUser, result);
        assertNotNull(result.getUserId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser() throws ValidationException, CompileTimeException {
        // given:
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setUserId(uuid);
        user.setEmail("john@example.com");
        user.setUsername("john123");
        user.setPassword("password");
        user.setRole(UserRole.NORMAL);

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("jane@example.com");
        userDTO.setUsername("jane456");
        userDTO.setPassword("newpassword");
        userDTO.setRole(UserRole.ADMIN);

        User updatedUser = new User();
        updatedUser.setUserId(uuid);
        updatedUser.setEmail(userDTO.getEmail());
        updatedUser.setUsername(userDTO.getUsername());
        updatedUser.setPassword(userDTO.getPassword());
        updatedUser.setRole(userDTO.getRole());

        // when:
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        User result = userService.updateUser(uuid, userDTO); // Now passing userDTO

        // then:
        assertEquals("jane@example.com", result.getEmail());
        assertEquals("jane456", result.getUsername());
        assertEquals(UserRole.ADMIN, result.getRole());
        verify(userRepository, times(1)).findById(uuid);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUserNotFound() {
        // given:
        UUID uuid = UUID.randomUUID();
        UserDTO userDTO = new UserDTO(); // Still need a DTO, even for not found case

        // when:
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        // then:
        assertThrows(CompileTimeException.class, () -> userService.updateUser(uuid, userDTO)); // Now passing userDTO
        verify(userRepository, times(1)).findById(uuid);
    }

    @Test
    void testDeleteUser() {
        // given:
        UUID uuid = UUID.randomUUID();

        // when:
        doNothing().when(userRepository).deleteById(uuid);
        userService.deleteUser(uuid); // Assuming a deleteUser method

        // then:
        verify(userRepository, times(1)).deleteById(uuid);
    }


}