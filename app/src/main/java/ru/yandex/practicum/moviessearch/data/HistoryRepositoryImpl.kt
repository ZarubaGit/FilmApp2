package ru.yandex.practicum.moviessearch.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.yandex.practicum.moviessearch.data.converters.MovieDbConvertor
import ru.yandex.practicum.moviessearch.data.db.AppDataBase
import ru.yandex.practicum.moviessearch.data.db.entity.MovieEntity
import ru.yandex.practicum.moviessearch.domain.db.HistoryRepository
import ru.yandex.practicum.moviessearch.domain.models.Movie

class HistoryRepositoryImpl(private val appDataBase: AppDataBase,
                            private val movieDbConvertor: MovieDbConvertor): HistoryRepository {
    override fun historyMovies(): Flow<List<Movie>> = flow{
        val movies = appDataBase.movieDao().getMovies()
        emit(convertFromMovieEntity(movies))
    }

    private fun convertFromMovieEntity(movies: List<MovieEntity>): List<Movie> {
        return movies.map { movie -> movieDbConvertor.map(movie) }
    }
}