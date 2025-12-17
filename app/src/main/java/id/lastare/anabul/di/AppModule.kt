package id.lastare.anabul.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import id.lastare.anabul.data.repository.AuthRepositoryImpl
import id.lastare.anabul.data.repository.ProductRepositoryImpl
import id.lastare.anabul.domain.repository.AuthRepository
import id.lastare.anabul.domain.repository.ProductRepository
import id.lastare.anabul.domain.usecase.AddProductUseCase
import id.lastare.anabul.domain.usecase.LoginUseCase
import id.lastare.anabul.domain.usecase.RegisterUseCase
import id.lastare.anabul.ui.screen.login.LoginViewModel
import id.lastare.anabul.ui.screen.product.AddProductViewModel
import id.lastare.anabul.ui.screen.register.RegisterViewModel
import id.lastare.anabul.ui.screen.splash.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Firebase
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }

    // Repository
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<ProductRepository> { ProductRepositoryImpl(get()) }
    
    // UseCase
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { AddProductUseCase(get()) }

    // ViewModel
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { AddProductViewModel(get()) }
}
