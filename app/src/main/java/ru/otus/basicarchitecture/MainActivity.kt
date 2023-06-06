package ru.otus.basicarchitecture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    private val mainActivityComponent = DaggerMainActivityComponent.create()

    fun getComponent(): MainActivityComponent = mainActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

}

@Component
@RegistrationScope
interface MainActivityComponent {

    fun inject(userFragment: UserFragment)

    fun inject(addressFragment: AddressFragment)

    fun inject(interestsFragment: InterestsFragment)

    fun inject(resumeFragment: ResumeFragment)

}
