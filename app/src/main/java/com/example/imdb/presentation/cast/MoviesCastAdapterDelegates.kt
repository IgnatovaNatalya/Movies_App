package com.example.imdb.presentation.cast

import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.imdb.databinding.ItemCastBinding
import com.example.imdb.util.RVItem
import com.example.imdb.databinding.ItemCastHeaderBinding
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

// Делегат для заголовков на экране состава участников

fun movieCastHeaderDelegate() =
    adapterDelegateViewBinding<MovieCastRVItem.HeaderItem, RVItem, ItemCastHeaderBinding>(
        { layoutInflater, root -> ItemCastHeaderBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {
            binding.header.text = item.headerText
        }
    }

// Делегат для отображения участников на соответствующем экране

fun movieCastPersonDelegate() = adapterDelegateViewBinding<MovieCastRVItem.PersonItem, RVItem, ItemCastBinding>(
    { layoutInflater, root -> ItemCastBinding.inflate(layoutInflater, root, false) }
) {
    bind {
        if (item.data.image == null) {
            binding.image.isVisible = false
        } else {
            Glide.with(itemView)
                .load(item.data.image)
                .into(binding.image)
            binding.image.isVisible = true
        }
        binding.name.text = item.data.name
        binding.description.text = item.data.description
    }
}