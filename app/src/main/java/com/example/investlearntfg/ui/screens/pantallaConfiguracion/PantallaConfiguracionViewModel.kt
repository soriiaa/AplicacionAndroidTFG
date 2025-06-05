package com.example.investlearntfg.ui.screens.pantallaConfiguracion

import androidx.lifecycle.ViewModel
import com.example.investlearntfg.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PantallaConfiguracionViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {
    fun subirCambiosAFirebase() {
        TODO("Not yet implemented")
    }

}