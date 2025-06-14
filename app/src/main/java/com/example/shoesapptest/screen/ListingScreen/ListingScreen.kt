package com.example.shoesapptest.screen.ListingScreen

import ProductItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shoesapp.ui.theme.MatuleTheme
import com.example.shoesapptest.R
import com.example.shoesapptest.data.remote.network.NetworkResponseSneakers
import com.example.shoesapptest.screen.CartScreen.CartViewModel
import com.example.shoesapptest.screen.ForSneakers.SneakersViewModel
import com.example.shoesapptest.screen.HomeScreen.PopularViewModel
import com.example.shoesapptest.screen.HomeScreen.component.BottomBar
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutdoorScreen(
    navController: NavController,
    categories: List<String> = listOf("Всё", "Outdoor", "Tennis", "Basketball", "Running"),
    viewModel: CartViewModel = koinViewModel()
) {
    var selectedCategory by remember { mutableStateOf("Outdoor") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = selectedCategory,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.back_arrow),
                            contentDescription = "Назад",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
            )
        },
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            CategoryTabs(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = category
                },
                cartViewModel = viewModel
            )

            OutdoorContent(
                navController = navController,
                category = selectedCategory,
                cartViewModel = viewModel
            )
        }
    }
}

@Composable
fun CategoryTabs(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    cartViewModel: CartViewModel
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            Box(
                modifier = Modifier
                    .clickable { onCategorySelected(category) }
                    .background(
                        color = if (category == selectedCategory) MatuleTheme.colors.accent else Color.White,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = category,
                    color = if (category == selectedCategory) Color.White else Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun OutdoorContent(
    navController: NavController,
    category: String,
    viewModel: PopularViewModel = koinViewModel(),
    cartViewModel: CartViewModel
) {
    val sneakersState by viewModel.sneakersState.collectAsState()
    val favoritesState by viewModel.favoritesState.collectAsState()

    LaunchedEffect(category) {
        viewModel.fetchSneakersByCategory(category)
        viewModel.fetchFavorites()
    }

    val sneakersWithFavorites = remember(sneakersState, favoritesState) {
        if (sneakersState is NetworkResponseSneakers.Success) {
            val sneakers = (sneakersState as NetworkResponseSneakers.Success).data
            val favorites = (favoritesState as? NetworkResponseSneakers.Success)?.data ?: emptyList()
            sneakers.map { sneaker ->
                sneaker.copy(
                    isFavorite = favorites.any { it.id == sneaker.id }
                )
            }
        } else {
            emptyList()
        }
    }

    when (sneakersState) {
        is NetworkResponseSneakers.Success -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(sneakersWithFavorites, key = { it.id }) { sneaker ->
                    ProductItem(
                        sneaker = sneaker,
                        onItemClick = { navController.navigate("details/${sneaker.id}") },
                        onFavoriteClick = { id, isFavorite -> viewModel.toggleFavorite(id, isFavorite) },
                        onAddToCart = { cartViewModel.addToCart(sneaker.id) },
                        modifier = Modifier.aspectRatio(0.85f)
                    )
                }
            }
        }
        is NetworkResponseSneakers.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ошибка: ${(sneakersState as NetworkResponseSneakers.Error).errorMessage}")
            }
        }
        NetworkResponseSneakers.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}