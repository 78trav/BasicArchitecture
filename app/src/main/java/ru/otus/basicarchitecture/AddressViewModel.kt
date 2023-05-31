package ru.otus.basicarchitecture

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class AddressViewModel constructor(private val cache: WizardCache): ViewModel() {
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

@RegistrationScope
class AddressViewModelFactory @Inject constructor(private val cache: WizardCache): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Log.d("cache", cache.toString())
        return AddressViewModel(cache) as T
    }
}
