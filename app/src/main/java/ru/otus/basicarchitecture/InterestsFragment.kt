package ru.otus.basicarchitecture

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.view.children
import androidx.core.view.marginTop
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.Component
import ru.otus.basicarchitecture.databinding.FragmentAddressBinding
import ru.otus.basicarchitecture.databinding.FragmentInterestsBinding
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Scope

@InterestsScope
class InterestsFragment : Fragment(R.layout.fragment_interests) {

    companion object {
        fun newInstance() = InterestsFragment()
    }

    @Inject
    lateinit var cache: Provider<WizardCache>

    private val viewModel: InterestsViewModel by viewModels<InterestsViewModel> { InterestsViewModelFactory(cache.get()) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        InterestsFragmentComponent.create((activity as MainActivity).getComponent()).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val b = FragmentInterestsBinding.bind(view)

        viewModel.getData().observe(viewLifecycleOwner) { cache ->

            b.llayout.children.forEach {
                if (it is CheckBox) {
                    val interest = it.tag as Interests
                    it.setOnClickListener {
                        updateInterests(interest)
                    }
                    it.isChecked = cache.contains(interest)
                }
            }
        }

        b.btn2Resume.setOnClickListener {
            findNavController().navigate(R.id.frg_resume)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = super.onCreateView(inflater, container, savedInstanceState)

        val b = FragmentInterestsBinding.bind(v!!)

        Interests.values().forEach {
            val cb = CheckBox(context).apply {
                text = it.toString().lowercase()
                tag = it
            }
            b.llayout.addView(cb, b.llayout.childCount - 1)
        }
        return v
    }

    private fun updateInterests(interest: Interests) {
        viewModel.setData(interest)
    }

}

@Component(dependencies = [MainActivityComponent::class])
@InterestsScope
interface InterestsFragmentComponent {


    companion object {

        fun create(mainActivityComponent: MainActivityComponent): InterestsFragmentComponent {
            return DaggerInterestsFragmentComponent.builder().mainActivityComponent(mainActivityComponent).build()
        }

    }

    fun inject(frg: InterestsFragment)

}

@Scope
annotation class InterestsScope
