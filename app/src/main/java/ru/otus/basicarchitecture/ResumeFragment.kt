package ru.otus.basicarchitecture

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.Component
import ru.otus.basicarchitecture.databinding.FragmentInterestsBinding
import ru.otus.basicarchitecture.databinding.FragmentResumeBinding
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Scope

@ResumeScope
class ResumeFragment : Fragment(R.layout.fragment_resume) {

    companion object {
        fun newInstance() = ResumeFragment()
    }

    @Inject
    lateinit var cache: Provider<WizardCache>

    private val viewModel: ResumeViewModel by viewModels<ResumeViewModel> { ResumeViewModelFactory(cache.get()) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ResumeFragmentComponent.create((activity as MainActivity).getComponent()).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val b = FragmentResumeBinding.bind(view)

        viewModel.getData().observe(viewLifecycleOwner) { cache ->

            val userData = cache.getUserData()
            val addrData = cache.getAddressData()
            val birthday = SimpleDateFormat("dd.MM.yyyy").format(Date(userData.birthday))
            var interests = ""
            cache.getInterests().forEach {
                interests = interests + "\n" + it.name.lowercase()
            }

            b.textResume.text =
                "NAME: ${userData.name}\nSURNAME: ${userData.surname}\nBIRTHDAY: $birthday\n\nCOUNTRY: ${addrData.country}\nCITY: ${addrData.city}\nADDRESS: ${addrData.address}\n\nINTERESTS: $interests"
        }


    }

}


@Component(dependencies = [MainActivityComponent::class])
@ResumeScope
interface ResumeFragmentComponent {


    companion object {

        fun create(mainActivityComponent: MainActivityComponent): ResumeFragmentComponent {
            return DaggerResumeFragmentComponent.builder().mainActivityComponent(mainActivityComponent).build()
        }

    }

    fun inject(frg: ResumeFragment)

}

@Scope
annotation class ResumeScope
