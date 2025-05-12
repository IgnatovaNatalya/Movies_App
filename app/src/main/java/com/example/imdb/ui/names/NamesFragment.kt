package com.example.imdb.ui.names

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imdb.databinding.FragmentNamesBinding
import com.example.imdb.domain.models.Name
import com.example.imdb.ui.core.BindingFragment
import com.example.imdb.viewmodel.NamesSearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NamesFragment : BindingFragment<FragmentNamesBinding>() {

    private val adapter = NamesAdapter()
    private var textWatcher: TextWatcher? = null

    private val viewModel: NamesSearchViewModel by viewModel()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?):
            FragmentNamesBinding {
        return FragmentNamesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.namesState.observe(viewLifecycleOwner) { render(it) }

        viewModel.observeShowToast().observe(requireActivity()) { showToast(it) }

        binding.recyclerNames.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerNames.adapter = adapter

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.loadNamesDebounce(changedText = p0?.toString() ?: "")
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        binding.queryInput.addTextChangedListener(textWatcher)
    }

    private fun showToast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun render(state: NamesState) {
        when (state) {
            is NamesState.Content -> showContent(state.names)
            is NamesState.Empty -> showEmpty(state.message)
            is NamesState.Error -> showError(state.errorMessage)
            is NamesState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        binding.recyclerNames.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showError(errorMessage: String) {
        binding.recyclerNames.visibility = View.GONE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.placeholderMessage.text = errorMessage
    }

    private fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(names: List<Name>) {
        binding.recyclerNames.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        adapter.names = names
        adapter.notifyDataSetChanged()
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        binding.queryInput.removeTextChangedListener(textWatcher)
//    }

}