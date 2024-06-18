package com.example.tryingtomakefetchhappen

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tryingtomakefetchhappen.model.HiringItem
import com.example.tryingtomakefetchhappen.sampledata.HiringItemSampleData
import com.example.tryingtomakefetchhappen.ui.theme.TryingToMakeFetchHappenTheme
import com.example.tryingtomakefetchhappen.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TryingToMakeFetchHappenTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Should the Surface call go inside `HiringItemsScreen`?
                    Surface(modifier = Modifier.padding(innerPadding)) {
                        HiringItemsScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun HiringItemsScreen(viewModel: MainViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    uiState.hiringItemState.data?.let { itemMap ->
        HiringItemList(itemMap)
    }

    uiState.hiringItemState.error?.let {
        Timber.e(it, "Error when loading hiring items")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HiringItemList(itemMap: Map<Int, List<HiringItem>>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        itemMap.forEach { (listId, items) ->
            stickyHeader() {
                Surface(
                    tonalElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "List ID: $listId",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                    )
                }
            }
            items(items) { item ->
                HiringItemCard(item)
            }
        }
    }
}

@Composable
fun HiringItemCard(item: HiringItem) {
    ListItem(
        headlineContent = { Text("ID: ${item.id}")},
        supportingContent = { Text("Name: ${item.name}")}
    )
    HorizontalDivider()
}

@Preview(
    showBackground = true,
    name = "Light Mode",
)
@Preview(
    uiMode = UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode",

)
@Composable
fun HiringItemListPreview() {
    TryingToMakeFetchHappenTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            // Should the Surface call go inside `HiringItemsScreen`?
            Surface(modifier = Modifier.padding(innerPadding)) {
                HiringItemList(HiringItemSampleData.hiringItemMap)
            }
        }
    }
}