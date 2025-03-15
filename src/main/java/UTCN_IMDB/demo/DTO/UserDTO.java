package UTCN_IMDB.demo.DTO;


import UTCN_IMDB.demo.config.EnumeratedMessage;
import UTCN_IMDB.demo.enums.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class UserDTO {

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid", regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private String email;
    @Size(min = 3, max = 20, message = "Username should be between 3 and 20 characters")
    private String username;
    @Size(min = 6, max = 20, message = "Password should be between 6 and 20 characters")
    private String password;
    @EnumeratedMessage(message= "User must have one of the roles: UNKNOWN|NORMAL|REVIEWER|ADMIN", value=EnumType.ORDINAL)
    private UserRole role;


}
