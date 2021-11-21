package com.udacity.asteroidradar.main


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.ListItemAsteroidBinding
import com.udacity.asteroidradar.models.Asteroid

class AsteroidAdapter(private val onClickListener: OnClickListener) :
  ListAdapter<Asteroid, AsteroidAdapter.ViewHolder>(AsteroidDiffCallback()) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val view = layoutInflater
      .inflate(R.layout.list_item_asteroid, parent, false) as ConstraintLayout
    return ViewHolder.from(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val asteroid = getItem(position)
    holder.bind(asteroid)
    holder.itemView.setOnClickListener {
      onClickListener.onClick(asteroid)
    }
  }

  class OnClickListener(val clickListener: (marsProperty: Asteroid) -> Unit) {
    fun onClick(marsProperty: Asteroid) = clickListener(marsProperty)
  }

  class ViewHolder private constructor(val binding: ListItemAsteroidBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(asteroid: Asteroid) {
      binding.asteroid = asteroid
      binding.executePendingBindings()
    }

    companion object {
      fun from(parent: ViewGroup): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemAsteroidBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
      }
    }
  }

  class AsteroidDiffCallback : DiffUtil.ItemCallback<Asteroid>() {
    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
      return oldItem == newItem
    }
  }

}