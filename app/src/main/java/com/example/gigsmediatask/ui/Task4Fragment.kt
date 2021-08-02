package com.example.gigsmediatask.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.gigsmediatask.R
import com.example.gigsmediatask.databinding.FragmentTask4Binding
import com.example.gigsmediatask.work.DownloadPdfWork

class Task4Fragment : Fragment() {
    private lateinit var binding: FragmentTask4Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_task4, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDownload.setOnClickListener {
            val uploadWorkRequest: WorkRequest =
                OneTimeWorkRequestBuilder<DownloadPdfWork>()
                    .build()

            WorkManager
                .getInstance(requireContext())
                .enqueue(uploadWorkRequest)
        }
    }
}