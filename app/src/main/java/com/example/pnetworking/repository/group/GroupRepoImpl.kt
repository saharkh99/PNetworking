package com.example.pnetworking.repository.group

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.User


class GroupRepoImpl(private val groupDataSource: GroupDataSource): GroupRepository {
    override fun createGroup(image: Uri, name: String, bio: String): MutableLiveData<String> {
        return groupDataSource.createGroup(image, name, bio)
    }

    override fun addAParticipant(user: User, chatId: String) {
        return groupDataSource.addAParticipant(user, chatId)
    }
}