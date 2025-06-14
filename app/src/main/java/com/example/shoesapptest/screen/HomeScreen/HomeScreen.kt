package com.example.pypypy.ui.screen.home

import ProductItem
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.shoesapp.ui.theme.MatuleTheme
import com.example.shoesapptest.R
import com.example.shoesapptest.Screen
import com.example.shoesapptest.data.remote.network.NetworkResponseSneakers
import com.example.shoesapptest.data.remote.network.PopularResponse
import com.example.shoesapptest.data.remote.network.SneakersResponse
import com.example.shoesapptest.screen.CartScreen.CartViewModel
import com.example.shoesapptest.screen.ForSneakers.SneakersViewModel
import com.example.shoesapptest.screen.HomeScreen.PopularViewModel
import com.example.shoesapptest.screen.HomeScreen.component.BottomBar
import com.example.shoesapptest.screen.HomeScreen.component.TopPanel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreenHast(
    navController: NavHostController,
    viewModel: PopularViewModel = koinViewModel(),
    cartViewModel: CartViewModel = koinViewModel()
) {
    Scaffold(
        topBar = {
            TopPanel(
                title = "Главная",
                leftImage = painterResource(R.drawable.menu),
                rightImage = painterResource(R.drawable.black_basket),
                textSize = 32
            )
        },
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        HomeScreenContent(
            paddingValues = paddingValues,
            viewModel = viewModel,
            navController = navController,
            cartViewModel = cartViewModel
        )
    }
}

@Composable
fun HomeScreenContent(
    paddingValues: PaddingValues,
    viewModel: PopularViewModel,
    navController: NavHostController,
    cartViewModel: CartViewModel
) {
    val sneakersState by viewModel.sneakersState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchSneakers()
    }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 22.dp, vertical = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable { navController.navigate(Screen.SearchScreen.route) }
            ) {
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Поиск") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.lupa),
                            contentDescription = "Поиск"
                        )
                    },
                    enabled = false,
                    readOnly = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        disabledContainerColor = MatuleTheme.colors.background,
                        disabledTextColor = Color.Gray,
                        disabledLeadingIconColor = Color.Gray,
                        disabledPlaceholderColor = Color.LightGray,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { /* TODO: сортировка */ }) {
                Image(
                    painter = painterResource(R.drawable.sort),
                    contentDescription = "Сортировка",
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "Категории",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyRow {
                items(listOf("Всё", "Outdoor", "Tennis", "Basketball", "Running")) { category ->
                    Button(
                        onClick = {
                            when (category) {
                                "Outdoor" -> navController.navigate(Screen.Outdoor.route)
                            }
                        },
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .height(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(text = category, fontSize = 12.sp)
                    }
                }
            }
        }

        when (sneakersState) {
            is NetworkResponseSneakers.Success -> {
                val sneakers = (sneakersState as NetworkResponseSneakers.Success<List<PopularResponse>>).data
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Популярное",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Text(
                            text = "Все",
                            modifier = Modifier.clickable {
                                navController.navigate(Screen.Popular.route)
                            },
                            fontSize = 12.sp,
                            color = MatuleTheme.colors.accent
                        )
                    }

                    LazyRow(
                        modifier = Modifier.padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(sneakers) { sneaker ->
                            ProductItem(
                                sneaker = sneaker,
                                onItemClick = {
                                    navController.navigate("details/${sneaker.id}")
                                },
                                onFavoriteClick = { id, isFavorite ->
                                    viewModel.toggleFavorite(id, isFavorite)
                                },
                                onAddToCart = { cartViewModel.addToCart(sneaker.id) },
                                modifier = Modifier.width(160.dp)
                            )
                        }
                    }
                }
            }

            is NetworkResponseSneakers.Error -> {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)) {
                    Text(
                        text = "Ошибка загрузки",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            NetworkResponseSneakers.Loading -> {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Акции",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Все",
                    fontSize = 12.sp,
                    color = MatuleTheme.colors.accent
                )
            }

            Box(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.activesale),
                    contentDescription = "Акция",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                )
            }
        }
    }
}