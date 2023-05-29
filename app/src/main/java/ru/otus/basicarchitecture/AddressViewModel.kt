package ru.otus.basicarchitecture

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Component
import javax.inject.Inject
import javax.inject.Provider

@AddressDataScope
class AddressViewModel @Inject constructor(private val cache: WizardCache): ViewModel() {
    // TODO: Implement the ViewModel

    private var data = MutableLiveData<AddressData>().apply {
        value = cache.getAddressData()
    }

    fun getData(): LiveData<AddressData> = data
    fun setData(data: AddressData) {
        cache.setAddressData(data)
        this.data = MutableLiveData<AddressData>().apply { value = cache.getAddressData() }
    }

}

@AddressDataScope
class AddressViewModelFactory @Inject constructor(private val cache: WizardCache): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddressViewModel(cache) as T
    }
}
