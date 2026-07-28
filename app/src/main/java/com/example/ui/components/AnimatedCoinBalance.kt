package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AnimatedCoinCounter(
    targetCoins: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    prefix: String = "",
    suffix: String = "",
    showScalePop: Boolean = true
) {
    // Smooth integer count up state
    val animatedCoins by animateIntAsState(
        targetValue = targetCoins,
        animationSpec = tween(
            durationMillis = 1200,
            easing = FastOutSlowInEasing
        ),
        label = "CoinCountUpAnimation"
    )

    // Scale pop effect when value increases
    var isPopping by remember { mutableStateOf(false) }
    var previousValue by remember { mutableIntStateOf(targetCoins) }

    LaunchedEffect(targetCoins) {
        if (targetCoins > previousValue && showScalePop) {
            isPopping = true
            kotlinx.coroutines.delay(350)
            isPopping = false
        }
        previousValue = targetCoins
    }

    val scaleValue by animateFloatAsState(
        targetValue = if (isPopping) 1.15f else 1.0f,
        animationSpec = spring(
            dampingRatio = 0.5f,
            stiffness = 300f
        ),
        label = "CoinScalePop"
    )

    val formattedNumber = remember(animatedCoins) {
        NumberFormat.getNumberInstance(Locale.US).format(animatedCoins)
    }

    Text(
        text = "$prefix$formattedNumber$suffix",
        style = style,
        color = color,
        fontWeight = fontWeight,
        modifier = modifier.scale(scaleValue)
    )
}
