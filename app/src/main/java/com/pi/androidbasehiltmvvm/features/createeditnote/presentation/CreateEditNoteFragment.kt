package com.pi.androidbasehiltmvvm.features.createeditnote.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.pi.androidbasehiltmvvm.R
import com.pi.androidbasehiltmvvm.core.binding.FragmentBinding.getDrawable
import com.pi.androidbasehiltmvvm.core.binding.ImageViewBinding.loadImage
import com.pi.androidbasehiltmvvm.core.extensions.changeUiState
import com.pi.androidbasehiltmvvm.core.extensions.observe
import com.pi.androidbasehiltmvvm.core.platform.delegations.viewBinding
import com.pi.androidbasehiltmvvm.core.platform.delegations.viewModelWithNavigation
import com.pi.androidbasehiltmvvm.databinding.FragmentCreateEditNoteBinding
import com.pi.androidbasehiltmvvm.features.createeditnote.domain.viewmodel.CreateEditNoteViewModel
import com.pi.data.remote.response.Note
import dagger.hilt.android.AndroidEntryPoint

/**
 * A @Fragment for adding or editing a note
 */
@AndroidEntryPoint
class CreateEditNoteFragment : Fragment(R.layout.fragment_create_edit_note) {

    private val viewModel by viewModelWithNavigation<CreateEditNoteViewModel>()
    private val binding by viewBinding(FragmentCreateEditNoteBinding::bind)

    private val args: CreateEditNoteFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViews()
        getViewData()
        observeData()
    }

    /**
     * Initialize view informations and observing recyclerview gestures
     */
    private fun setUpViews() {
        binding.apply {
            viewmodel = viewModel
        }
    }

    /**
     * Setting @Note model on Fragment's View which sent from previous fragment
     */
    private fun getViewData() {
        viewModel.setViewData(args.note, args.isNoteExist)
    }

    /**
     * Observing View Events and noteList to fill the View
     */
    private fun observeData() {

        observe(viewModel.uiStateFlow) { viewState ->
            binding.apply {
                progressBar.changeUiState(uiState = viewState.uiState)

                if (viewState.newNote != null) {
                    controlNoteDetails(newNote = viewState.newNote)
                }
            }
        }

        observe(viewModel.imageUrl) { imageUrl ->

            binding.noteImageView.loadImage(
                url = imageUrl,
                placeholder = getDrawable(R.drawable.ic_placeholder),
                errorDrawable = getDrawable(R.drawable.ic_broken_image)
            )
        }
    }

    /**
     * Controlling All Places according to needs. If they have an error then it will show
     * under the @TextInputLayout
     */
    private fun controlNoteDetails(newNote: Note) {
        newNote.apply {
            var hasError = false
            if (title.isEmpty()) {
                binding.titleTextInputLayout.error = getString(R.string.empty_title_error)
                hasError = true
            }
            if (description.isEmpty()) {
                binding.descriptionTextInputLayout.error =
                    getString(R.string.empty_description_error)
                hasError = true
            }

            if (!hasError) {
                viewModel.saveNoteAndNavigateToList(newNote = this)
            }
        }
    }
}
