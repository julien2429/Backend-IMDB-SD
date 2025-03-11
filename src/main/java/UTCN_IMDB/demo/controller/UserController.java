package UTCN_IMDB.demo.controller;

import UTCN_IMDB.demo.model.User;
import UTCN_IMDB.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


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
    public User getUserById(@PathVariable UUID uuid) {
        return userService.getUserById(uuid);
    }

    @GetMapping("/user/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/user/login/{email}/{password}")
    public User getUserByLogin(@PathVariable String email, @PathVariable String password) {
        return userService.getUserByLogin(email, password);
    }

    @PostMapping("/user")
    public User addUser(@RequestBody User person) {
        return userService.addUser(person);
    }

    @PutMapping("/user/{uuid}")
    public User updateUser(@PathVariable UUID uuid, @RequestBody User person) {
        return userService.updateUser(uuid, person);
    }

    @DeleteMapping("/user/{uuid}")
    public void deleteUser(@PathVariable UUID uuid) {
        userService.deleteUser(uuid);
    }

}
