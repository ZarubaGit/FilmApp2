package ru.yandex.practicum.moviessearch.data

import ru.yandex.practicum.moviessearch.data.dto.Response

interface NetworkClient {
    suspend fun doRequestSuspend(dto: Any): Response
}