package ru.shcool21.gloras.ft_hangouts.system

import android.app.Application
import androidx.room.Room
import ru.shcool21.gloras.ft_hangouts.data.db.AppDatabase
import ru.shcool21.gloras.ft_hangouts.di.DaggerAppComponent

internal class App : Application() {

    private val appDatabase by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "hangouts"
        )
            .build()
    }

    val daggerComponent by lazy {
        DaggerAppComponent
            .builder()
            .context(this)
            .db(appDatabase)
            .build()
    }

}
