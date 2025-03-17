package UTCN_IMDB.demo.DTO;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class MovieDTO
{
    @Size(min = 2, max = 64, message = "Title should be between 3 and 64 characters")
    private String title;
    private Date year;

}
