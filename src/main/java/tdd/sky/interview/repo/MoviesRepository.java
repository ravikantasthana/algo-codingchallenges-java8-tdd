package tdd.sky.interview.repo;


import tdd.sky.interview.Movie;

import java.io.IOException;
import java.util.stream.Stream;

/**
 * NOT TO BE IMPLEMENTED
 * Contract for retrieving all movies from a datasource
 */
public interface MoviesRepository {

    Stream<Movie> getAll() throws IOException;
}
