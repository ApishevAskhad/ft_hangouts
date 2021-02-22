package ru.shcool21.gloras.ft_hangouts.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.shcool21.gloras.ft_hangouts.data.db.AppDatabase
import ru.shcool21.gloras.ft_hangouts.ui.MainActivity
import ru.shcool21.gloras.ft_hangouts.ui.fragment.ChatFragment
import ru.shcool21.gloras.ft_hangouts.ui.fragment.CreationFragment
import ru.shcool21.gloras.ft_hangouts.ui.fragment.HomeFragment

@Component(modules = [AppModule::class])
@ModuleScope
internal interface AppComponent {
    fun inject(homeFragment: HomeFragment)
    fun inject(chatFragment: ChatFragment)
    fun inject(creationFragment: CreationFragment)
    fun inject(mainActivity: MainActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        @BindsInstance
        fun db(db: AppDatabase): Builder

        fun build(): AppComponent
    }
}
