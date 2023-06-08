package ru.otus.basicarchitecture

import android.widget.BaseAdapter
import com.squareup.moshi.Json
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Scope
import kotlin.coroutines.resumeWithException


enum class Interests {
    WORKING,
    READING,
    FOOTBALL,
    HOCKEY,
    SWIMMING,
    TRAVELLING,
    COOKING,
    SLEEPING,
    EATING,
    DRINKING,
    OTHER
}

data class UserData(val name: String, val surname: String, val birthday: LocalDate)
data class AddressData(val country: String, val city: String, val address: String)

@RegistrationScope
class WizardCache @Inject constructor() {

    private var cnt = 0


    private var userData = UserData(
        "Ivan",
        "Petrov",
        LocalDate.of(2000, 7, 20)
    )
    private var addressData = AddressData("", "", "")
    private val interests = mutableSetOf<Interests>()

    fun setUserData(userData: UserData) {
        this.userData = userData
    }

    fun getUserData(): UserData = userData

    fun setAddressData(addressData: AddressData) {
        this.addressData = addressData
    }

    fun getAddressData(): AddressData = addressData

    fun updateInterest(interest: Interests): Boolean =
        if (interests.contains(interest)) {
            interests.remove(interest)
            false
        } else {
            interests.add(interest)
            true
        }

    fun getInterests(): Array<Interests> = interests.toTypedArray()

    fun inc() {
        cnt++
    }

    fun getCnt(): Int = cnt
}

@Scope
annotation class RegistrationScope

@Module
interface RegistrationModule {

    companion object {

        @RegistrationScope
        @Provides
        fun provideRetrofit(): Retrofit = Retrofit.Builder()
            .baseUrl("https://suggestions.dadata.ru/suggestions/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

}


interface DaDataService {

    @POST("api/4_1/rs/suggest/address")
    @Headers("Content-Type: application/json", "Accept: application/json", "Authorization: Token 6fe267d622e2445f96084ec7ecfb0371c6db5d88")
    suspend fun getAddressHint(@Body query: QueryString): Suggestion

}

/*
suspend fun <T> retrofitCall(request:() -> Call<T>): Response<T> = suspendCancellableCoroutine {
    request.invoke().enqueue(
        object: Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                it.resume(response) {}
            }
            override fun onFailure(call: Call<T>, t: Throwable) {
                it.resumeWithException(t)
            }
        }
    )
}


 */

data class QueryString(
    @field:Json(name = "query")
    val query: String,

    @field:Json(name = "count")
    val count: Int = 5
)

data class Suggestion(
    @field:Json(name = "suggestions")
    val suggestions: List<SuggestionData>
)

data class SuggestionData(
    @field:Json(name = "value")
    val value: String,

    @field:Json(name = "unrestricted_value")
    val unrestricted_value: String
)

@RegistrationScope
@Component(modules = [RegistrationModule::class])
interface DaDataComponent {

    fun inject(vm: AddressViewModel)

}
