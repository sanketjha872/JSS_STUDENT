package com.jhainusa.jss_student.onboarding

import android.view.WindowInsets
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhainusa.jss_student.UserPref.UserPreferences
import com.jhainusa.jss_student.ui.theme.AccentBlue
import com.jhainusa.jss_student.ui.theme.NavyText
import com.jhainusa.jss_student.ui.theme.PlusJakartaSans
import com.jhainusa.jss_student.ui.theme.SubtitleGray
import com.jhainusa.jss_student.ui.theme.TickGray
import kotlinx.coroutines.launch
import kotlin.math.*

// ---------- Colors ----------


// ---------- Font ----------

// ---------- Ruler geometry ----------

// ---------- Ruler geometry (matches the reference design's proportions) ----------
private val RULER_HEIGHT = 260.dp     // total height of the ruler component
private val RULER_TOP_INSET = 28.dp   // gap above the tallest tick
private val TICK_SHORT = 38.dp        // 0.2kg fine ticks
private val TICK_MEDIUM = 60.dp       // 1kg ticks
private val TICK_TALL = 90.dp         // 5kg labeled ticks
private val LABEL_GAP = 20.dp         // gap between tallest tick and its number
private const val RUBBER_BAND_FACTOR = 0.3f  // resistance when dragging past min/max


@Composable
fun DesiredAttendanceScreen(onFinish: () -> Unit) {
    val minWeight = 0f
    val maxWeight = 100f
    var desiredAttendance by remember { mutableFloatStateOf(75f) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize()
        .navigationBarsPadding()
        , color = Color.White,
        ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(64.dp))

            Text(
                text = "Your Attendance Goal",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = PlusJakartaSans,
                color = NavyText
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Adjust the slider below",
                fontSize = 17.sp,
                fontFamily = PlusJakartaSans,
                color = SubtitleGray
            )


            Spacer(Modifier.height(64.dp))

            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(start = 24.dp) // Offset for the kg label to keep number centered
            ) {
                Text(
                    text = "%.1f".format(desiredAttendance),
                    fontSize = 96.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PlusJakartaSans,
                    color = NavyText,
                    letterSpacing = (-4).sp
                )
                Text(
                    text = "%",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PlusJakartaSans,
                    color = NavyText,
                    modifier = Modifier.padding(bottom = 20.dp, start = 4.dp)
                )
            }

            Spacer(Modifier.height(40.dp))

            SlidingWeightRuler(
                value = desiredAttendance,
                onValueChange = { desiredAttendance = it },
                minValue = minWeight,
                maxValue = maxWeight,
                majorStep = 3f,
                unitWidth = 45.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(RULER_HEIGHT)
            )

            Spacer(Modifier.height(56.dp))

            Text(
                text = "My college want %.1f".format(desiredAttendance) + "%\nattendance from me",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontFamily = PlusJakartaSans,
                color = NavyText
            )

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                        scope.launch {
                            UserPreferences.completeDesiredAttendance(context, desiredAttendance)
                            onFinish()
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E2E33),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Finish",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = PlusJakartaSans
                )
            }
        }
    }
}


