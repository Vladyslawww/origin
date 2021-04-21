package com.holikov.domain.di

import com.holikov.domain.categories.usecases.GetCategoriesUseCase
import com.holikov.domain.categories.usecases.GetCategoriesUseCaseImpl
import com.holikov.domain.listing.usecases.*
import com.holikov.domain.listing.usecases.RemoteItemUseCase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


val domainModules = Kodein.Module("domainModules") {
    import(useCasesModule)

    private val useCasesModule = Kodein.Module("intercatorsModule") {
        bind<GetCategoriesUseCase>() with singleton { GetCategoriesUseCaseImpl(instance()) }
        bind<ObserveSavedUseCase>() with singleton { ObserveSavedUseCaseImpl(instance()) }
        bind<SearchUseCase>() with singleton { SearchUseCaseImpl(instance()) }
        bind<SaveItemUseCase>() with singleton { SaveItemUseCaseImpl(instance()) }
        bind<RemoveItemUseCase>() with singleton { RemoveItemUseCaseImpl(instance()) }
        bind<RemoveAllUseCase>() with singleton { RemoveAllUseCaseImpl(instance()) }
        bind<IsSavedUseCase>() with singleton { IsSavedUseCaseImpl(instance()) }
        bind<RemoteItemUseCase>() with singleton { RemoteItemUseCaseImpl(instance()) }
    }