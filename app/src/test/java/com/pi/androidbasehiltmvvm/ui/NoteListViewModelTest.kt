package com.pi.androidbasehiltmvvm.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.pi.androidbasehiltmvvm.data.NoteModelFactory
import com.pi.androidbasehiltmvvm.features.notelist.domain.usecase.NoteListUseCase
import com.pi.androidbasehiltmvvm.features.notelist.domain.viewmodel.NoteListViewModel
import com.pi.androidbasehiltmvvm.utils.ObserverExtension.getOrAwaitValue
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Testing NoteListViewModel with real ROOM Database and looking if datas inserted, updated, deleted
 * or could be able to get all notes from ROOM
 */

class NoteListViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var noteListUseCase: NoteListUseCase

    private lateinit var viewModel: NoteListViewModel

    private val mainThreadSurrogate = newSingleThreadContext("Main Thread")

    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.initMocks(this)
        viewModel = NoteListViewModel(noteListUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Get All Notes Data from usecase (ROOM) return list`(): TestResult = runBlocking {
        Mockito.`when`(noteListUseCase.getAllNotes())
            .thenReturn(flow { emit(NoteModelFactory.getMockNotes()) })

        viewModel.getAllNotes()

        val noteList = viewModel.noteList.getOrAwaitValue(time = 10)

        assertThat(noteList).isNotNull()
        assertThat(noteList).isNotEmpty()
        assertEquals(noteList, NoteModelFactory.getMockNotes())
    }
}
