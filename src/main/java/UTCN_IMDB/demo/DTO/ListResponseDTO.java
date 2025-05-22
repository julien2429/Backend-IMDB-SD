package UTCN_IMDB.demo.DTO;

import UTCN_IMDB.demo.model.Movie;

import java.util.List;
import java.util.UUID;

public class ListResponseDTO {

    private UUID listId;
    private String listName;
    private List<Movie> movieList;

    public ListResponseDTO(UUID listId, String listName, List<Movie> movieList) {
        this.listId = listId;
        this.listName = listName;
        this.movieList = movieList;
    }

    public UUID getListId() {
        return listId;
    }

    public void setListId(UUID listId) {
        this.listId = listId;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }
}
