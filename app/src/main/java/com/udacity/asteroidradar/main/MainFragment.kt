package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.MainActivity
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentDetailBinding
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.utils.addDays
import com.udacity.asteroidradar.utils.getFormattedDate
import com.udacity.asteroidradar.utils.getSeventhDay
import com.udacity.asteroidradar.utils.getToday
import java.util.Calendar

class MainFragment : Fragment() {

  private val viewModel: MainViewModel by lazy {
    val activity = requireNotNull(this.activity) {
      "You can only access the viewModel after onViewCreated()"
    }
    ViewModelProvider(this,
                      MainViewModel.Factory(activity.application))[MainViewModel::class.java]
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View? {
    val binding = FragmentMainBinding.inflate(inflater)
    binding.lifecycleOwner = this

    binding.viewModel = viewModel

    setHasOptionsMenu(true)

    val adapter = AsteroidAdapter(AsteroidAdapter.OnClickListener {
      viewModel.displayAsteroidDetails(it)
    })

    binding.asteroidRecycler.adapter = adapter

    viewModel.asteroids.observe(viewLifecycleOwner, {
      it?.let {
        adapter.submitList(it)
      }
    })

    viewModel.navigateToSelectedAsteroid.observe(this, {
      if (null != it) {
        this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
        viewModel.displayAsteroidDetailsComplete()
      }
    })

    viewModel.pictureOfDay.observe(this, {
      if (null != it) {
        binding.activityMainImageOfTheDay.contentDescription = it.title
      }
    })

    setHasOptionsMenu(true)
    return binding.root
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.main_overflow_menu, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    val dates: Pair<String, String> = when (item.itemId) {
      R.id.show_all_menu -> getToday() to getSeventhDay()
      R.id.show_rent_menu -> getToday() to getToday()
      else -> getToday() to getSeventhDay()
    }
    viewModel.updateFilter(dates.first, dates.second)
    return true
  }
}
