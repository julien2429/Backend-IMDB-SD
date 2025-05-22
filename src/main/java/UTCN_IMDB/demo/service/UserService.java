package UTCN_IMDB.demo.service;

import UTCN_IMDB.demo.DTO.ListResponseDTO;
import UTCN_IMDB.demo.DTO.ReviewDTO;
import UTCN_IMDB.demo.config.BCryptHashing;
import UTCN_IMDB.demo.config.CompileTimeException;
import UTCN_IMDB.demo.DTO.UserDTO;
import UTCN_IMDB.demo.enums.ReviewStatus;
import UTCN_IMDB.demo.model.*;
import UTCN_IMDB.demo.repository.ListsRepository;
import UTCN_IMDB.demo.repository.MovieRepository;
import UTCN_IMDB.demo.repository.ReviewRepository;
import UTCN_IMDB.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.plaf.PanelUI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final ListsRepository listsRepository;
    private final ReviewRepository reviewRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    public List<User>
    getUsers() {
        return userRepository.findAll();
    }


    @Transactional
    public User addUser(UserDTO userDTO) throws CompileTimeException {
        User user = new User();

        ///validations
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new CompileTimeException("Email already taken");
        }

        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new CompileTimeException("Username already taken");
        }

        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user.setUsername(userDTO.getUsername());

        List<Review> reviews = new ArrayList<>();
        user.setReviews(reviews);

        List<Lists> lists = new ArrayList<>();

        Lists Favorites = new Lists();
        Favorites.setListName("Favorites");
        Favorites.setMovieList(new ArrayList<>());
        Favorites.setUser(user); // Set the User for Favorites
        lists.add(Favorites);

        Lists Watchlist = new Lists();
        Watchlist.setMovieList(new ArrayList<>());
        Watchlist.setListName("Watchlist");
        Watchlist.setUser(user); // Set the User for Watchlist
        lists.add(Watchlist);

        user.setLists(lists);

        String hashedPassword = BCryptHashing.hashPassword(userDTO.getPassword());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    public User updateUser(UUID uuid, UserDTO userDTO) throws CompileTimeException {
        User existingUser =
                userRepository.findById(uuid).orElseThrow(
                        () -> new CompileTimeException("User with uuid " + uuid + " not found"));

        ///validations

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent() && !userRepository.findByEmail(userDTO.getEmail()).get().getUserId().equals(uuid)) {
            throw new CompileTimeException("Email already taken");
        }

        if (userRepository.findByUsername(userDTO.getUsername()).isPresent() && !userRepository.findByUsername(userDTO.getUsername()).get().getUserId().equals(uuid)) {
            throw new CompileTimeException("Username already taken");
        }

        existingUser.setEmail(userDTO.getEmail());
        existingUser.setRole(userDTO.getRole());
        existingUser.setUsername(userDTO.getUsername());

        String hashedPassword = BCryptHashing.hashPassword(userDTO.getPassword());
        existingUser.setPassword(hashedPassword);
        return userRepository.save(existingUser);
    }

    @Transactional
    public User addReview( ReviewDTO reviewDTO) throws CompileTimeException {
        User user = userRepository.findByUsername(reviewDTO.getUsername()).orElseThrow(
                () -> new CompileTimeException("User with uuid " + reviewDTO.getUsername() + " not found"));

        Movie movie = movieRepository.findById(reviewDTO.getMovieId()).orElseThrow(
                () -> new CompileTimeException("Movie with id " + reviewDTO.getMovieId() + " not found"));


        Review review = movie.getReviews().stream()
                .filter(r -> r.getUser().getUserId().equals(user.getUserId()))
                .findFirst()
                .orElse(null);

        if (review != null) {
            review.setComment(reviewDTO.getReviewText());
            review.setRating(reviewDTO.getRating());
            review.setStatus(ReviewStatus.PENDING);
            reviewRepository.save(review);
        } else {
            Review newReview = new Review();
            newReview.setComment(reviewDTO.getReviewText());
            newReview.setRating(reviewDTO.getRating());
            newReview.setUser(user);
            newReview.setMovie(movie);
            newReview.setStatus(ReviewStatus.PENDING);
            movie.getReviews().add(newReview);
            user.getReviews().add(newReview);
            reviewRepository.save(newReview);

        }


        return userRepository.save(user);
    }

    public User getUserByUsername(String username) throws CompileTimeException {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new CompileTimeException("User with username " + username + " not found"));
    }


    public User createList(UUID uuid, String listName) throws CompileTimeException {
        User user = userRepository.findById(uuid).orElseThrow(
                () -> new CompileTimeException("User with uuid " + uuid + " not found"));

        if (user.getLists().stream().anyMatch(list -> list.getListName().equals(listName))) {
            throw new CompileTimeException("List with name " + listName + " already exists");
        }

        Lists newList = new Lists();
        newList.setListName(listName);

        if(user.getLists() == null) {
            user.setLists(new ArrayList<>());
        }

        List<Lists> lists = user.getLists();
        lists.add(newList);
        newList.setUser(user);
        newList.setMovieList(new ArrayList<>());
        user.setLists(lists);

        return userRepository.save(user);
    }

    public User deleteList(UUID uuid, UUID listId) throws CompileTimeException {
        User user = userRepository.findById(uuid).orElseThrow(
                () -> new CompileTimeException("User with uuid " + uuid + " not found"));

        Lists list = user.getLists().stream()
                .filter(l -> l.getListId().equals(listId))
                .findFirst()
                .orElseThrow(() -> new CompileTimeException("List with id " + listId + " not found"));

        user.getLists().remove(list);
        return userRepository.save(user);
    }

    public User addToList(UUID uuid, String listName, UUID movieId) throws CompileTimeException {
        User user = userRepository.findById(uuid).orElseThrow(
                () -> new CompileTimeException("User with uuid " + uuid + " not found"));

        Lists list = user.getLists().stream()
                .filter(l -> l.getListName().equals(listName))
                .findFirst()
                .orElseThrow(() -> new CompileTimeException("List with name " + listName + " not found"));

        Movie movie = movieRepository.findById(movieId).orElseThrow(
                () -> new CompileTimeException("Movie with id " + movieId + " not found"));

        MovieList movieList = new MovieList();
        movieList.setMovie(movie);
        movieList.setList(list);

        if (list.getMovieList() == null) {
            list.setMovieList(new ArrayList<>());
        }
        List<MovieList> movieLists = list.getMovieList();
        movieLists.add(movieList);
        movieList.setList(list);
        return userRepository.save(user);
    }

    public User removeFromList(UUID uuid, String listName, UUID movieId) throws CompileTimeException {
        User user = userRepository.findById(uuid).orElseThrow(
                () -> new CompileTimeException("User with uuid " + uuid + " not found"));

        Lists list = user.getLists().stream()
                .filter(l -> l.getListName().equals(listName))
                .findFirst()
                .orElseThrow(() -> new CompileTimeException("List with name " + listName + " not found"));

        Movie movie = movieRepository.findById(movieId).orElseThrow(
                () -> new CompileTimeException("Movie with id " + movieId + " not found"));

        MovieList movieList = list.getMovieList().stream()
                .filter(ml -> ml.getMovie().getMovieId().equals(movie.getMovieId()))
                .findFirst()
                .orElseThrow(() -> new CompileTimeException("Movie with id " + movieId + " not found in list"));

        list.getMovieList().remove(movieList);
        return userRepository.save(user);
    }

    public User addMovieToList(UUID uuid, UUID listId, UUID movieId) throws CompileTimeException {
        User user = userRepository.findById(uuid).orElseThrow(
                () -> new CompileTimeException("User with uuid " + uuid + " not found"));

        Lists list = user.getLists().stream()
                .filter(l -> l.getListId().equals(listId))
                .findFirst()
                .orElseThrow(() -> new CompileTimeException("List with id " + listId + " not found"));

        Movie movie = movieRepository.findById(movieId).orElseThrow(
                () -> new CompileTimeException("Movie with id " + movieId + " not found"));

        MovieList movieList = new MovieList();
        movieList.setMovie(movie);
        movieList.setList(list);

        if (list.getMovieList() == null) {
            list.setMovieList(new ArrayList<>());
        }
        List<MovieList> movieLists = list.getMovieList();
        movieLists.add(movieList);
        movieList.setList(list);
        return userRepository.save(user);
    }

    public User removeMovieFromList(UUID uuid, UUID listId, UUID movieId) throws CompileTimeException {
        User user = userRepository.findById(uuid).orElseThrow(
                () -> new CompileTimeException("User with uuid " + uuid + " not found"));

        Lists list = user.getLists().stream()
                .filter(l -> l.getListId().equals(listId))
                .findFirst()
                .orElseThrow(() -> new CompileTimeException("List with id " + listId + " not found"));

        Movie movie = movieRepository.findById(movieId).orElseThrow(
                () -> new CompileTimeException("Movie with id " + movieId + " not found"));

        MovieList movieList = list.getMovieList().stream()
                .filter(ml -> ml.getMovie().getMovieId().equals(movie.getMovieId()))
                .findFirst()
                .orElseThrow(() -> new CompileTimeException("Movie with id " + movieId + " not found in list"));

        list.getMovieList().remove(movieList);
        return userRepository.save(user);
    }

    public ListResponseDTO getList(UUID uuid, UUID listId) throws CompileTimeException {
        User user = userRepository.findById(uuid).orElseThrow(
                () -> new CompileTimeException("User with uuid " + uuid + " not found"));

        return user.getLists().stream()
                .filter(l -> l.getListId().equals(listId))
                .findFirst( ).map(
                        list -> new ListResponseDTO(
                                list.getListId(),
                                list.getListName(),
                                list.getMovieList().stream()
                                        .map(MovieList::getMovie)
                                        .toList()
                        )
                )
                .orElseThrow(() -> new CompileTimeException("List with id " + listId + " not found"));
    }

    public List<ListResponseDTO> getAllLists(UUID uuid) throws CompileTimeException {
        User user = userRepository.findById(uuid).orElseThrow(
                () -> new CompileTimeException("User with uuid " + uuid + " not found"));

        return user.getLists().stream().map(lists ->
                    new ListResponseDTO(
                            lists.getListId(),
                            lists.getListName(),
                            lists.getMovieList().stream()
                                    .map(MovieList::getMovie)
                                    .toList()
                    )
                ).toList();
    }

    public List<String> findMovieInLists(UUID uuid, UUID movieId) throws  CompileTimeException
    {
        User user = userRepository.findById(uuid).orElseThrow(
                () -> new CompileTimeException("User with uuid " + uuid + " not found"));

        List<String> listNames = new ArrayList<>();


        user.getLists().forEach(list -> {
            list.getMovieList().forEach(movieList -> {
                if (movieList.getMovie().getMovieId().equals(movieId)) {
                    listNames.add(list.getListName());
                }
            });
        });

        return listNames;
    }

    public void deleteUser(UUID uuid) {
        userRepository.deleteById(uuid);
    }

    public User getUserByEmail(String email) throws CompileTimeException {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new CompileTimeException("User with email " + email + " not found"));
    }

    public User getUserById(UUID uuid) throws CompileTimeException {
        return userRepository.findById(uuid).orElseThrow(
                () -> new CompileTimeException("User with id " + uuid + " not found"));
    }


}
