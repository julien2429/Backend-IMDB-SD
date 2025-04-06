package UTCN_IMDB.demo.DTO;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

public class MovieDTO
{
    private String title;
    private Date releaseYear;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Date releaseYear) {
        this.releaseYear = releaseYear;
    }

    public MovieDTO() {
    }

    public MovieDTO(String updatedMatrix, Date releaseYear) {
        this.title = updatedMatrix;
        this.releaseYear = releaseYear;
    }
}
