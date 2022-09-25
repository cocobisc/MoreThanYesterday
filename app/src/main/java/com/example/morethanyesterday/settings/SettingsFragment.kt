package com.example.morethanyesterday.settings

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.morethanyesterday.R
import com.example.morethanyesterday.alarm.WeatherAlarmManager
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {
    private val settingsPreference by lazy { SettingsSharedPreference(requireContext()) }
    @Inject
    lateinit var weatherAlarmManager: WeatherAlarmManager

    private lateinit var toggle: SwitchPreference
    private lateinit var time: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)

        toggle = findPreference("toggle")!!
        time = findPreference("time")!!

        initToggle()
        initTime()
    }

    private fun initToggle() {
        toggle.isChecked = settingsPreference.notiEnabled
        toggle.setOnPreferenceChangeListener { _, isChecked ->
            isChecked as Boolean

            if (isChecked) {
                setAlarm()
            } else {
                cancelAlarm()

                showToast("알람이 더이상 울리지 않습니다.")
            }
            settingsPreference.notiEnabled = isChecked

            true
        }
    }

    private fun initTime() {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, settingsPreference.notiHour)
        cal.set(Calendar.MINUTE, settingsPreference.notiMinute)

        time.summary = SimpleDateFormat("HH:mm").format(cal.time)
        time.setOnPreferenceClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                val text = SimpleDateFormat("HH:mm").format(cal.time)
                time.summary = text

                settingsPreference.notiHour = hour
                settingsPreference.notiMinute = minute

                cancelAlarm()
                setAlarm()

                showToast("알람이 매일 ${text}에 울립니다.")
            }

            TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()

            true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setAlarm() {
        val hour = settingsPreference.notiHour
        val min = settingsPreference.notiMinute

        weatherAlarmManager.startAlarm(hour, min)
    }

    private fun cancelAlarm() {
        weatherAlarmManager.cancelAlarm()
    }
}