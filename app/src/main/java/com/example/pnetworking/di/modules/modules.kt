package com.example.pnetworking.di.modules

import com.example.pnetworking.repository.TestDataSource
import com.example.pnetworking.repository.TestRepository
import com.example.pnetworking.repository.TestRepositoryImpl
import com.example.pnetworking.ui.base.test.TestViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {

            TestViewModel(get())
    }
}
val repositoryModule = module {
    single<TestRepository> { TestRepositoryImpl(TestDataSource()) }
}

