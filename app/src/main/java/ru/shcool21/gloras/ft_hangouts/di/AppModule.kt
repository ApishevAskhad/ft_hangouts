package ru.shcool21.gloras.ft_hangouts.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.shcool21.gloras.ft_hangouts.data.db.AppDatabase
import ru.shcool21.gloras.ft_hangouts.system.CustomPreferences
import ru.shcool21.gloras.ft_hangouts.ui.navigation.Navigation
import ru.shcool21.gloras.ft_hangouts.ui.navigation.Router
import ru.shcool21.gloras.ft_hangouts.ui.navigation.impl.NavigatorImpl
import ru.shcool21.gloras.ft_hangouts.ui.navigation.impl.RouterImpl
import ru.shcool21.gloras.ft_hangouts.ui.viewmodel.ChatViewModel
import ru.shcool21.gloras.ft_hangouts.ui.viewmodel.CreationViewModel
import ru.shcool21.gloras.ft_hangouts.ui.viewmodel.HomeViewModel

@Module
internal abstract class AppModule {

    @Binds
    @ModuleScope
    abstract fun bindNavigation(impl: NavigatorImpl): Navigation

    @Binds
    @ModuleScope
    abstract fun bindRouter(impl: RouterImpl): Router

    companion object {
        @JvmStatic
        @Provides
        fun provideChatViewModel(
            router: Router,
            db: AppDatabase,
            pref: CustomPreferences
        ) = ChatViewModel(router, db, pref)

        @JvmStatic
        @Provides
        fun provideHomeViewModel(
            router: Router,
            db: AppDatabase,
            pref: CustomPreferences
        ) = HomeViewModel(router, db, pref)

        @JvmStatic
        @Provides
        fun provideCreationViewModel(
            router: Router,
            db: AppDatabase
        ) = CreationViewModel(router, db)
    }
}
