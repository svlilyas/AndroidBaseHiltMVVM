package com.pi.androidbasehiltmvvm.features.createeditnote.presentation

import androidx.navigation.fragment.navArgs
import com.pi.androidbasehiltmvvm.R
import com.pi.androidbasehiltmvvm.core.binding.ImageViewBinding.loadImage
import com.pi.androidbasehiltmvvm.core.extensions.changeUiState
import com.pi.androidbasehiltmvvm.core.extensions.observe
import com.pi.androidbasehiltmvvm.core.navigation.PageName
import com.pi.androidbasehiltmvvm.core.platform.BaseFragment
import com.pi.androidbasehiltmvvm.databinding.FragmentCreateEditNoteBinding
import com.pi.androidbasehiltmvvm.features.createeditnote.domain.viewmodel.CreateEditNoteViewModel
import com.pi.data.remote.response.Note
import dagger.hilt.android.AndroidEntryPoint

/**
 * A @Fragment for adding or editing a note
 */
@AndroidEntryPoint
class CreateEditNoteFragment :
    BaseFragment<FragmentCreateEditNoteBinding, CreateEditNoteViewModel>(
        layoutId = R.layout.fragment_create_edit_note,
        viewModelClass = CreateEditNoteViewModel::class.java
    ) {
    private val args: CreateEditNoteFragmentArgs by navArgs()

    /**
     * To know which page we are in - It might use in @DeepLink Feature
     */
    override fun getScreenKey(): String = PageName.PreLogin.NOTE_LIST_PAGE

    /**
     * Initialize view informations and observing recyclerview gestures
     */
    override fun setUpViews() {
        binding.apply {
            viewmodel = viewModel
        }
    }

    /**
     * Setting @Note model on Fragment's View which sent from previous fragment
     */
    override fun getViewData() {
        viewModel.setViewData(args.note, args.isNoteExist)
    }

    /**
     * Observing View Events and noteList to fill the View
     */
    override fun observeData() {

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
