package com.example.imdb.presentation.names

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.databinding.ItemNameBinding
import com.example.imdb.domain.models.Name

class NamesAdapter(): RecyclerView.Adapter<NameViewHolder>() {

    var names = listOf<Name>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return NameViewHolder(
            parent,
            ItemNameBinding.inflate(layoutInspector, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NameViewHolder, position: Int) {
        holder.bind(names[position])
    }

    override fun getItemCount(): Int {
        return names.size
    }
}

    