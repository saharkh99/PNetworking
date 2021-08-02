package com.example.pnetworking.ui.features

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.pnetworking.databinding.ActivitySettingBinding
import com.example.pnetworking.ui.base.test.TestActivity
import com.example.pnetworking.ui.groupchat.CreateGroupActivity
import com.example.pnetworking.utils.ChatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.android.viewmodel.ext.android.viewModel

class SettingsActivity : ChatActivity() {
    lateinit var binding: ActivitySettingBinding
    private val mainViewModel by viewModel<SettingsViewModel>()
    lateinit var changeEmailButton: Button
    lateinit var changePasswordButton: Button
    lateinit var blackListsButton: Button
    lateinit var testButton: Button
    lateinit var reportButton: Button
    lateinit var saveEmail: Button
    lateinit var savePassword: Button
    lateinit var cancelEmail: Button
    lateinit var cancelPassword: Button

    lateinit var currentEmail: EditText
    lateinit var passwords: EditText
    lateinit var newEmail: EditText
    lateinit var currentEmail2: EditText
    lateinit var currentPasswords: EditText
    lateinit var newPasswords: EditText
    lateinit var repost: EditText
    lateinit var theme: SwitchMaterial


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        init()
        changeEmailButton.setOnClickListener {
            changeEmail()
        }
        changePasswordButton.setOnClickListener {
            changePassword()
        }
        blackListsButton.setOnClickListener {
            getBlocks()
        }
        testButton.setOnClickListener {
            moveToTest()
        }
        cancelEmail.setOnClickListener {
            cancelEmailFun()
        }
        cancelPassword.setOnClickListener {
            cancelPasswordFun()
        }
    }

    private fun cancelPasswordFun() {
        currentEmail2.visibility = View.GONE
        currentPasswords.visibility = View.GONE
        newPasswords.visibility = View.GONE
        changePasswordButton.visibility = View.VISIBLE
        savePassword.visibility = View.GONE
        cancelPassword.visibility=View.GONE
    }

    private fun cancelEmailFun() {
        currentEmail.visibility = View.GONE
        passwords.visibility = View.GONE
        newEmail.visibility = View.GONE
        changeEmailButton.visibility = View.VISIBLE
        saveEmail.visibility = View.GONE
        cancelEmail.visibility = View.GONE

    }

    private fun moveToTest() {
        startActivity(Intent(this, TestActivity::class.java))
    }

    private fun getBlocks() {
        val intent = Intent(this, FriendsActivity::class.java)
        intent.putExtra("block", "block")
        startActivity(intent)
    }

    private fun changePassword() {
        currentEmail2.visibility = View.VISIBLE
        currentPasswords.visibility = View.VISIBLE
        newPasswords.visibility = View.VISIBLE
        changePasswordButton.visibility = View.GONE
        savePassword.visibility = View.VISIBLE
        cancelPassword.visibility=View.VISIBLE

        savePassword.setOnClickListener {
            mainViewModel.updatePassword(
                currentEmail2.text.toString(),
                currentPasswords.text.toString(),
                newPasswords.text.toString()
            )
            currentEmail2.visibility = View.GONE
            currentPasswords.visibility = View.GONE
            newPasswords.visibility = View.GONE
            changePasswordButton.visibility = View.VISIBLE
            savePassword.visibility = View.GONE
            cancelPassword.visibility=View.GONE

        }
    }

    private fun changeEmail() {
        currentEmail.visibility = View.VISIBLE
        passwords.visibility = View.VISIBLE
        newEmail.visibility = View.VISIBLE
        changeEmailButton.visibility = View.GONE
        saveEmail.visibility = View.VISIBLE
        cancelEmail.visibility = View.VISIBLE
        saveEmail.setOnClickListener {
            mainViewModel.updateEmail(
                currentEmail.text.toString(), passwords.text.toString(), newEmail.text.toString()
            )
            currentEmail.visibility = View.GONE
            passwords.visibility = View.GONE
            newEmail.visibility = View.GONE
            changeEmailButton.visibility = View.VISIBLE
            saveEmail.visibility = View.GONE
            cancelEmail.visibility = View.GONE


        }


    }

    private fun init() {
        changeEmailButton = binding.addUser
        changePasswordButton = binding.settingsGo
        blackListsButton = binding.settingsGo3
        testButton = binding.settingsGo4
        reportButton = binding.settingsGo6
        currentEmail = binding.settingsCurrentEmail
        passwords = binding.settingsPassword
        newEmail = binding.settingsNewEmail
        currentEmail2 = binding.settingsCurrentEmail2
        currentPasswords = binding.settingsPassword2
        newPasswords = binding.settingsNewEmail2
        repost = binding.settingsReportTxt
        theme = binding.switchTheme
        saveEmail = binding.settingsSave
        savePassword = binding.settingsSave2
        cancelEmail = binding.settingsCancel
        cancelPassword = binding.settingsCancel2

    }
}