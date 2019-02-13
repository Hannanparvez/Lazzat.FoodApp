package com.project.lazzatproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class splashActivity : AppCompatActivity() {

        private var mDelayHandler: Handler? = null
        private val SPLASH_DELAY: Long = 1000 //1 seconds

        private val mRunnable: Runnable = Runnable {
            if (!isFinishing) {

                val intent = Intent(applicationContext, signin::class.java)
                startActivity(intent)
                finish()
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_splash)

            //Initialize the Handler
            mDelayHandler = Handler()

            //Navigate with delay
            mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

        }

        public override fun onDestroy() {

            if (mDelayHandler != null) {
                mDelayHandler!!.removeCallbacks(mRunnable)
            }

            super.onDestroy()
        }

    }