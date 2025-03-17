package UTCN_IMDB.demo.DTO;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoleDTO {

    @Size(min = 1, max = 20, message = "Role name should be between 3 and 20 characters")
    private String roleName;

}
