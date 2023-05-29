package ru.otus.basicarchitecture

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

@InterestsScope
class InterestsViewModel @Inject constructor(private val cache: WizardCache): ViewModel() {
    // TODO: Implement the ViewModel

    private var data = MutableLiveData<Array<Interests>>().apply { value = cache.getInterests() }

    fun getData(): LiveData<Array<Interests>> = data
    fun setData(interest: Interests) {
        cache.updateInterest(interest)
        this.data = MutableLiveData<Array<Interests>>().apply { value = cache.getInterests() }
    }

}

@InterestsScope
class InterestsViewModelFactory @Inject constructor(private val cache: WizardCache): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InterestsViewModel(cache) as T
    }
}
