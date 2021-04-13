package com.example.chat.ui.main.connection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.User
import com.example.pnetworking.repository.connection.ConnectionRepository
import com.example.pnetworking.utils.ChatViewmodel
import java.util.*
import javax.inject.Inject

class ConnectionViewModel(private val repoRepository: ConnectionRepository): ChatViewmodel() {
    private val _query = MutableLiveData<String>()

    val query: LiveData<String> = _query

    val results: LiveData<List<User>> = _query.let {
        if (it.toString().trim().equals("")) {
            AbsentLiveData.create()
        } else {
            repoRepository.fakeUsers()
        }
    }


    fun setQuery(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault()).trim()
        if (input == _query.value) {
            return
        }

    }


    fun refresh() {
        _query.value?.let {
            _query.value = it
        }
    }

    class LoadMoreState(val isRunning: Boolean, val errorMessage: String?) {
        private var handledError = false

        val errorMessageIfNotHandled: String?
            get() {
                if (handledError) {
                    return null
                }
                handledError = true
                return errorMessage
            }
    }
}
