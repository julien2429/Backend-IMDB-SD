package UTCN_IMDB.demo.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Data
public class ReviewDTO {

    @NotBlank(message = "Review cannot be blank")
    private String reviewText;

    @Min(value = 1, message = "Rating must be between 1 and 10")
    @Max(value = 10, message = "Rating must be between 1 and 10")
    private float rating;

    @NonNull
    private UUID movieId;
}
