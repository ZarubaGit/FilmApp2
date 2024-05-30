package ru.yandex.practicum.moviessearch.presentation.names

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.yandex.practicum.moviessearch.R
import ru.yandex.practicum.moviessearch.domain.api.NamesInteractor
import ru.yandex.practicum.moviessearch.domain.models.Person
import ru.yandex.practicum.moviessearch.presentation.SingleLiveEvent

class NamesViewModel(private val context: Context, private val namesInteractor: NamesInteractor) :
    ViewModel() {

        companion object {
            private const val SEARCH_DEBOUNCE_DELAY = 2000L
            private val SEARCH_REQUEST_TOKEN = Any()
        }

    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<NamesState>()

    fun observeState(): LiveData<NamesState> = stateLiveData

    private val showToast = SingleLiveEvent<String?>()
    fun observeShowToast(): LiveData<String?> = showToast

    private var latestSearchText: String? = null

    private var searchJob: Job? = null

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(changedText: String) {
        if(latestSearchText == changedText){
            return
        }

        this.latestSearchText = changedText

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(NamesState.Loading)

            viewModelScope.launch {
                namesInteractor
                    .searchNames(newSearchText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundNames: List<Person>?, errorMessage: String?){
        val person = mutableListOf<Person>()
        if (foundNames != null) {
            person.addAll(foundNames)
        }
        when {
            errorMessage != null -> {
                renderState(
                    NamesState.Error(
                        message = context.getString(R.string.something_went_wrong),
                    )
                )
                showToast.postValue(errorMessage)
            }

            person.isEmpty() -> {
                renderState(
                    NamesState.Empty(
                        message = context.getString(R.string.nothing_found),
                    )
                )
            }

            else -> {
                renderState(
                    NamesState.Content(
                        person = person,
                    )
                )
            }
        }
    }

    private fun renderState(state: NamesState){
        stateLiveData.postValue(state)
    }
}