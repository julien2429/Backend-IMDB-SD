package UTCN_IMDB.demo.controller;


import UTCN_IMDB.demo.DTO.ResetPasswordDTO;
import UTCN_IMDB.demo.config.CompileTimeException;
import UTCN_IMDB.demo.model.EmailDetails;
import UTCN_IMDB.demo.service.EmailService;
import UTCN_IMDB.demo.service.ForgottenPasswordService;
import jakarta.persistence.criteria.From;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin
public class ResetPasswordController {

    @Autowired
    private ForgottenPasswordService forgottenPasswordService;


    @PostMapping("/auth/reset-password/{email}")
    public String resetPassword(@PathVariable String email) throws CompileTimeException {

        forgottenPasswordService.requestForgottenPassword(email);

        return "Password reset link sent to your email.";
    }

    @PostMapping("/auth/reset-password-confirmation")
    public String resetPasswordData(@RequestBody ResetPasswordDTO resetPasswordDTO) throws CompileTimeException {

        forgottenPasswordService.resetPassword(resetPasswordDTO);
        return "Password successfully changed.";
    }


}
