package com.example.prac1.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.prac1.R
import com.example.prac1.app.MyApplication
import com.example.prac1.databinding.FragmentAuthBinding
import com.example.prac1.presentation.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class FragmentAuth : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity().application as MyApplication).appComponent.inject(this)

        authViewModel = ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]

        authViewModel.checkAuthorization()
        if (authViewModel.isAuthorized.value) {
            findNavController().navigate(R.id.action_auth_to_catalog)
        } //пока выполняется показать индикатор загрузки

        binding.enter.setOnClickListener { _ -> onClickSignIn() }
        binding.register.setOnClickListener { _ -> onClickSignUpNow() }
    }

    private fun onClickSignIn() {
        val email = binding.emailEdittext.text.toString()
        val password = binding.passwordEdittext.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            authViewModel.signIn(email = email, password = password)
            viewLifecycleOwner.lifecycleScope.launch {
                authViewModel.signInState.collect { result ->
                    when (result) {
                        is AuthResult.Loading -> {
                            // Показать индикатор загрузки
                        }

                        is AuthResult.Success -> {
                            if (result.data) {
                                findNavController().navigate(R.id.action_auth_to_catalog)
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Sign in failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        is AuthResult.Error -> {
                            Toast.makeText(
                                requireContext(),
                                "Sign in failed: ${result.exception.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        } else {
            Toast.makeText(requireContext(), "Fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

    // Обработка клика по ссылке "Зарегистрироваться"
    private fun onClickSignUpNow() {
        // Логика для навигации на экран регистрации
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

