package com.example.pnetworking.ui.base.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.pnetworking.R
import com.example.pnetworking.ui.MainActivity
import com.example.pnetworking.ui.base.signup.SignupActivity
import com.example.pnetworking.ui.base.test.TestActivity
import com.github.appintro.AppIntro
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType
import com.github.appintro.model.SliderPage

class IntroActivity : AppIntro2() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addSlide(
            AppIntroFragment.newInstance(
                "Welcome!",
                "instant communication platform for \n all of your friends to communicate ",
                imageDrawable = R.drawable.intro,
                backgroundDrawable = R.drawable.background
            )
        )

        addSlide(
            AppIntroFragment.newInstance(
                SliderPage(
                    "find your real friends!",
                    "find your soul mate based on your \n personality trait ",
                    imageDrawable = R.drawable.intro2,
                    backgroundDrawable = R.color.pink,
                    titleColor = resources.getColor(R.color.purple_500),
                    descriptionColor = resources.getColor(R.color.purple_500)

                )
            )
        )

        addSlide(
            AppIntroFragment.newInstance(
                "easy communication!",
                "converse with your friends \n and enjoy your time",
                imageDrawable = R.drawable.intro3,
                backgroundDrawable = R.color.purple_200 ,
                titleColor = resources.getColor(R.color.purple_700),
                descriptionColor = resources.getColor(R.color.purple_700)
            )
        )

        addSlide(
            AppIntroFragment.newInstance(
                ":)",
                "Let's get started",
                backgroundDrawable = R.color.teal_700

            )
        )

        setTransformer(AppIntroPageTransformerType.Parallax(
            titleParallaxFactor = 1.0,
            imageParallaxFactor = -1.0,
            descriptionParallaxFactor = 2.0,
        ))
    }

    public override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        finish()
    }

    public override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        startActivity(Intent(this, SignupActivity::class.java))
        finish()
    }
}

