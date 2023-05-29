package ru.otus.basicarchitecture

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.Component
import ru.otus.basicarchitecture.databinding.FragmentAddressBinding
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Scope

@AddressDataScope
class AddressFragment : Fragment(R.layout.fragment_address) {

    companion object {
        fun newInstance() = AddressFragment()
    }

    @Inject
    lateinit var cache: Provider<WizardCache>

    private val viewModel: AddressViewModel by viewModels<AddressViewModel> { AddressViewModelFactory(cache.get()) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AddressFragmentComponent.create((activity as MainActivity).getComponent()).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val b = FragmentAddressBinding.bind(view)

        viewModel.getData().observe(viewLifecycleOwner) { cache ->

            b.country.text = Editable.Factory.getInstance().newEditable(cache.country)
            b.city.text = Editable.Factory.getInstance().newEditable(cache.city)
            b.address.text = Editable.Factory.getInstance().newEditable(cache.address)

        }

        b.country.addTextChangedListener { updateAddressData() }
        b.city.addTextChangedListener { updateAddressData() }
        b.address.addTextChangedListener { updateAddressData() }

        b.btn2Interests.setOnClickListener {
            findNavController().navigate(R.id.frg_interests)
        }

    }

    private fun updateAddressData() {
        val b = FragmentAddressBinding.bind(requireView())
        viewModel.setData(AddressData(b.country.text.toString(), b.city.text.toString(), b.address.text.toString()))
    }

}

@Component(dependencies = [MainActivityComponent::class])
@AddressDataScope
interface AddressFragmentComponent {


    companion object {

        fun create(mainActivityComponent: MainActivityComponent): AddressFragmentComponent {
            return DaggerAddressFragmentComponent.builder().mainActivityComponent(mainActivityComponent).build()
        }

    }

    fun inject(frg: AddressFragment)

}

@Scope
annotation class AddressDataScope
