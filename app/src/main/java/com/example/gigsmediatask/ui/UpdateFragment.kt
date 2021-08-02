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
import androidx.navigation.fragment.findNavController
import com.example.gigsmediatask.R
import com.example.gigsmediatask.database.entity.UserEntity
import com.example.gigsmediatask.databinding.FragmentUpdateBinding
import com.example.gigsmediatask.viewmodel.UserViewModel


class UpdateFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var id: Int? = 0
    private var name: String? = null
    private var mobileNo: String? = null
    private var bookName: String? = null

    private lateinit var binding: FragmentUpdateBinding
    private lateinit var userViewModel: UserViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getInt("id")
            name = it.getString("name")
            mobileNo = it.getString("mobile")
            bookName = it.getString("bookname")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_update, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        binding.user = UserEntity(name = name, mobileNo = mobileNo, bookName = bookName)
        val bookNames = resources.getStringArray(R.array.book_names)

        var bookPosition = 0
        bookNames.forEachIndexed { index, s ->
            if (bookName.equals(s, true)) {
                bookPosition = index
            }
        }
        binding.spnBook.setSelection(bookPosition)

        binding.btnUpdate.setOnClickListener {
            if (validate()) {
                userViewModel.updateUser(
                    UserEntity(
                        id = id!!,
                        name = binding.etName.text.toString(),
                        mobileNo = binding.etMobileNumber.text.toString(),
                        bookName = binding.spnBook.selectedItem.toString()
                    )
                )
                Toast.makeText(requireContext(), "User Updated!!!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
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