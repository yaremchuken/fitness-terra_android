package exp.yaremchuken.fitnessterra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import exp.yaremchuken.fitnessterra.data.datasource.YamlDatasource
import exp.yaremchuken.fitnessterra.ui.route.Navigation
import exp.yaremchuken.fitnessterra.ui.theme.FitnessTerraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        YamlDatasource.preload(this)
        setContent {
            FitnessTerraTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Navigation()
                }
            }
        }
    }
}