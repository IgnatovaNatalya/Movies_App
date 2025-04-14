package com.example.imdb.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.imdb.databinding.FragmentDetailsBinding
import com.example.imdb.ui.core.BindingFragment
import com.google.android.material.tabs.TabLayoutMediator

class DetailsFragment : BindingFragment<FragmentDetailsBinding>() {

    private lateinit var tabMediator: TabLayoutMediator

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?):
            FragmentDetailsBinding {
        return FragmentDetailsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = requireArguments().getString(EXTRA_MOVIE_ID).toString()
        val poster = requireArguments().getString(EXTRA_POSTER).toString()

        binding.viewPager.adapter =
            DetailsViewPagerAdapter(childFragmentManager, lifecycle, poster, movieId)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Постер"
                1 -> tab.text = "О фильме"
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }

    companion object {

        const val EXTRA_MOVIE_ID = "MOVIE_ID"
        const val EXTRA_POSTER = "POSTER"

        fun createArgs(movieId: String, poster: String): Bundle =
            bundleOf(EXTRA_MOVIE_ID to movieId, EXTRA_POSTER to poster)
    }
}