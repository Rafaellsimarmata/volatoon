package com.example.volatoon.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.volatoon.model.Comic
import com.example.volatoon.model.Genres
import com.example.volatoon.model.RecentSearch
import com.example.volatoon.viewmodel.SearchViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewState : SearchViewModel,
    genres : List<Genres>,
    navigateToDetail : (String) -> Unit,
    navigateToGenre: (String) -> Unit,
    ){

    Log.d("SearchScreen", "Initial genres size: ${genres.size}")

    val searchText by viewState.searchText.collectAsState()
    val isSearching by viewState.isSearching.collectAsState()
    val comicsState by viewState.comics.collectAsState()

    Column (
        modifier = Modifier.fillMaxWidth().padding(10.dp).wrapContentHeight()
    ){
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    onQueryChange = viewState::onSearchTextChange,
                    query = searchText,
                    onSearch = { text ->
                        // This is called when user presses enter/search on keyboard
                        viewState.onSearchTextChange(text)
                        if (text.isNotEmpty()) {
                            viewState.addRecentSearch(text)
                        }
                    },
                    expanded = isSearching,
                    onExpandedChange = {},
                    placeholder = { Text("Find the comic!") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        Icon(Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                viewState.onClearSearch()
                            }
                        )
                    },
                )
            },
            expanded = isSearching,
            onExpandedChange = {}
        ) {
            when {
                comicsState.loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
                    )
                }

                comicsState.error != null -> {
                    Text(text = "ERROR OCCURRED ${comicsState.error}")
                }

                else -> {
                    LazyColumn {
                        items(comicsState.listComic){
                                comic ->
                            ComicSearchItem(
                                comic = comic,
                                navigateToDetail = navigateToDetail,
                                onSearchClick = {
                                    viewState.onSearchTextChange(comic.title)
                                    viewState.addRecentSearch(comic.title)
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("Recent Search", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)

        val recentSearches by viewState.recentSearches.collectAsState()

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp) // Adjust the height as needed
        ) {
            items(recentSearches) { search ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            viewState.onSearchTextChange(search.searchText)
                            viewState.addRecentSearch(search.searchText)
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = search.searchText,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "Delete",
                            modifier = Modifier
                                .padding(4.dp)
                                .clickable { viewState.deleteRecentSearch(search.searchText) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("Favourite Genres", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(15.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth()
                .height(480.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(genres) { genre ->
                GenreItem(
                    genre = genre,
                    onGenreClick = { navigateToGenre(genre.genre_id) }
                )
            }
        }
    }
}

@Composable
fun RecentSearches(
    searches: List<RecentSearch>,
    onSearchClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit
) {
    Column {
        if (searches.isEmpty()) {
            Text("No recent searches")
            return
        }

        searches.forEach { search ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onSearchClick(search.searchText) },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = search.searchText,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Delete",
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable { onDeleteClick(search.searchText) }
                    )
                }
            }
        }
    }
}

@Composable
fun ComicSearchItem(
    comic: Comic,
    navigateToDetail: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.9f),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(10.dp)
            .clickable {
                onSearchClick()
                navigateToDetail(comic.komik_id)
            },
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = comic.image),
            contentDescription = null,
            modifier = Modifier
                .width(71.dp)
                .height(90.dp)
                .aspectRatio(1f)
        )

        Spacer(modifier = Modifier.width(8.dp)) // Space between image and text

        Column(
            modifier = Modifier.weight(1f) // Allow title to take available space
        ) {
            Text(
                text = comic.title,
                fontWeight = FontWeight.Bold,
                maxLines = 2,             // Limit title to 2 lines
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Score: ${comic.score}",
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        // Type in a separate column for better alignment
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "Type: ${comic.type ?: "Unknown"}", // Display "Unknown" if type is null
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Chapter: ${comic.chapter}",
                fontSize = 12.sp
            )
        }
    }
}


@Composable
private fun GenreItem(
    genre: Genres,
    onGenreClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .border(
                1.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onGenreClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = getGenreEmoji(genre.name),
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = genre.name,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun getGenreEmoji(genreName: String): String {
    return when (genreName.lowercase(Locale.ROOT)) {
        "4-koma" -> "📚"
        "action" -> "⚔️"
        "adult" -> "🔞"
        "adventure" -> "🗺️"
        "comedy" -> "😂"
        "cooking" -> "👨‍🍳"
        "crime" -> "🚔"
        "dark fantasy" -> "🌑"
        "demons" -> "👿"
        "detective" -> "🔍"
        "drama" -> "🎭"
        "fantasy" -> "🧙‍♂️"
        "fantasy. school life. shounen" -> "🏫"
        "full color" -> "🎨"
        "game" -> "🎮"
        "gender bender", "genderswap" -> "⚧"
        "gore" -> "🩸"
        "gyaru" -> "👧"
        "harem" -> "👥"
        "historical" -> "📜"
        "horror" -> "👻"
        "isekai" -> "🌀"
        "josei" -> "👱‍♀️"
        "life slice of life", "slice of life" -> "☕"
        "magic" -> "✨"
        "manga" -> "📖"
        "manhua" -> "🀄"
        "manhwa" -> "🎎"
        "martial art", "martial arts" -> "🥋"
        "mature" -> "🔞"
        "mecha" -> "🤖"
        "medical" -> "⚕️"
        "military" -> "💂"
        "monsters" -> "👾"
        "music" -> "🎵"
        "mystery" -> "🔎"
        "ninja" -> "🥷"
        "one shot", "oneshot" -> "📝"
        "parody" -> "🃏"
        "police" -> "👮"
        "psychological" -> "🧠"
        "reincarnation" -> "♻️"
        "reverse harem" -> "👥"
        "romance" -> "❤️"
        "samurai" -> "⚔️"
        "school", "school life" -> "🏫"
        "sci-fi" -> "🚀"
        "seinen" -> "👨"
        "shoujo" -> "🎀"
        "shounen" -> "👦"
        "sport", "sports" -> "⚽"
        "super power", "superpowers" -> "💪"
        "superhero", "superheroes" -> "🦸"
        "supernatural", "supranatural" -> "👻"
        "survival" -> "🏃"
        "thriller" -> "😱"
        "tragedy" -> "💔"
        "vampire" -> "🧛"
        "villainess" -> "👑"
        "webtoon", "webtoons" -> "📱"
        "zombies" -> "🧟"
        else -> "📚"
    }
}