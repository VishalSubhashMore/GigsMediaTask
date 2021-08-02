package com.example.gigsmediatask.ui

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.gigsmediatask.R
import com.example.gigsmediatask.database.entity.UserEntity
import com.example.gigsmediatask.databinding.FragmentTask1Binding
import com.example.gigsmediatask.viewmodel.UserViewModel
import com.example.gigsmediatask.work.DownloadPdfWork

class Task1Fragment : Fragment() {

    private lateinit var binding: FragmentTask1Binding
    private lateinit var userViewModel: UserViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_task1, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.btnSave.setOnClickListener() {
            if (validate()) {
                userViewModel.insertUser(
                    UserEntity(
                        name = binding.etName.text.toString(),
                        mobileNo = binding.etMobileNumber.text.toString(),
                        bookName = binding.spnBook.selectedItem.toString()
                    )
                )
                Toast.makeText(requireContext(), "User Added!!!", Toast.LENGTH_SHORT).show()
                binding.etName.setText("")
                binding.etMobileNumber.setText("")
                binding.spnBook.setSelection(0)
            }
        }

    }

    fun validate(): Boolean {
        if (TextUtils.isEmpty(binding.etName.text.toString().trim())) {
            binding.etName.error = "Please enter valid name"
            return false
        }

        if (TextUtils.isEmpty(
                binding.etMobileNumber.text.toString().trim()
            ) || binding.etMobileNumber.text.toString().trim().length != 10
        ) {
            binding.etMobileNumber.error = "Please enter valid mobile no"
            return false
        }

        if (binding.spnBook.selectedItemPosition == 0) {
            Toast.makeText(requireContext(), "Please select book name", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}