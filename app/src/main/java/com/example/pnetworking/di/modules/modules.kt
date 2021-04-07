package com.example.pnetworking.di.modules

import com.example.pnetworking.repository.signup.SignUpDataSource
import com.example.pnetworking.repository.signup.SignUpRepoImpl
import com.example.pnetworking.repository.signup.SignUpRepository
import com.example.pnetworking.repository.test.TestDataSource
import com.example.pnetworking.repository.test.TestRepository
import com.example.pnetworking.repository.test.TestRepositoryImpl
import com.example.pnetworking.ui.base.signup.SignUpViewModel
import com.example.pnetworking.ui.base.test.TestViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val signupmodule = module {
    viewModel {
        SignUpViewModel(get())
    }
    single<SignUpRepository>{SignUpRepoImpl(SignUpDataSource())}
}
val testmodule= module {
    viewModel {
        TestViewModel(get())
    }
    single<TestRepository> { TestRepositoryImpl(TestDataSource()) }
}

