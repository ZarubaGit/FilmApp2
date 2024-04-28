package ru.yandex.practicum.moviessearch.ui.names

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.yandex.practicum.moviessearch.domain.models.Person

class PersonAdapter : RecyclerView.Adapter<PersonViewHolder>() {

    var person = mutableListOf<Person>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder =
        PersonViewHolder(parent)

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(person[position])
    }

    override fun getItemCount(): Int = person.size
}