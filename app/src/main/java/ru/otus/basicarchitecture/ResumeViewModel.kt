package ru.otus.basicarchitecture

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

@ResumeScope
class ResumeViewModel @Inject constructor(private val cache: WizardCache): ViewModel() {
    // TODO: Implement the ViewModel
    private var data = MutableLiveData<WizardCache>().apply { value = cache }

    fun getData(): LiveData<WizardCache> = data
}

@ResumeScope
class ResumeViewModelFactory @Inject constructor(private val cache: WizardCache): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ResumeViewModel(cache) as T
    }
}
