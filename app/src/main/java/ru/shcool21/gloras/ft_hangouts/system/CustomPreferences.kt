package ru.shcool21.gloras.ft_hangouts.system

import android.content.Context
import androidx.core.content.edit
import ru.shcool21.gloras.ft_hangouts.R
import ru.shcool21.gloras.ft_hangouts.di.ModuleScope
import javax.inject.Inject

@ModuleScope
internal class CustomPreferences @Inject constructor(
    private val context: Context
) {
    companion object {
        private const val PREF_NAME = "custom_pref"

        private const val HEADER_COLOR_K = "header_color"
        private const val ITEM_COLOR_K = "item_color"

        private const val TIMESTAMP = "timestamp"
    }

    private val sharedPreferences = context.getSharedPreferences(
        "${context.packageName}_$PREF_NAME",
        Context.MODE_PRIVATE
    )

    init {
        setBR()
        dropTime()
    }

    fun saveCurrentTime(currentTime: Long) {
        sharedPreferences.edit {
            putLong(TIMESTAMP, currentTime)
        }
    }

    fun dropTime() {
        sharedPreferences.edit {
            putLong(TIMESTAMP, -1)
        }
    }

    fun getLastTime() = sharedPreferences.getLong(TIMESTAMP, -1)

    fun changeColor() {
        if (getHeaderColor() == R.color.blue && getItemColor() == R.drawable.app_home_menu_color_red) {
            setRB()
        } else if (getHeaderColor() == R.color.red && getItemColor() == R.drawable.app_home_menu_color_blue) {
            setBR()
        }
    }

    private fun setBR() {
        sharedPreferences.edit {
            putInt(HEADER_COLOR_K, R.color.blue)
            putInt(ITEM_COLOR_K, R.drawable.app_home_menu_color_red)
        }
    }

    private fun setRB() {
        sharedPreferences.edit {
            putInt(HEADER_COLOR_K, R.color.red)
            putInt(ITEM_COLOR_K, R.drawable.app_home_menu_color_blue)
        }
    }

    fun getHeaderColor() = sharedPreferences.getInt(HEADER_COLOR_K, -1)

    fun getItemColor() = sharedPreferences.getInt(ITEM_COLOR_K, -1)

}