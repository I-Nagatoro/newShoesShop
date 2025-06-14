package com.example.shoesapptest.screen.SlideScreen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoesapptest.R

@Composable
fun TitleScreen(onNavigateToSliderScreen: () -> Unit) {
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000)
        onNavigateToSliderScreen()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.horizontalGradient(listOf(Color(0xFF48B2E7), Color(0xFF0076B1)))))
            {
                Text(
                    text = "MATULE",
                    fontSize = 32.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
}

@Composable
fun SlideScreen(onNavigateToAuthScreen: () -> Unit) {
    var sliderValue by remember { mutableStateOf(0) }
    val slides = listOf(
        SlideData(
            imageRes = R.drawable.sneakers1,
            title = "ДОБРО\nПОЖАЛОВАТЬ",
            subtitle = ""
        ),
        SlideData(
            imageRes = R.drawable.sneakers2,
            title = "Начнем\nпутешествие",
            subtitle = "Умная, великолепная и модная\nколлекция Изучите сейчас"
        ),
        SlideData(
            imageRes = R.drawable.sneakers3,
            title = "У вас есть сила,\nчтобы",
            subtitle = "В вашей комнате много красивых и\nпривлекательных растений"
        )
    )

    val maxIndex = slides.size - 1
    val currentSlide = slides[sliderValue]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.horizontalGradient(listOf(Color(0xFF48B2E7), Color(0xFF0076B1)))),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = currentSlide.title,
            color = Color.White,
            fontStyle = FontStyle.Italic,
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 42.sp,
            overflow = TextOverflow.Clip,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Image(
            painter = painterResource(id = currentSlide.imageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .padding(vertical = 20.dp)
        )

        if (currentSlide.subtitle.isNotEmpty()) {
            Text(
                text = currentSlide.subtitle,
                color = Color.White,
                fontStyle = FontStyle.Italic,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                overflow = TextOverflow.Clip,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        SlideIndicator(currentIndex = sliderValue, maxIndex = maxIndex) {
            sliderValue = it
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (sliderValue < maxIndex) sliderValue++
                else onNavigateToAuthScreen()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 40.dp)
        ) {
            Text(text = if (sliderValue < maxIndex) "Далее" else "Начать")
        }
    }
}

@Composable
fun SlideIndicator(currentIndex: Int, maxIndex: Int, onClick: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 0..maxIndex) {
            val animatedWidth by animateDpAsState(
                targetValue = if (i == currentIndex) 40.dp else 20.dp,
                label = "widthAnim"
            )
            val animatedColor by animateColorAsState(
                targetValue = if (i == currentIndex) Color.White else Color.LightGray,
                label = "colorAnim"
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .height(6.dp)
                    .width(animatedWidth)
                    .background(
                        color = animatedColor,
                        shape = RoundedCornerShape(50)
                    )
                    .clickable { onClick(i) }
            )
        }
    }
}

data class SlideData(
    val imageRes: Int,
    val title: String,
    val subtitle: String
)