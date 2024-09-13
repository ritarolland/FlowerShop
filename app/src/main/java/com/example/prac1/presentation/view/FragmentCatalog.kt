package com.example.prac1.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prac1.R
import com.example.prac1.databinding.FragmentCatalogBinding
import com.example.prac1.domain.model.Flower
import com.example.prac1.presentation.adapter.CatalogAdapter
import com.example.prac1.presentation.interfaces.ItemClickInterface
import com.example.prac1.presentation.viewmodel.CatalogViewModel
import kotlinx.coroutines.launch
import com.example.prac1.app.MyApplication
import javax.inject.Inject


class FragmentCatalog : Fragment(), ItemClickInterface {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var catalogViewModel: CatalogViewModel
    private lateinit var catalogAdapter: CatalogAdapter
    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity().application as MyApplication).appComponent.inject(this)

        catalogViewModel = ViewModelProvider(this, viewModelFactory)[CatalogViewModel::class.java]

        catalogAdapter = CatalogAdapter { v -> onItemClicked(v) }
        binding.recyclerViewCatalog.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = catalogAdapter
        }

        lifecycleScope.launch {
            catalogViewModel.catalogItems.collect { items ->
                catalogAdapter.submitList(items)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(item: Flower) {
        findNavController().navigate(
            R.id.action_catalog_to_details,
            bundleOf("itemId" to item.id)
        )
    }
}