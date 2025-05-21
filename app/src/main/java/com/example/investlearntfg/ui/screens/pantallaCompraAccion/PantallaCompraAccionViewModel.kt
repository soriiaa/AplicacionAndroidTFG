package com.example.investlearntfg.ui.screens.pantallaCompraAccion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.investlearntfg.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PantallaCompraAccionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val postRepository: PostRepository
) : ViewModel() {

}