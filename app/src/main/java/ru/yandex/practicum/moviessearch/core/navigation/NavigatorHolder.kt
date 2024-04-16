package ru.yandex.practicum.moviessearch.core.navigation

import androidx.fragment.app.Fragment

interface NavigatorHolder {

    fun attachNavigator(navigator: Navigator)

    fun dettachNavigator()

    fun openFragment(fragment: Fragment)
}