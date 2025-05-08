package UTCN_IMDB.demo.controller;

import UTCN_IMDB.demo.DTO.ReviewDTO;
import UTCN_IMDB.demo.config.CompileTimeException;
import UTCN_IMDB.demo.DTO.UserDTO;
import UTCN_IMDB.demo.model.LoginRequest;
import UTCN_IMDB.demo.model.LoginResponse;
import UTCN_IMDB.demo.model.User;
import UTCN_IMDB.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;


@RestController
@AllArgsConstructor
@CrossOrigin
public class UserController {
    private final UserService userService;

    @GetMapping("/user")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/user/{uuid}")
    public User getUserById(@PathVariable UUID uuid) throws CompileTimeException {
        return userService.getUserById(uuid);
    }

    @GetMapping("/user/email/{email}")
    public User getUserByEmail(@PathVariable String email) throws CompileTimeException {
        return userService.getUserByEmail(email);
    }

    @PostMapping("/user")
    public User addUser(@Valid @RequestBody UserDTO userDTO) throws CompileTimeException {
        return userService.addUser(userDTO);
    }

    @PostMapping("/user/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws CompileTimeException {
        LoginResponse loginResponse = userService.login(loginRequest.username(), loginRequest.password());
        if(loginResponse.success()) {
            return ResponseEntity.ok(loginResponse);
        } else {
            return ResponseEntity.status(UNAUTHORIZED.value()).body(loginResponse);
        }
    }

    @PostMapping("/user/addReview")
    public User addReview(@Valid @RequestBody ReviewDTO reviewDTO) throws CompileTimeException {
        return userService.addReview(reviewDTO);
    }

    @PostMapping("/user/createList/{uuid}/{listName}")
    public User createList(@PathVariable UUID uuid, @PathVariable String listName) throws CompileTimeException {
        return userService.createList(uuid, listName);
    }

    @PostMapping("/user/deleteList/{uuid}/{listId}")
    public User deleteList(@PathVariable UUID uuid, @PathVariable UUID listId) throws CompileTimeException {
        return userService.deleteList(uuid, listId);
    }

    @PostMapping("/user/addMovieToList/{uuid}/{listId}/{movieId}")
    public User addMovieToList(@PathVariable UUID uuid, @PathVariable UUID listId, @PathVariable UUID movieId) throws CompileTimeException {
        return userService.addMovieToList(uuid, listId, movieId);
    }

    @PostMapping("/user/removeMovieFromList/{uuid}/{listId}/{movieId}")
    public User removeMovieFromList(@PathVariable UUID uuid, @PathVariable UUID listId, @PathVariable UUID movieId) throws CompileTimeException {
        return userService.removeMovieFromList(uuid, listId, movieId);
    }



    @PutMapping("/user/{uuid}")
    public User updateUser(@PathVariable UUID uuid, @Valid @RequestBody UserDTO userDTO) throws CompileTimeException {
        return userService.updateUser(uuid, userDTO);
    }


    @DeleteMapping("/user/{uuid}")
    public void deleteUser(@PathVariable UUID uuid) {
        userService.deleteUser(uuid);
    }

}
