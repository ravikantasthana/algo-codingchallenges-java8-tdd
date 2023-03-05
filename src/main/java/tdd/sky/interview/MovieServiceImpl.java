package tdd.sky.interview;

import tdd.sky.interview.repo.MoviesRepository;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MovieServiceImpl implements MovieService {

    private final MoviesRepository moviesRepository;

    public MovieServiceImpl(MoviesRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }

    @Override
    public Optional<Movie> findByTitle(String title) {
        return getAllMovies()
                .filter(movie -> movie.getTitle() != null)
                .filter(movie -> movie.getTitle().equals(title))
                .findFirst();
    }

    private Stream<Movie> getAllMovies() {
        try {
            return moviesRepository.getAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Movie> fuzzyMatch(String titleRegexp)
    {
        return getAllMovies()
                .filter(movie -> movie.getTitle() != null)
                .filter(movie -> isFuzzyMatch(movie.getTitle().toLowerCase(), titleRegexp))
                .collect(Collectors.toList());
    }

    private boolean isFuzzyMatch(String tile, String titleRegexp){

        Pattern pattern = Pattern.compile(titleRegexp);
        Matcher matcher = pattern.matcher(tile);
        return matcher.find();
    }

    @Override
    public Collection<Movie> findByGenres(String... genres) {

        return getAllMovies()
                .filter(movie -> movie.getGenres() != null)
                .filter(movie -> isContainsGenre(movie.getGenres(), genres))
                .collect(Collectors.toList());
    }

    private boolean isContainsGenre(List<String> genreList, String[] genres){
        return !Collections.disjoint(genreList, Arrays.asList(genres));
    }

    @Override
    public Collection<Movie> findByGenres(GenreQuery query) {

        return getAllMovies()
                .filter(movie -> movie.getGenres() != null)
                .filter(movie -> matchesGenreWithGenreQuery(movie.getGenres(), query))
                .collect(Collectors.toList());
    }

    private boolean matchesGenreWithGenreQuery(List<String> genreList, GenreQuery genreQuery){
        return switch (genreQuery.getOperand()){
            case AND -> andCheck(genreList,genreQuery.getGenres());
            case OR -> orCheck(genreList,genreQuery.getGenres());
            case NOT -> notCheck(genreList,genreQuery.getGenres());
            case XOR -> xorCheck(genreList,genreQuery.getGenres());
            default -> throw new IllegalArgumentException("Invalid Operand");
        };
    }

    private boolean andCheck(List<String> genreList, Set<String> genresToCheck){
        return genreList.containsAll(genresToCheck);
    }

    private boolean orCheck(List<String> genreList, Set<String> genresToCheck){
        return genresToCheck.stream().anyMatch(genreList::contains);
    }

    private boolean notCheck(List<String> genreList, Set<String> genresToCheck){
        return Collections.disjoint(genreList,genresToCheck);
    }

    private boolean xorCheck(List<String> genreList, Set<String> genresToCheck){

        String[] genres = genresToCheck.toArray(String[]::new);
        boolean result = genreList.contains(genres[0]);

        return Arrays.stream(genres, 1, genres.length)
                .map(g -> genreList.contains(g))
                .reduce(result, (r1, r2) -> r1 ^ r2);

    }
}
