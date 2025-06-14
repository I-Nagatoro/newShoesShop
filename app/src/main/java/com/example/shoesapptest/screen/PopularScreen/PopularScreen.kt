package com.example.shoesapptest.screen.PopularScreen

import ProductItem
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.shoesapptest.R
import com.example.shoesapptest.data.remote.network.NetworkResponseSneakers
import com.example.shoesapptest.data.remote.network.PopularResponse
import com.example.shoesapptest.data.remote.network.SneakersResponse
import com.example.shoesapptest.screen.ForSneakers.SneakersViewModel
import com.example.shoesapptest.screen.HomeScreen.PopularViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PopularScreen(
    navController: NavController,
    viewModel: PopularViewModel = koinViewModel()
) {
    val sneakersState by viewModel.sneakersState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchSneakers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopBar(
            onBack = { navController.popBackStack() },
            onFavoriteClick = { navController.navigate("favorite") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Популярное",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (sneakersState) {
            is NetworkResponseSneakers.Success -> {
                val sneakers = (sneakersState as NetworkResponseSneakers.Success).data
                PopularContent(
                    sneakers = (sneakersState as NetworkResponseSneakers.Success).data,
                    onFavoriteClick = viewModel::toggleFavorite,
                    onAddToCart = { },
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            }

            is NetworkResponseSneakers.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is NetworkResponseSneakers.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Ошибка загрузки", color = Color.Red)
                }
            }
        }
    }
}

@Composable
fun TopBar(
    onBack: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .size(36.dp)
                .background(Color.White, shape = CircleShape)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = "Назад",
                tint = Color.Black,
                modifier = Modifier.padding(6.dp)
            )
        }

        IconButton(
            onClick = onFavoriteClick,
            modifier = Modifier
                .size(36.dp)
                .background(Color.White, shape = CircleShape)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.heart),
                contentDescription = "Избранное",
                tint = Color.Black,
                modifier = Modifier.padding(6.dp)
            )
        }
    }
}

@Composable
fun PopularContent(
    sneakers: List<PopularResponse>,
    onFavoriteClick: (Int, Boolean) -> Unit,
    onAddToCart: (PopularResponse) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(sneakers, key = { it.id }) { sneaker ->
            ProductItem(
                sneaker = sneaker,
                onItemClick = { navController.navigate("details/${sneaker.id}") },
                onFavoriteClick = onFavoriteClick,
                onAddToCart = { onAddToCart(sneaker) },
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.85f)
            )
        }
    }
}
