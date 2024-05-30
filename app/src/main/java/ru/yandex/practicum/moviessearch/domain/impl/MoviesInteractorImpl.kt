package ru.yandex.practicum.moviessearch.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.yandex.practicum.moviessearch.domain.api.MoviesInteractor
import ru.yandex.practicum.moviessearch.domain.api.MoviesRepository
import ru.yandex.practicum.moviessearch.domain.models.Movie
import ru.yandex.practicum.moviessearch.domain.models.MovieCast
import ru.yandex.practicum.moviessearch.domain.models.MovieDetails
import ru.yandex.practicum.moviessearch.util.Resource

class MoviesInteractorImpl(private val repository: MoviesRepository) : MoviesInteractor {

    override fun searchMovies(expression: String): Flow<Pair<List<Movie>?, String?>> {
        return repository.searchMovies(expression).map { movie ->
            when (movie) {
                is Resource.Success -> {
                    Pair(movie.data, null)
                }

                is Resource.Error -> {
                    Pair(movie.data, movie.message)
                }
            }
        }
    }

    override fun getMoviesDetails(movieId: String): Flow<Pair<MovieDetails?, String?>> {
        return repository.getMovieDetails(movieId).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    Pair(resource.data, null)
                }

                is Resource.Error -> {
                    Pair(resource.data, resource.message)
                }
            }
        }
    }

    override fun getMovieCast(movieId: String): Flow<Pair<MovieCast?, String?>> {
        return repository.getMovieCast(movieId).map {resource ->
            when(resource){
                is Resource.Success -> { Pair(resource.data, null) }
                is Resource.Error -> { Pair(resource.data, resource.message) }
            }
        }
    }

}
