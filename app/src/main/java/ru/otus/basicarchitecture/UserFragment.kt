package ru.otus.basicarchitecture

import android.content.Context
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.Component
import ru.otus.basicarchitecture.databinding.FragmentUserBinding
import java.util.GregorianCalendar
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Scope

@UserDataScope
class UserFragment : Fragment(R.layout.fragment_user) {

    companion object {
        fun newInstance() = UserFragment()
    }

    @Inject
    lateinit var cache: Provider<WizardCache>

    private val viewModel: UserViewModel by viewModels<UserViewModel> { UserViewModelFactory(cache.get()) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        UserFragmentComponent.create((activity as MainActivity).getComponent()).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val b = FragmentUserBinding.bind(view)

        viewModel.getData().observe(viewLifecycleOwner) { cache ->

            b.name.text = Editable.Factory.getInstance().newEditable(cache.name)
            b.surname.text = Editable.Factory.getInstance().newEditable(cache.surname)

            val c = GregorianCalendar.getInstance().apply { timeInMillis = cache.birthday }
            b.birthday.init(c.get(GregorianCalendar.YEAR), c.get(GregorianCalendar.MONTH), c.get(GregorianCalendar.DATE)) { _, year, month, day ->
                setBirthday(year, month, day)
            }
        }

        b.name.addTextChangedListener { setName(b.name.text.toString()) }
        b.surname.addTextChangedListener { setSurname(b.surname.text.toString()) }

        b.btn2Address.setOnClickListener {

            if (viewModel.isValidBirthDay())
                findNavController().navigate(R.id.frg_address)
            else
                Toast.makeText(context, "Sorry, you are very small", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setBirthday(year: Int, month: Int, day: Int) {
        val d = viewModel.getData().value
        if (d != null) viewModel.setData(UserData(d.name, d.surname, GregorianCalendar.getInstance().apply { set(year, month, day) }.timeInMillis))
    }

    private fun setName(name: String) {
        val d = viewModel.getData().value
        if (d != null) viewModel.setData(UserData(name, d.surname, d.birthday))
    }

    private fun setSurname(surname: String) {
        val d = viewModel.getData().value
        if (d != null) viewModel.setData(UserData(d.name, surname, d.birthday))
    }

}

@Component(dependencies = [MainActivityComponent::class])
@UserDataScope
interface UserFragmentComponent {


    companion object {

        fun create(mainActivityComponent: MainActivityComponent): UserFragmentComponent {
            return DaggerUserFragmentComponent.builder().mainActivityComponent(mainActivityComponent).build()
        }

    }

    fun inject(frg: UserFragment)

}

@Scope
annotation class UserDataScope
