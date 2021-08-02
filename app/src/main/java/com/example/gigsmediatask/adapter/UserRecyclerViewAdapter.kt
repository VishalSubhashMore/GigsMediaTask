package com.example.gigsmediatask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.gigsmediatask.R
import com.example.gigsmediatask.database.entity.UserEntity
import com.example.gigsmediatask.databinding.LayoutGithubdataListItemBinding
import com.example.gigsmediatask.databinding.LayoutUserListItemBinding

class UserRecyclerViewAdapter(
    val onDeleteListener: OnDeleteListener,
    val onSelectListener: OnSelectListener
) :
    RecyclerView.Adapter<UserRecyclerViewAdapter.MyViewHolder>() {

    private lateinit var userList: List<UserEntity>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_user_list_item,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.userListItemBinding.user = userList[position]
        holder.userListItemBinding.ivDelete.setOnClickListener {
            onDeleteListener.onDelete(userList[position])
        }

        holder.userListItemBinding.content.setOnClickListener {
            onSelectListener.onSelected(userList[position])
        }
    }

    fun setUserList(userList: List<UserEntity>) {
        this.userList = userList
        notifyDataSetChanged()
    }

    override fun getItemCount() = userList.size

    inner class MyViewHolder(val userListItemBinding: LayoutUserListItemBinding) :
        RecyclerView.ViewHolder(userListItemBinding.root)

    interface OnDeleteListener {
        fun onDelete(userEntity: UserEntity)
    }

    interface OnSelectListener {
        fun onSelected(userEntity: UserEntity)
    }

}