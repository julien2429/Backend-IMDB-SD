package UTCN_IMDB.demo.DTO;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonDTO {
    @Size(min = 2, max = 64, message = "First name should be between 2 and 64 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name should contain only letters")
    private String firstName;

    @Size(min = 2, max = 64, message = "Last name should be between 2 and 64 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name should contain only letters")
    private String lastName;

    @Size(min = 2, max = 64, message = "Gender should be between 2 and 64 characters")
    @Pattern(regexp = "^[a-zA-Z-]+$", message = "Gender should contain only letters and '-' character")
    private String gender;

    @Size(min = 2, max = 64, message = "Nationality should be between 2 and 64 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Nationality should contain only letters")
    private String nationality;

    private LocalDate birthDate;
    private LocalDate deathDate;
}
