package UTCN_IMDB.demo.controller;


import UTCN_IMDB.demo.DTO.UserDTO;
import UTCN_IMDB.demo.security.JwtGenerator;
import UTCN_IMDB.demo.config.CompileTimeException;
import UTCN_IMDB.demo.model.LoginRequest;
import UTCN_IMDB.demo.model.LoginResponse;
import UTCN_IMDB.demo.model.User;
import UTCN_IMDB.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin
public class LoginController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private JwtGenerator jwtGenerator;

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.username(),
                            loginRequest.password()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtGenerator.generateToken(authentication);

            LoginResponse loginResponse = new LoginResponse(true, null, token);
            return ResponseEntity.ok(loginResponse);

        } catch (BadCredentialsException ex) {
            return ResponseEntity
                    .status(401)
                    .body(new LoginResponse(false, "Invalid username or password", null));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(500)
                    .body(new LoginResponse(false, "Internal server error", null));
        }
    }


    @PostMapping("/auth/register")
    public User addUser(@Valid @RequestBody UserDTO userDTO) throws CompileTimeException {
        return userService.addUser(userDTO);
    }





}
