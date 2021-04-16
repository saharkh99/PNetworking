package com.example.pnetworking.di.modules

import com.example.chat.ui.main.connection.ConnectionViewModel
import com.example.pnetworking.repository.auth.AuthDataSource
import com.example.pnetworking.repository.auth.AuthRepoImpl
import com.example.pnetworking.repository.auth.AuthRepository
import com.example.pnetworking.repository.connection.ConnectionRepository
import com.example.pnetworking.repository.profile.ProfileDataStore
import com.example.pnetworking.repository.profile.ProfileRepoImpl
import com.example.pnetworking.repository.profile.ProfileRepository
import com.example.pnetworking.repository.test.TestDataSource
import com.example.pnetworking.repository.test.TestRepository
import com.example.pnetworking.repository.test.TestRepositoryImpl
import com.example.pnetworking.ui.base.signin.SignInViewModel
import com.example.pnetworking.ui.base.signup.SignUpViewModel
import com.example.pnetworking.ui.base.test.TestViewModel
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
val connectionmodule= module {
    viewModel {
        ConnectionViewModel(ConnectionRepository())
    }

}

