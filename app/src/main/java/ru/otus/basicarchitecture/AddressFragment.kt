package ru.otus.basicarchitecture

import android.content.Context
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import retrofit2.Retrofit
import ru.otus.basicarchitecture.databinding.FragmentAddressBinding
import javax.inject.Inject

@RegistrationScope
class AddressFragment : Fragment(R.layout.fragment_address) {

    companion object {
        fun newInstance() = AddressFragment()
    }

    @Inject
    lateinit var viewModelFactory: AddressViewModelFactory

    private val viewModel by viewModels<AddressViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).getComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val b = FragmentAddressBinding.bind(view)

        viewModel.getData().observe(viewLifecycleOwner) { cache ->

            b.country.text = Editable.Factory.getInstance().newEditable(cache.country)
//            b.city.text = Editable.Factory.getInstance().newEditable(cache.city)
//            b.address.text = Editable.Factory.getInstance().newEditable(cache.address)
            b.fullAddress.text = Editable.Factory.getInstance().newEditable(cache.address)

        }

        b.country.addTextChangedListener { updateAddressData() }
//        b.city.addTextChangedListener { updateAddressData() }
//        b.address.addTextChangedListener { updateAddressData() }


        //b.fullAddress.setAdapter(AddressAutoCompleteAdapter(requireContext()))

        b.fullAddress.apply {
            tag = true
            addTextChangedListener {
                if (tag == true)
                    viewModel.searchAddress(it.toString())
                else
                    tag = true
                updateAddressData()
            }
        }

        viewModel.getHints().observe(viewLifecycleOwner) {
            b.fullAddress.apply {
                tag = false
                setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        R.layout.address_item,
                        R.id.address_item,
                        it
                    )
                )
                showDropDown()
            }
        }

        b.btn2Interests.setOnClickListener {
            findNavController().navigate(R.id.frg_interests)
        }

    }

    private fun updateAddressData() {
        val b = FragmentAddressBinding.bind(requireView())
        viewModel.setData(AddressData(b.country.text.toString(), "", b.fullAddress.text.toString()))
    }

}
