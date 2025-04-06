package UTCN_IMDB.demo.controller;

import UTCN_IMDB.demo.model.MovieCast;
import UTCN_IMDB.demo.service.MovieCastService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin

public class MovieCastController {

    private  final MovieCastService movieCastService;
    @GetMapping("/movieCast")
    public List<MovieCast> getMovieCasts()
    {
        return movieCastService.getMovieCasts();
    }
}
