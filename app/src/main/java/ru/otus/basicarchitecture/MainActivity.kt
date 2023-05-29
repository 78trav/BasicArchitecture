package ru.otus.basicarchitecture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.Component
import javax.inject.Inject
import javax.inject.Provider

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var cache: Provider<WizardCache>

    companion object{
        private var mainActivityComponent: MainActivityComponent? = null
    }

    fun getComponent(): MainActivityComponent = mainActivityComponent ?: MainActivityComponent.create().also { mainActivityComponent = it }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getComponent().inject(this)

        //cache.get().inc()
        //val c = cache.get().getCnt()
    }

}


@Component
@WizardCacheScope
interface MainActivityComponent {

    companion object {
        fun create(): MainActivityComponent = DaggerMainActivityComponent.create()
    }

    fun inject(activity: MainActivity)

    fun provideWizardCache(): WizardCache

}
