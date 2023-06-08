package ru.otus.basicarchitecture

import android.content.Context
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.navigation.fragment.findNavController
import dagger.Component
import ru.otus.basicarchitecture.databinding.FragmentUserBinding
import java.time.LocalDate
import java.util.GregorianCalendar
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Scope

@RegistrationScope
class UserFragment : Fragment(R.layout.fragment_user) {

    companion object {
        fun newInstance() = UserFragment()
    }

    @Inject
    lateinit var viewModelFactory: UserViewModelFactory

    private val viewModel by viewModels<UserViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).getComponent().inject(this)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val b = FragmentUserBinding.bind(view)

        viewModel.getData().observe(viewLifecycleOwner) { cache ->

            b.name.text = Editable.Factory.getInstance().newEditable(cache.name)
            b.surname.text = Editable.Factory.getInstance().newEditable(cache.surname)

            b.birthday.init(cache.birthday.year, cache.birthday.monthValue, cache.birthday.dayOfMonth) { _, year, month, day ->
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
        if (d != null) viewModel.setData(UserData(d.name, d.surname, LocalDate.of(year, month, day)))
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
