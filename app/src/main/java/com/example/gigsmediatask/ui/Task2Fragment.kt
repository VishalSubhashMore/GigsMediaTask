package com.example.gigsmediatask.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gigsmediatask.R
import com.example.gigsmediatask.adapter.UserRecyclerViewAdapter
import com.example.gigsmediatask.adapter.UserRecyclerViewAdapter.OnDeleteListener
import com.example.gigsmediatask.database.entity.UserEntity
import com.example.gigsmediatask.databinding.FragmentTask2Binding
import com.example.gigsmediatask.viewmodel.UserViewModel

class Task2Fragment : Fragment() {


    private lateinit var binding: FragmentTask2Binding
    private lateinit var userViewModel: UserViewModel
    private lateinit var userRecyclerViewAdapter: UserRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_task2, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        userViewModel.getUsers().observe(requireActivity(), {
            binding.progressBar.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            if (it != null) {
                userRecyclerViewAdapter.setUserList(it)
            } else {
                Toast.makeText(requireContext(), "Error in getting Data", Toast.LENGTH_SHORT).show()
            }
        })

        initRecyclerView()
        userViewModel.getAllUserList()

    }

    private fun initRecyclerView() {
        userRecyclerViewAdapter = UserRecyclerViewAdapter(object : OnDeleteListener {
            override fun onDelete(userEntity: UserEntity) {
                userViewModel.deleteUser(userEntity)
            }
        }, object : UserRecyclerViewAdapter.OnSelectListener {
            override fun onSelected(userEntity: UserEntity) {
                val bundle = bundleOf(
                    "id" to userEntity.id,
                    "name" to userEntity.name,
                    "mobile" to userEntity.mobileNo,
                    "bookname" to userEntity.bookName
                )
                this@Task2Fragment.findNavController()
                    .navigate(R.id.action_navigation_task2_to_navigation_update, bundle)
            }
        })
        userRecyclerViewAdapter.setUserList(emptyList())
        binding.recyclerView.adapter = userRecyclerViewAdapter
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }
}