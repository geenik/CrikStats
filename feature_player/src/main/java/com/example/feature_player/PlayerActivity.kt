package com.example.feature_player

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.crikstats.di.AppEntryPoint
import com.example.feature_player.ui.PlayerScreen
import com.example.feature_player.ui.PlayerViewModel
import com.google.android.play.core.splitcompat.SplitCompat
import dagger.hilt.android.EntryPointAccessors

class PlayerActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val entryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            AppEntryPoint::class.java
        )

        val repo = entryPoint.getRepo()

        val playerViewModel = PlayerViewModel(repo)
        setContent {
            PlayerScreen(playerViewModel) {
                finish()
            }
        }
    }
}