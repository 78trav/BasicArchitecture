package ru.otus.basicarchitecture

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.otus.basicarchitecture.databinding.FragmentInterestsBinding
import javax.inject.Inject

@RegistrationScope
class InterestsFragment : Fragment(R.layout.fragment_interests) {

    companion object {
        fun newInstance() = InterestsFragment()
    }

    @Inject
    lateinit var viewModelFactory: InterestsViewModelFactory

    private val viewModel by viewModels<InterestsViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).getComponent().inject(this)
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
