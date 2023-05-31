package ru.otus.basicarchitecture

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class InterestsViewModel constructor(private val cache: WizardCache): ViewModel() {
    // TODO: Implement the ViewModel

    private var data = MutableLiveData<Array<Interests>>().apply { value = cache.getInterests() }

    fun getData(): LiveData<Array<Interests>> = data
    fun setData(interest: Interests) {
        cache.updateInterest(interest)
        this.data = MutableLiveData<Array<Interests>>().apply { value = cache.getInterests() }
    }

}

@RegistrationScope
class InterestsViewModelFactory @Inject constructor(private val cache: WizardCache): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Log.d("cache", cache.toString())
        return InterestsViewModel(cache) as T
    }
}
