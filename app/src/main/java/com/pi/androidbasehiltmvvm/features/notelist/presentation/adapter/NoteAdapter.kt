package com.pi.androidbasehiltmvvm.features.notelist.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pi.androidbasehiltmvvm.core.extensions.setOnDebouncedClickListener
import com.pi.androidbasehiltmvvm.core.platform.BaseListAdapter
import com.pi.androidbasehiltmvvm.core.platform.BaseViewHolder
import com.pi.androidbasehiltmvvm.databinding.ItemNoteListBinding
import com.pi.data.remote.response.Note

/**
 * Adapter with viewModel to manage events
 * and DiffUtil to change the list without notifyData
 */
class NoteAdapter() : BaseListAdapter<Note>(
    itemsSame = { old, new -> old.id == new.id },
    contentsSame = { old, new -> old == new }
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return NoteViewHolder(parent, inflater)
    }

    /**
     * Binding Note Item to its view
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NoteViewHolder -> {
                holder.bind(getItem(position))
            }
        }
    }

    /**
     * Removing the exact item and submit the list with new one
     * Without using @notifyDataSetChanged()
     */
    fun removeItem(position: Int): Note {
        val noteList = ArrayList<Note>()
        currentList.forEach {
            noteList.add(it)
        }
        val removedItem = noteList.removeAt(position)
        submitList(noteList)

        return removedItem
    }

    /**
     * Binding Note model with its view - @DataBinding
     */
    internal inner class NoteViewHolder(parent: ViewGroup, inflater: LayoutInflater) :
        BaseViewHolder<ItemNoteListBinding>(
            ItemNoteListBinding.inflate(inflater, parent, false)
        ) {
        fun bind(note: Note) {
            binding.apply {
                item = note

                itemView.setOnDebouncedClickListener {
                    debouncedClickListener?.invoke(note)
                }

                executePendingBindings()
            }
        }
    }
}
