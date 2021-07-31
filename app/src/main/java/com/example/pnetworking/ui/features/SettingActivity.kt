package com.example.pnetworking.ui.features

import android.content.Intent
import android.os.Bundle
import android.preference.*
import android.preference.Preference
import android.view.MenuItem
import com.example.pnetworking.R


lateinit var themeName:String
class SettingsActivity : AppCompatPreferenceActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pref = PreferenceManager
            .getDefaultSharedPreferences(this)
        themeName = pref.getString("pref_share", "purple")!!
        if (themeName == "purple") {
            setTheme(R.style.Theme_PNetworking)
        } else if (themeName == "white") {
            setTheme(R.style.AppTheme)
        }
        fragmentManager.beginTransaction().replace(android.R.id.content, MainPreferenceFragment()).commit()


    }

    class MainPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.preferences)

            bindPreferenceSummaryToValue(findPreference("key_email_name"))
            bindPreferenceSummaryToValue(findPreference("key_password_name"))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private val TAG = SettingsActivity::class.java!!.getSimpleName()

        private fun bindPreferenceSummaryToValue(preference: Preference) {
            preference.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener

            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                    .getDefaultSharedPreferences(preference.context)
                    .getString(preference.key, ""))
        }

        private val sBindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            val stringValue = newValue.toString()

            if (preference is ListPreference) {
                val index = preference.findIndexOfValue(stringValue)
                preference.setSummary(
                    if (index >= 0)
                        preference.entries[index]
                    else
                        null)

            } else if (preference is EditTextPreference) {
                if (preference.getKey() == "key_password_name") {
                    preference.setSummary(stringValue)

                }
                else if (preference.getKey() == "key_email_name") {
                    preference.setSummary(stringValue)
                }

            }
            else if(preference is SwitchPreference){
                if(!preference.isEnabled){
                    themeName="white"
                }else{
                    themeName="purple"
                }
            }
            else {
                if(preference.getKey() == "test"){
                    preference.setOnPreferenceClickListener {
                        true
                    }
                }
                preference.summary = stringValue
            }
            true
        }
    }
}