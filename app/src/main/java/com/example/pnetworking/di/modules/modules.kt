package com.example.pnetworking.di.modules

import com.example.chat.ui.main.connection.ConnectionViewModel
import com.example.pnetworking.repository.auth.AuthDataSource
import com.example.pnetworking.repository.auth.AuthRepoImpl
import com.example.pnetworking.repository.auth.AuthRepository
import com.example.pnetworking.repository.connection.ConnectionRepository
import com.example.pnetworking.repository.follow.FollowDataSource
import com.example.pnetworking.repository.follow.FollowRepoImpl
import com.example.pnetworking.repository.follow.FollowRepository
import com.example.pnetworking.repository.notifications.NotificationDataSource
import com.example.pnetworking.repository.pchat.PChatDataSource
import com.example.pnetworking.repository.pchat.PChatRepoImpl
import com.example.pnetworking.repository.pchat.PChatRepository
import com.example.pnetworking.repository.profile.ProfileDataStore
import com.example.pnetworking.repository.profile.ProfileRepoImpl
import com.example.pnetworking.repository.profile.ProfileRepository
import com.example.pnetworking.repository.test.TestDataSource
import com.example.pnetworking.repository.test.TestRepository
import com.example.pnetworking.repository.test.TestRepositoryImpl
import com.example.pnetworking.ui.base.signin.SignInViewModel
import com.example.pnetworking.ui.base.signup.SignUpViewModel
import com.example.pnetworking.ui.base.test.TestViewModel
import com.example.pnetworking.ui.pchat.PrivateChateViewModel
import com.example.pnetworking.ui.profile.FollowViewModel
import com.example.pnetworking.ui.profile.ProfileViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.sql.Connection

val signupmodule = module {
    viewModel {
        SignUpViewModel(get())
    }
    viewModel {
        SignInViewModel(get())
    }
    single<AuthRepository>{AuthRepoImpl(AuthDataSource())}
}

val testmodule= module {
    viewModel {
        TestViewModel(get())
    }
    single<TestRepository> { TestRepositoryImpl(TestDataSource()) }
}
val profilemodule= module {
    viewModel {
        ProfileViewModel(get())
    }
    single<ProfileRepository> { ProfileRepoImpl(ProfileDataStore()) }
}
val followmodule= module {
    viewModel {
        FollowViewModel(get())
    }
    single<FollowRepository> { FollowRepoImpl(FollowDataSource()) }
}
val connectionmodule= module {
    viewModel {
        ConnectionViewModel(ConnectionRepository())
    }
}
val pchatmodule= module {
    viewModel {
        PrivateChateViewModel(get(), NotificationDataSource())
    }
    single<PChatRepository> { PChatRepoImpl(PChatDataSource()) }
}

