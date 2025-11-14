package com.example.crikstats

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.crikstats.ui.theme.CrikStatsTheme
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CrikStatsTheme {
                MainScreen()
            }
        }
    }
}


@Composable
fun MainScreen() {
    val context = LocalContext.current
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = {
                installModule(context = context)
            }) {
                Text(text = "Download Player module")
            }
        }
    }

}

fun installModule(context: Context) {
    val mSplitInstallManager = SplitInstallManagerFactory.create(context)

    val request = SplitInstallRequest.newBuilder()
        .addModule("feature_player")
        .build()
    val listener = SplitInstallStateUpdatedListener { state ->
        when (state.status()) {
            SplitInstallSessionStatus.DOWNLOADING -> {
                Log.i("TAG", "FeatureModule Downloading")
            }

            SplitInstallSessionStatus.INSTALLED -> {
                try {
                    val intent = Intent().setClassName(
                        context.packageName,
                        "com.example.feature_player.PlayerActivity"
                    )
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Log.e("DynamicFeature", "Failed to open MainActivity: ${e.message}")
                }
                Log.i("TAG", "FeatureModule Installed")
            }

            SplitInstallSessionStatus.FAILED -> {
                Log.i("TAG", "FeatureModule ErrorCode: ${state.errorCode()}")
            }
        }
    }
    mSplitInstallManager.registerListener(listener)

    if (!mSplitInstallManager.installedModules.contains("feature_player")) {
        mSplitInstallManager.startInstall(request)
    }else{
        val intent = Intent().setClassName(
            context as MainActivity,
            "com.example.feature_player.PlayerActivity"
        )
        (context).startActivity(intent)
    }
}