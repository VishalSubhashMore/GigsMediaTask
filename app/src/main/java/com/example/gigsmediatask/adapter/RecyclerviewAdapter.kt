package com.example.gigsmediatask.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.gigsmediatask.R
import com.example.gigsmediatask.databinding.LayoutGithubdataListItemBinding
import com.example.gigsmediatask.model.GithubData

class RecyclerviewAdapter() :
    RecyclerView.Adapter<RecyclerviewAdapter.MyViewHolder>() {

    private lateinit var githubDataList:List<GithubData>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.layout_githubdata_list_item, parent, false
        )
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.recyclerViewItemBinding.githubData = githubDataList[position]
        Glide.with(holder.recyclerViewItemBinding.ivOwner)
            .load(githubDataList[position].owner?.avatar_url)
            .apply(RequestOptions.centerCropTransform())
            .into(holder.recyclerViewItemBinding.ivOwner)
    }

    fun setList(githubList:List<GithubData>){
        githubDataList = githubList
        notifyDataSetChanged()
    }

    override fun getItemCount() = githubDataList.size

    inner class MyViewHolder(val recyclerViewItemBinding: LayoutGithubdataListItemBinding) :
        RecyclerView.ViewHolder(recyclerViewItemBinding.root)
}