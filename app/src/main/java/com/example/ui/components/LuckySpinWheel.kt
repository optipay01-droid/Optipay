package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SpinSegment
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RoyalIndigo
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LuckySpinWheel(
    segments: List<SpinSegment>,
    targetAngle: Float,
    isSpinning: Boolean,
    onSpinClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedAngle by animateFloatAsState(
        targetValue = targetAngle,
        animationSpec = tween(
            durationMillis = 3200,
            easing = FastOutSlowInEasing
        ),
        label = "spinWheelAngle"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(320.dp)
                .padding(8.dp)
        ) {
            // Outer Glowing Ring Canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.minDimension / 2

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(com.example.ui.theme.SleekRoseContainer, com.example.ui.theme.SleekPrimary, com.example.ui.theme.SleekBorder),
                        center = center,
                        radius = radius
                    ),
                    radius = radius
                )
                drawCircle(
                    color = com.example.ui.theme.SleekPrimary,
                    radius = radius - 6.dp.toPx(),
                    style = Stroke(width = 8.dp.toPx())
                )
            }

            // Rotating Wheel Canvas
            Canvas(
                modifier = Modifier
                    .size(290.dp)
                    .clip(CircleShape)
                    .rotate(animatedAngle)
                    .testTag("spin_wheel_canvas")
            ) {
                val wheelCenter = Offset(size.width / 2, size.height / 2)
                val wheelRadius = size.minDimension / 2
                val sweepAngle = 360f / segments.size

                segments.forEachIndexed { index, segment ->
                    val startAngle = index * sweepAngle - 90f

                    // Segment Arc
                    drawArc(
                        color = segment.color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true,
                        size = Size(wheelRadius * 2, wheelRadius * 2),
                        topLeft = Offset(0f, 0f)
                    )

                    // Border Divider
                    val lineAngleRad = (startAngle * PI / 180f).toFloat()
                    val lineEnd = Offset(
                        wheelCenter.x + wheelRadius * cos(lineAngleRad),
                        wheelCenter.y + wheelRadius * sin(lineAngleRad)
                    )
                    drawLine(
                        color = Color.White.copy(alpha = 0.5f),
                        start = wheelCenter,
                        end = lineEnd,
                        strokeWidth = 2.dp.toPx()
                    )

                    // Text Draw
                    val midAngleRad = ((startAngle + sweepAngle / 2f) * PI / 180f).toFloat()
                    val textRadius = wheelRadius * 0.65f
                    val textX = wheelCenter.x + textRadius * cos(midAngleRad)
                    val textY = wheelCenter.y + textRadius * sin(midAngleRad)

                    val paint = android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 34f
                        isFakeBoldText = true
                        textAlign = android.graphics.Paint.Align.CENTER
                        setShadowLayer(4f, 0f, 2f, android.graphics.Color.BLACK)
                    }

                    drawContext.canvas.nativeCanvas.drawText(
                        if (segment.isJackpot) "150⭐" else "${segment.coins}",
                        textX,
                        textY + 12f,
                        paint
                    )
                }
            }

            // Top Pointer Needle
            Canvas(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.TopCenter)
                    .padding(top = 4.dp)
            ) {
                val path = Path().apply {
                    moveTo(size.width / 2, size.height)
                    lineTo(0f, 0f)
                    lineTo(size.width, 0f)
                    close()
                }
                drawPath(path, color = com.example.ui.theme.SleekDarkTerracotta)
                drawPath(path, color = Color.White, style = Stroke(width = 2.dp.toPx()))
            }

            // Central Spin Button Surface
            Surface(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(4.dp, com.example.ui.theme.SleekDarkTerracotta, CircleShape),
                color = com.example.ui.theme.SleekPrimary,
                tonalElevation = 8.dp
            ) {
                Button(
                    onClick = onSpinClick,
                    enabled = !isSpinning,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = com.example.ui.theme.SleekPrimary,
                        disabledContainerColor = com.example.ui.theme.SleekRoseContainer
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("spin_now_button")
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Spin",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = if (isSpinning) "SPINNING" else "SPIN",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.SleekCardBg),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.SleekBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "🎁 Guaranteed prize on every spin! Tap SPIN to win up to 150 Jackpot Coins.",
                color = com.example.ui.theme.SleekPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(14.dp)
            )
        }
    }
}
