package ru.otus.basicarchitecture

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.time.LocalDate
import java.time.Period
import java.util.GregorianCalendar
import javax.inject.Inject

private var MINIMUM_YEARS_OLD = 18

@UserDataScope
class UserViewModel @Inject constructor(private val cache: WizardCache): ViewModel() {
    // TODO: Implement the ViewModel

    private var data = MutableLiveData<UserData>().apply { value = cache.getUserData() }

    fun getData(): LiveData<UserData> = data

    fun setData(data: UserData) {
        cache.setUserData(data)
        this.data = MutableLiveData<UserData>().apply { value = cache.getUserData() }
    }

    fun isValidBirthDay(): Boolean {
        var c = GregorianCalendar.getInstance().apply { timeInMillis = System.currentTimeMillis() }
        val d1 = LocalDate.of(c.get(GregorianCalendar.YEAR), c.get(GregorianCalendar.MONTH), c.get(GregorianCalendar.DAY_OF_MONTH))

        c = GregorianCalendar.getInstance().apply { timeInMillis = data.value?.birthday ?: 0 }
        val d2 = LocalDate.of(c.get(GregorianCalendar.YEAR), c.get(GregorianCalendar.MONTH), c.get(GregorianCalendar.DAY_OF_MONTH))

        return (Period.between(d2, d1).years >= MINIMUM_YEARS_OLD)
    }

}


@UserDataScope
class UserViewModelFactory @Inject constructor(private val cache: WizardCache): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(cache) as T
    }
}
