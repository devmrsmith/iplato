package com.iplato.spacex.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.button.MaterialButton
import com.iplato.spacex.R
import com.iplato.spacex.utils.SharedPreference
import com.iplato.spacex.viewmodels.WelcomeViewModel


class WelcomeFragment : Fragment() {

    companion object {
        fun newInstance() = WelcomeFragment()
    }

    private lateinit var viewModel: WelcomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // TODO:
        // Inject ViewModel
        // Move SharedPreferences into Repository
        // Add databinding

        val view = inflater.inflate(R.layout.welcome_fragment, container, false)

        // Configure button to navigate
        val button: MaterialButton = view.findViewById(R.id.btnStart) as MaterialButton
        button.setOnClickListener {
            it.isEnabled = false
            Navigation.findNavController(
                context as Activity,
                R.id.nav_host_fragment
            )
                .navigate(R.id.action_welcomeFragment_to_listFragment)
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WelcomeViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.activity?.let {

            // Store a Boolean preference flagging whether we've already displayed the welcome screen
            val sharedPreference: SharedPreference = SharedPreference(this.context)
            if (sharedPreference.getValueBoolean("welcome_shown")) {
                Navigation.findNavController(
                    it,
                    R.id.nav_host_fragment
                ).navigate(R.id.action_welcomeFragment_to_listFragment)
            }
            sharedPreference.save("welcome_shown", true)
        }
    }

}