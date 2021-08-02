package com.example.gigsmediatask.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.gigsmediatask.R
import com.example.gigsmediatask.adapter.RecyclerviewAdapter
import com.example.gigsmediatask.databinding.FragmentTask3Binding
import com.example.gigsmediatask.viewmodel.Task3ViewModel

class Task3Fragment : Fragment() {

    private lateinit var binding: FragmentTask3Binding
    private lateinit var recyclerviewAdapter: RecyclerviewAdapter

    private lateinit var task3ViewModel: Task3ViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_task3, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        task3ViewModel = ViewModelProvider(this).get(Task3ViewModel::class.java)
        task3ViewModel.getGithubLiveData().observe(requireActivity(), {
            binding.progressBar.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            if (it != null) {
                recyclerviewAdapter.setList(it.items)
            } else {
                Toast.makeText(requireContext(), "Error in getting Data", Toast.LENGTH_SHORT).show()
            }
        })
        task3ViewModel.callGithubAPI()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerviewAdapter = RecyclerviewAdapter()
        recyclerviewAdapter.setList(emptyList())
        binding.recyclerView.adapter = recyclerviewAdapter
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }

}