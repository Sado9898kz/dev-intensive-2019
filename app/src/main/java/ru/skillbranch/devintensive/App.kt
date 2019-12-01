package ru.skillbranch.devintensive

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatDelegate
import ru.skillbranch.devintensive.repositories.PreferencesRepository

class App : Application() {
    companion object {
        private var instantce: App? = null

        fun applicationContext(): Context {
            return instantce!!.applicationContext
        }
    }

    init {
        instantce = this
    }

    override fun onCreate() {
        super.onCreate()
        PreferencesRepository.getAppTheme().also {
            AppCompatDelegate.setDefaultNightMode(it)
        }
    }

}