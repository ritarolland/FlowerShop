package com.example.prac1.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prac1.app.MyApplication
import com.example.prac1.databinding.FragmentCartBinding
import com.example.prac1.presentation.adapter.CartAdapter
import com.example.prac1.presentation.viewmodel.CartViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class FragmentCart : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        (requireActivity().application as MyApplication).appComponent.inject(this)

        cartViewModel = ViewModelProvider(this, viewModelFactory)[CartViewModel::class.java]
        cartAdapter = CartAdapter { v -> cartViewModel.getItemById(v) }
        binding.recyclerViewCart.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }

        lifecycleScope.launch {
            cartViewModel.cartItems.collect { items ->
                cartAdapter.submitList(items)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}