package UTCN_IMDB.demo.service;


import UTCN_IMDB.demo.DTO.ResetPasswordDTO;
import UTCN_IMDB.demo.DTO.UserDTO;
import UTCN_IMDB.demo.config.CompileTimeException;
import UTCN_IMDB.demo.model.EmailDetails;
import UTCN_IMDB.demo.model.ForgottenPasswordData;
import UTCN_IMDB.demo.model.User;
import UTCN_IMDB.demo.repository.ForgottenPasswordRepository;
import UTCN_IMDB.demo.repository.UserRepository;
import UTCN_IMDB.demo.utils.ForgottenPasswordUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ForgottenPasswordService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private ForgottenPasswordRepository forgottenPasswordRepository;
    @Autowired
    private UserRepository userRepository;

    public void requestForgottenPassword(String recipient) throws CompileTimeException {

        ForgottenPasswordData forgottenPasswordData = new ForgottenPasswordData();

        Optional<User> user = userRepository.getUserByEmail(recipient);
        if (user.isEmpty()) {
            throw new CompileTimeException("User with email " + recipient + " not found");
        }

        // Generate a unique token for the password reset link
        String token = ForgottenPasswordUtils.generateRandomString(6);
        Date expirationDate = ForgottenPasswordUtils.calculateExpirationDate(5);

        User existingUser = user.get();
        forgottenPasswordData.setUser(existingUser);
        forgottenPasswordData.setForgottenPasswordToken(token);
        forgottenPasswordData.setForgottenPasswordTokenExpirationDate(expirationDate);

        forgottenPasswordRepository.save(forgottenPasswordData);

        String subject = "Password Reset Request";
        String body = "To reset your password, please use the following code: " + token + "\n" +
                "This code will expire in 5 minutes.\n";
        String response = emailService.sendSimpleMail(new EmailDetails(recipient, body, subject, null));


    }

    public void resetPassword(ResetPasswordDTO resetPasswordDTO) throws CompileTimeException {

        Optional<ForgottenPasswordData> data = forgottenPasswordRepository.findByForgottenPasswordToken(resetPasswordDTO.getToken());
        if (data.isEmpty()) {
            throw new CompileTimeException("Invalid token");
        }

        ForgottenPasswordData existingData = data.get();

        if (existingData.getForgottenPasswordTokenExpirationDate().before(new Date())) {
            throw new CompileTimeException("Token expired");
        }

        Optional<User> existingUser = userRepository.getUserByEmail(resetPasswordDTO.getEmail());
        if (existingUser.isEmpty()) {
            throw new CompileTimeException("User with email " + resetPasswordDTO.getEmail() + " not found");
        }

        User user = existingUser.get();

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(resetPasswordDTO.getEmail());
        userDTO.setPassword(resetPasswordDTO.getPassword());
        userDTO.setUsername(user.getUsername());
        userDTO.setRole(user.getRole());

        userService.updateUser(user.getUserId(), userDTO);
        forgottenPasswordRepository.delete(existingData);
    }
}
