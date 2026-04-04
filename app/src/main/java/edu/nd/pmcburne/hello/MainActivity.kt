package edu.nd.pmcburne.hello

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.nd.pmcburne.hello.ui.CampusScreen
import edu.nd.pmcburne.hello.ui.CampusViewModel
import edu.nd.pmcburne.hello.ui.CampusViewModelFactory
import edu.nd.pmcburne.hello.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: CampusViewModel by viewModels {
        CampusViewModelFactory(
            CampusRepository(
                AppDatabase.getDatabase(applicationContext).locationDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val tags = viewModel.tags.collectAsStateWithLifecycle().value
                val selectedTag = viewModel.selectedTag.collectAsStateWithLifecycle().value
                val locations = viewModel.locations.collectAsStateWithLifecycle().value

                CampusScreen(
                    tags = tags,
                    selectedTag = selectedTag,
                    locations = locations,
                    onTagSelected = viewModel::updateSelectedTag
                )
            }
        }
    }
}