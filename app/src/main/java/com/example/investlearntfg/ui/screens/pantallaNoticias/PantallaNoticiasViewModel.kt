package com.example.investlearntfg.ui.screens.pantallaNoticias

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investlearntfg.data.model.Noticia
import com.example.investlearntfg.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PantallaNoticiasViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _noticias = MutableStateFlow<List<Noticia>>(emptyList())
    val noticias: StateFlow<List<Noticia>> = _noticias

    private val _estaCargando = MutableStateFlow(false)
    val estaCargando: StateFlow<Boolean> = _estaCargando

    init {
        cargarNoticias()
    }

    fun cargarNoticias() {
        viewModelScope.launch {
            _estaCargando.value = true
            try {
                val listaNoticias = postRepository.obtenerNoticiasGenerales()
                _noticias.value = listaNoticias
            } catch (e: Exception) {
                Log.d("AAAAAA", e.toString())
                _noticias.value = emptyList()
            }
            _estaCargando.value = false
        }
    }

}