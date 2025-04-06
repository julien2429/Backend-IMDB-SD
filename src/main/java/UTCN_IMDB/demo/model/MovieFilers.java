package UTCN_IMDB.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Setter
@Getter
public class MovieFilers {
    private Optional<String> title = Optional.empty(); // Initialize with Optional.empty()
    private Optional<List<String>> genresNames = Optional.empty(); // Initialize with Optional.empty()
    private Optional<String> director = Optional.empty(); // Initialize with Optional.empty()
    private Optional<Date> startDate = Optional.empty(); // Initialize with Optional.empty()
    private Optional<Date> endDate = Optional.empty(); // Initialize with Optional.empty()

    // You can also provide constructors that initialize these fields:
    public MovieFilers() {
        // All fields are initialized to Optional.empty() by default
    }

    public MovieFilers(String title, List<String> genresNames, String director, Date startDate, Date endDate) {
        this.title = Optional.ofNullable(title);
        this.genresNames = Optional.ofNullable(genresNames);
        this.director = Optional.ofNullable(director);
        this.startDate = Optional.ofNullable(startDate);
        this.endDate = Optional.ofNullable(endDate);
    }

}
