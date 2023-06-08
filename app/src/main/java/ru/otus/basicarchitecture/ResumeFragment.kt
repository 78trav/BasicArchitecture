package ru.otus.basicarchitecture

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.otus.basicarchitecture.databinding.FragmentResumeBinding
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RegistrationScope
class ResumeFragment : Fragment(R.layout.fragment_resume) {

    companion object {
        fun newInstance() = ResumeFragment()
    }

    @Inject
    lateinit var viewModelFactory: ResumeViewModelFactory

    private val viewModel by viewModels<ResumeViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).getComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val b = FragmentResumeBinding.bind(view)

        viewModel.getData().observe(viewLifecycleOwner) { cache ->

            val userData = cache.getUserData()
            val addrData = cache.getAddressData()
            val birthday = cache.getUserData().birthday.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            var interests = ""
            cache.getInterests().forEach {
                interests = interests + "\n" + it.name.lowercase()
            }

            b.textResume.text =
                "NAME: ${userData.name}\nSURNAME: ${userData.surname}\nBIRTHDAY: $birthday\n\nCOUNTRY: ${addrData.country}\nADDRESS: ${addrData.address}\n\nINTERESTS: $interests"
        }


    }

}