@Composable
private fun SlidingWeightRuler(
    value: Float,
    onValueChange: (Float) -> Unit,
    minValue: Float,
    maxValue: Float,
    modifier: Modifier = Modifier,
    tickStep: Float = 0.2f,
    mediumStep: Float = 1f,
    majorStep: Float = 5f,
    unitWidth: Dp = 40.dp,
    pointerFraction: Float = 0.5f
) {
    val density = LocalDensity.current
    val unitWidthPx = with(density) { unitWidth.toPx() }
    val textMeasurer = rememberTextMeasurer()
    val scope = rememberCoroutineScope()
    val decay = rememberSplineBasedDecay<Float>()
    val haptics = LocalHapticFeedback.current

    val topInsetPx = with(density) { RULER_TOP_INSET.toPx() }
    val shortPx = with(density) { TICK_SHORT.toPx() }
    val mediumPx = with(density) { TICK_MEDIUM.toPx() }
    val tallPx = with(density) { TICK_TALL.toPx() }
    val labelGapPx = with(density) { LABEL_GAP.toPx() }

    val animatedValue = remember { Animatable(value) }
    var isUserInteracting by remember { mutableStateOf(false) }
    var lastHapticTickIndex by remember { mutableIntStateOf(Int.MIN_VALUE) }

    LaunchedEffect(animatedValue) {
        snapshotFlow { animatedValue.value }.collect { onValueChange(it) }
    }

    LaunchedEffect(value) {
        if (!isUserInteracting && !animatedValue.isRunning && abs(animatedValue.value - value) > 0.01f) {
            animatedValue.animateTo(value, animationSpec = tween(220))
        }
    }

    fun rubberBand(raw: Float): Float = when {
        raw < minValue -> minValue - (minValue - raw) * RUBBER_BAND_FACTOR
        raw > maxValue -> maxValue + (raw - maxValue) * RUBBER_BAND_FACTOR
        else -> raw
    }

    val labelLayouts = remember(textMeasurer, minValue, maxValue, majorStep) {
        val style = TextStyle(
            fontSize = 17.sp,
            color = NavyText,
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Bold
        )
        val map = HashMap<Int, TextLayoutResult>()
        var v = (floor(minValue / majorStep) * majorStep)
        while (v <= maxValue + 0.001f) {
            if (v >= minValue) {
                val labelInt = v.roundToInt()
                map[labelInt] = textMeasurer.measure(text = labelInt.toString(), style = style)
            }
            v += majorStep
        }
        map
    }

    val draggableState = rememberDraggableState { delta ->
        val deltaValue = -delta / unitWidthPx
        val target = rubberBand(animatedValue.value + deltaValue)
        scope.launch { animatedValue.snapTo(target) }

        val tickIndex = floor((target.coerceIn(minValue, maxValue) / tickStep).toDouble()).toInt()
        if (tickIndex != lastHapticTickIndex) {
            lastHapticTickIndex = tickIndex
            haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }

    Box(
        modifier = modifier.draggable(
            orientation = Orientation.Horizontal,
            state = draggableState,
            onDragStarted = { isUserInteracting = true },
            onDragStopped = { velocity ->
                scope.launch {
                    val startedOutOfBounds = animatedValue.value < minValue || animatedValue.value > maxValue
                    if (startedOutOfBounds) {
                        animatedValue.animateTo(
                            targetValue = animatedValue.value.coerceIn(minValue, maxValue),
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    } else {
                        val flingVelocity = -velocity / unitWidthPx
                        val predictedTarget = decay.calculateTargetValue(animatedValue.value, flingVelocity)
                        if (predictedTarget < minValue || predictedTarget > maxValue) {
                            animatedValue.animateTo(
                                targetValue = predictedTarget.coerceIn(minValue, maxValue),
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                        } else {
                            animatedValue.animateDecay(flingVelocity, decay)
                        }
                    }

                    val settled = animatedValue.value.coerceIn(minValue, maxValue)
                    val snapped = (Math.round(settled / tickStep) * tickStep).coerceIn(minValue, maxValue)
                    animatedValue.animateTo(
                        targetValue = snapped,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                    isUserInteracting = false
                }
            }
        )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val currentValue = animatedValue.value
            val pointerX = size.width * pointerFraction
            val ticksTopY = topInsetPx

            drawRect(
                color = AccentBlue.copy(alpha = 0.15f),
                topLeft = Offset(0f, ticksTopY),
                size = Size(pointerX, tallPx)
            )

            val minVisibleValue = currentValue - pointerX / unitWidthPx
            val maxVisibleValue = currentValue + (size.width - pointerX) / unitWidthPx
            val startIndex = floor((minVisibleValue / tickStep).toDouble()).toInt()
            val endIndex = ceil((maxVisibleValue / tickStep).toDouble()).toInt()

            for (i in startIndex..endIndex) {
                val tickValue = i * tickStep
                if (tickValue < minValue || tickValue > maxValue) continue

                val x = pointerX + (tickValue - currentValue) * unitWidthPx
                val isMajor = abs((tickValue / majorStep).roundToInt() * majorStep - tickValue) < 0.001f
                val isMedium = !isMajor && abs((tickValue / mediumStep).roundToInt() * mediumStep - tickValue) < 0.001f
                val isFilled = x <= pointerX

                val tickLengthPx = when {
                    isMajor -> tallPx
                    isMedium -> mediumPx
                    else -> shortPx
                }
                val strokeWidthPx = when {
                    isMajor -> 6f
                    isMedium -> 4f
                    else -> 2.5f
                }

                drawLine(
                    color = if (isFilled) AccentBlue else TickGray,
                    start = Offset(x, ticksTopY),
                    end = Offset(x, ticksTopY + tickLengthPx),
                    strokeWidth = strokeWidthPx,
                    cap = StrokeCap.Round
                )

                if (isMajor) {
                    val layout = labelLayouts[tickValue.roundToInt()]
                    if (layout != null) {
                        drawText(
                            textLayoutResult = layout,
                            topLeft = Offset(
                                x - layout.size.width / 2f,
                                ticksTopY + tallPx + labelGapPx
                            )
                        )
                    }
                }
            }

            drawLine(
                color = AccentBlue,
                start = Offset(pointerX, ticksTopY - 15f),
                end = Offset(pointerX, ticksTopY + tallPx + 15f),
                strokeWidth = 6f,
                cap = StrokeCap.Round
            )
        }
    }
}
