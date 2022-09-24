package com.example.morethanyesterday.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.morethanyesterday.Constants
import com.example.morethanyesterday.R
import com.example.morethanyesterday.databinding.FragmentWeatherBinding
import com.example.morethanyesterday.viewmodel.WeatherViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class WeatherFragment: Fragment(R.layout.fragment_weather) {
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val binding by viewBinding(FragmentWeatherBinding::bind)

    private val degree = "°C"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initListeners()
        requestForInit()
    }

    private fun initListeners() {
        with(binding.refreshLayout) {
            setOnRefreshListener {
                weatherViewModel.getWeather()
                isRefreshing = false
            }
        }
    }

    private fun initObservers() {
        weatherViewModel.isLoading.launchAndCollectIn {
            binding.loadingLayout.isVisible = it
        }

        weatherViewModel.weatherUiState.launchAndCollectIn {
            binding.title.text = it.title
            binding.address.text = it.address

            it.mainImageRes.takeIf { res -> res != -1 }?.let { res ->
                binding.mainImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), res))
            }

            binding.tempLayout.isVisible = it.tempVO != null
            binding.date.text = it.date + "기준"
            binding.prevTemp.text = it.tempVO?.prevTemp.toString() + degree
            binding.currTemp.text = it.tempVO?.currTemp.toString() + degree

            Glide.with(this@WeatherFragment)
                .load(Constants.ICON_URL + it.iconName + ".png")
                .into(binding.weatherIcon)
        }

        weatherViewModel.toastMessage.launchAndCollectIn { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestForInit() {
        weatherViewModel.getWeather()
    }

    private inline fun <T> Flow<T>.launchAndCollectIn(
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        crossinline action: suspend CoroutineScope.(T) -> Unit
    ) = viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(minActiveState) {
            collect {
                action(it)
            }
        }
    }
}