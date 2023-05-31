package ru.otus.basicarchitecture

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.time.LocalDate
import java.time.Period
import javax.inject.Inject

private var MINIMUM_YEARS_OLD = 18

class UserViewModel constructor(private val cache: WizardCache): ViewModel() {
    // TODO: Implement the ViewModel

    private var data = MutableLiveData<UserData>().apply { value = cache.getUserData() }

    fun getData(): LiveData<UserData> = data

    fun setData(data: UserData) {
        cache.setUserData(data)
        this.data = MutableLiveData<UserData>().apply { value = cache.getUserData() }
    }

    fun isValidBirthDay(): Boolean = Period.between(data.value?.birthday ?: LocalDate.now(), LocalDate.now()).years >= MINIMUM_YEARS_OLD

}

@RegistrationScope
class UserViewModelFactory @Inject constructor(private val cache: WizardCache): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Log.d("cache", cache.toString())
        return UserViewModel(cache) as T
    }
}
