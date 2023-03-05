package tdd.sky.interview;

import org.junit.jupiter.api.Test;
import tdd.sky.interview.repo.MoviesRepository;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class MovieServiceTest {

    private final MovieService movieService;
    private final MoviesRepository moviesRepositoryMock;

    public MovieServiceTest() {
        moviesRepositoryMock = mock(MoviesRepository.class);
        this.movieService = new MovieServiceImpl(moviesRepositoryMock);
    }

    @Test
    void findByTitleWorks_whenMovieNotFound() throws IOException {

        when(moviesRepositoryMock.getAll()).thenReturn(Stream.of());

        Optional<Movie> movie = movieService.findByTitle("Matrix 4");

        assertThat(movie.isPresent()).isFalse();
    }

    @Test
    void findByTitleWorks_whenMovieFound() throws IOException {

        List<Movie> movieList = List.of(new Movie("Matrix 4",null,null,null));
        when(moviesRepositoryMock.getAll()).thenReturn(movieList.stream());

        Optional<Movie> movie = movieService.findByTitle("Matrix 4");

        assertThat(movie.isPresent()).isTrue();
    }

    @Test
    void findByTitle_shouldReturnEmpty_whenTitleIsNull() throws Exception {

        List<Movie> movieList = List.of(new Movie(null,null,null,null));
        when(moviesRepositoryMock.getAll()).thenReturn(movieList.stream());

        Optional<Movie> movie = movieService.findByTitle("Matrix 4");

        assertThat(movie.isPresent()).isFalse();
    }

    @Test
    void findByTitle_shouldReturnFirstRecord_whenDuplicates() throws Exception {

        List<Movie> movieList = List.of(new Movie("Matrix 4",2017,null,null),
                new Movie("Matrix 4",2018,null,null));
        when(moviesRepositoryMock.getAll()).thenReturn(movieList.stream());

        Optional<Movie> movie = movieService.findByTitle("Matrix 4");

        assertThat(movie.isPresent()).isTrue();
        assertThat(movie.get().getTitle()).isEqualTo("Matrix 4");
        assertThat(movie.get().getYear()).isEqualTo(2017);
    }

    @Test
    void findByTitle_shouldThrowRuntimeException_whenRepositoryThrowsException() throws Exception{

        when(moviesRepositoryMock.getAll()).thenThrow(RuntimeException.class);
        assertThatThrownBy(() -> movieService.findByTitle("Matrix 4")).isInstanceOf(RuntimeException.class);
    }

    @Test
    void fuzzyMatch_whenMatchesWithRegexp_thenCorrect() throws Exception {

        List<Movie> movieList = List.of(new Movie("Terminator",2017,null,null),
                new Movie("Exterminate",2018,null,null),
                new Movie("Matrix 4",2018,null,null));
        when(moviesRepositoryMock.getAll()).thenReturn(movieList.stream());

        Collection<Movie> movies = movieService.fuzzyMatch("(term)");
        assertThat(movies).isNotNull();
        assertThat(movies.size()).isEqualTo(2);
    }

    @Test
    void findByGenre_whenMatches_thenCorrect() throws Exception{

        List<Movie> movieList = List.of(new Movie("Avengers: Age of Ultron",2017, List.of("Action"), List.of("Robert Downey Jr", "Chris Evans")),
                new Movie("The Avengers",2018,List.of("Superhero"),null),
                new Movie("Matrix 4",2018,null,null));
        when(moviesRepositoryMock.getAll()).thenReturn(movieList.stream());

        Collection<Movie> movies = movieService.findByGenres("Action", "Superhero");
        assertThat(movies).isNotEmpty();
        assertThat(movies.size()).isEqualTo(2);
    }

    @Test
    void findByGenreQuery_withAndOperand_whenMatches_thenCorrect() throws Exception {
        List<Movie> movieList = List.of(new Movie("Avengers: Age of Ultron",2017, List.of("Action","Superhero"), List.of("Robert Downey Jr", "Chris Evans")),
                new Movie("The Avengers",2018,List.of("Superhero","Action"),null),
                new Movie("Matrix 4",2018,List.of("Action"),null));
        when(moviesRepositoryMock.getAll()).thenReturn(movieList.stream());

        MovieService.GenreQuery genreQuery = new MovieService.GenreQuery(Set.of("Action","Superhero"), MovieService.Operand.AND);

        Collection<Movie> movies = movieService.findByGenres(genreQuery);
        assertThat(movies).isNotEmpty();
        assertThat(movies.size()).isEqualTo(2);
    }

    @Test
    void findByGenreQuery_withOROperand_whenMatches_thenCorrect() throws Exception {

        List<Movie> movieList = List.of(new Movie("Avengers: Age of Ultron",2017, List.of("Action","Superhero"), List.of("Robert Downey Jr", "Chris Evans")),
                new Movie("The Avengers",2018,List.of("Superhero","Action"),null),
                new Movie("Matrix 4",2018,List.of("Action"),null));
        when(moviesRepositoryMock.getAll()).thenReturn(movieList.stream());

        MovieService.GenreQuery genreQuery = new MovieService.GenreQuery(Set.of("Action"), MovieService.Operand.OR);
        Collection<Movie>  movies = movieService.findByGenres(genreQuery);
        assertThat(movies).isNotEmpty();
        assertThat(movies.size()).isEqualTo(3);
    }

    @Test
    void findByGenreQuery_withNOTOperand_whenMatches_thenCorrect() throws Exception {

        List<Movie> movieList = List.of(new Movie("Avengers: Age of Ultron",2017, List.of("Action","Superhero"), List.of("Robert Downey Jr", "Chris Evans")),
                new Movie("The Avengers",2018,List.of("Superhero","Action"),null),
                new Movie("Matrix 4",2018,List.of("Adhyatm"),null));
        when(moviesRepositoryMock.getAll()).thenReturn(movieList.stream());

        MovieService.GenreQuery genreQuery = new MovieService.GenreQuery(Set.of("Action","Superhero"), MovieService.Operand.NOT);
        Collection<Movie>  movies = movieService.findByGenres(genreQuery);
        assertThat(movies).isNotEmpty();
        assertThat(movies.size()).isEqualTo(1);
    }

    @Test
    void findByGenreQuery_withXOROperand_whenMatches_thenCorrect() throws Exception {

        List<Movie> movieList = List.of(new Movie("Avengers: Age of Ultron",2017, List.of("Action","Superhero"), List.of("Robert Downey Jr", "Chris Evans")),
                new Movie("The Avengers",2018,List.of("Superhero","Action"),null),
                new Movie("Matrix 4",2018,List.of("Adhyatm", "Action"),null),
                new Movie("Krrish",2006,List.of("Adhyatm", "Superhero"),null));
        when(moviesRepositoryMock.getAll()).thenReturn(movieList.stream());

        MovieService.GenreQuery genreQuery = new MovieService.GenreQuery(Set.of("Action", "Superhero"), MovieService.Operand.XOR);
        Collection<Movie> movies = movieService.findByGenres(genreQuery);
        assertThat(movies).isNotEmpty();
        assertThat(movies.size()).isEqualTo(2);
    }
}