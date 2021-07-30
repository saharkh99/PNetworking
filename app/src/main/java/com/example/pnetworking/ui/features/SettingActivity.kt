package com.example.pnetworking.ui.features

import android.os.Bundle
import android.preference.*
import android.preference.Preference
import android.preference.Preference.OnPreferenceClickListener
import android.util.Log
import android.view.MenuItem
import com.example.pnetworking.R


class SettingsActivity : AppCompatPreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // load settings fragment
        fragmentManager.beginTransaction().replace(android.R.id.content, MainPreferenceFragment()).commit()

    }

    class MainPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.preferences)

            // gallery EditText change listener
            bindPreferenceSummaryToValue(findPreference("key_email_name"))

            // notification preference change listener
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
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                val index = preference.findIndexOfValue(stringValue)

                // Set the summary to reflect the new value.
                preference.setSummary(
                    if (index >= 0)
                        preference.entries[index]
                    else
                        null)

            } else if (preference is EditTextPreference) {
                if (preference.getKey() == "key_password_name") {
                    preference.setSummary(stringValue)
                }
            } else {
                preference.summary = stringValue
            }
            true
        }
    }
}