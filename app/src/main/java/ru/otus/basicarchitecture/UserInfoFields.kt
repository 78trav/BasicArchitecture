package ru.otus.basicarchitecture

import java.util.GregorianCalendar
import javax.inject.Inject
import javax.inject.Scope


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

data class UserData(val name: String, val surname: String, val birthday: Long)
data class AddressData(val country: String, val city: String, val address: String)

@WizardCacheScope
class WizardCache @Inject constructor() {

    private var cnt = 0


    private var userData = UserData(
        "Ivan",
        "Petrov",
        GregorianCalendar.getInstance().apply { set(2000, 7, 20) }.timeInMillis
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
annotation class WizardCacheScope
