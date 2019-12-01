package ru.skillbranch.devintensive.viewmodels

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.repositories.PreferencesRepository
import java.util.prefs.PreferenceChangeEvent

class ProfileViewModel : ViewModel() {

    private val repository: PreferencesRepository = PreferencesRepository
    private val profileDate = MediatorLiveData<Profile>()
    private val appTheme = MediatorLiveData<Int>()

    init {
        Log.d("M_ProfileViewModel", "init view model")
        profileDate.value = repository.getProfile()
        appTheme.value = repository.getAppTheme()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("M_ProfileViewModel", "view model cleared")
    }

    fun getProfileDate(): LiveData<Profile> = profileDate

    fun getTheme(): LiveData<Int> = appTheme

    fun saveProfileDate(profile: Profile) {
        repository.saveProfile(profile)
        profileDate.value = profile
    }

    fun switchTheme() {
        if (appTheme.value == AppCompatDelegate.MODE_NIGHT_YES) {
            appTheme.value = AppCompatDelegate.MODE_NIGHT_NO
        } else {
            appTheme.value = AppCompatDelegate.MODE_NIGHT_YES
        }
        repository.saveAppTheme(appTheme.value!!)
    }

}