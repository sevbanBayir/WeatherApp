package com.sevban.home.components.forecastquadrant

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sevban.designsystem.theme.ComposeScaffoldProjectTheme
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun LineChart(
    yAxisData: List<Int>,
    modifier: Modifier = Modifier,
    xAxisData: List<Int> = (3..24 step 3).toList(),
    graphStyle: GraphStyle = GraphStyle(
        lineColor = MaterialTheme.colorScheme.onBackground,
        jointColor = MaterialTheme.colorScheme.primary,
        textColor = MaterialTheme.colorScheme.onBackground,
        backgroundColor = MaterialTheme.colorScheme.background,
        lineStroke = 6f,
        jointStroke = 4f,
        jointRadius = 10f
    ),
    onDayChanged: () -> Unit = {},
) {
    val textMeasurer = rememberTextMeasurer()
    var center by remember {
        mutableStateOf(Offset.Zero)
    }
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }
    var angle by remember {
        mutableFloatStateOf(0f)
    }

    var dragStartedAngle by remember {
        mutableFloatStateOf(0f)
    }

    var oldAngle by remember {
        mutableFloatStateOf(angle)
    }

    val gradient = Brush.linearGradient(
        listOf(
            Color.Red,
            Color.Yellow,
            Color.Green
        )
    )
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .background(graphStyle.backgroundColor)
            .height(400.dp)
            .border(1.dp, Color.Red)
            .pointerInput(true) {
                detectDragGestures(
                    onDragStart = { offset ->
                        dragStartedAngle = -atan2(
                            y = circleCenter.x - offset.x,
                            x = circleCenter.y - offset.y
                        ) * (180 / PI.toFloat())
                    },
                    onDragEnd = {
                        oldAngle = angle
                    }
                ) { change, _ ->
                    val touchAngle = -atan2(
                        y = circleCenter.x - change.position.x,
                        x = circleCenter.y - change.position.y
                    ) * (180 / PI.toFloat())

                    val newAngle = oldAngle + (touchAngle - dragStartedAngle)
                    angle = newAngle
                    //onWeightChange((initialWeight - angle).roundToInt())
                }
            }
    ) {
        val path = Path()
        val circleHeight = 200f
        val textHeight = textMeasurer.measure("A").size.height
        val graphDepth = circleHeight + 200.dp.toPx() - 2 * textHeight
        val oneDegree = graphDepth / (yAxisData.max() - yAxisData.min())
        val oneInterval = size.width / (yAxisData.size - 1)

        drawQuadrant()

        drawGraphXAxisSubtext(
            textData = xAxisData,
            textMeasurer = textMeasurer,
            textColor = graphStyle.textColor,
            graphDepth = graphDepth + circleHeight,
            oneInterval = oneInterval
        )

        drawGraphXAxisText(
            textData = yAxisData,
            textMeasurer = textMeasurer,
            textColor = graphStyle.textColor,
            graphDepth = graphDepth + circleHeight,
            oneInterval = oneInterval
        )

        drawTemperatureLines(
            initialY = 200f,
            path = path,
            yAxisData = yAxisData,
            graphDepth = graphDepth,
            oneDegree = oneDegree,
            oneInterval = oneInterval,
            lineColor = graphStyle.lineColor,
            lineStrokeWidth = graphStyle.lineStroke,
            jointColor = graphStyle.jointColor,
            jointStrokeWidth = graphStyle.jointStroke,
            jointRadius = graphStyle.jointRadius,
            drawOutOfTheLayer = {
                drawJoints(
                    yAxisData = yAxisData,
                    graphDepth = graphDepth,
                    oneDegree = oneDegree,
                    graphStyle = graphStyle,
                    oneInterval = oneInterval
                )
            }
        )
    }
}

private fun DrawScope.drawTemperatureLines(
    initialY: Float = 200f,
    path: Path,
    yAxisData: List<Int>,
    graphDepth: Float,
    oneDegree: Float,
    oneInterval: Float,
    lineColor: Color,
    lineStrokeWidth: Float,
    jointColor: Color,
    jointStrokeWidth: Float,
    jointRadius: Float,
    drawOutOfTheLayer: DrawScope.() -> Unit
) {
    var yCursor1: Float = initialY
    var xCursor1 = 0f
    drawWithLayer {

        path.apply {
            moveTo(0f, yCursor1)
            yAxisData.forEach { value ->

                yCursor1 = initialY + graphDepth - ((value - yAxisData.min()) * oneDegree)
                lineTo(xCursor1, yCursor1)
                drawLine(
                    Color.Gray,
                    start = Offset(xCursor1, initialY),
                    end = Offset(xCursor1, graphDepth + initialY)
                )
                moveTo(xCursor1, yCursor1)
                xCursor1 += oneInterval
            }
        }

        drawPath(
            path,
            color = lineColor.copy(alpha = 0.5f),
            style = Stroke(width = lineStrokeWidth),
        )

        yAxisData.forEach { value ->
            yCursor1 = initialY + graphDepth - ((value - yAxisData.min()) * oneDegree)
            drawCircle(
                color = jointColor,
                radius = jointRadius,
                center = Offset(xCursor1, yCursor1),
                blendMode = BlendMode.Clear
            )
            xCursor1 += oneInterval
        }
    }

    drawOutOfTheLayer()
}

private fun DrawScope.drawJoints(
    initialY: Float = 200f,
    yAxisData: List<Int>,
    graphDepth: Float,
    oneDegree: Float,
    graphStyle: GraphStyle,
    oneInterval: Float
) {
    var yCursor1: Float
    var xCursor1 = 0f
    yAxisData.forEach { value ->
        yCursor1 = initialY + graphDepth - ((value - yAxisData.min()) * oneDegree)
        drawCircle(
            color = graphStyle.jointColor,
            radius = graphStyle.jointRadius,
            center = Offset(xCursor1, yCursor1),
            style = Stroke(graphStyle.jointStroke),
        )
        xCursor1 += oneInterval
    }

}

private fun DrawScope.drawGraphXAxisText(
    textData: List<Int>,
    textMeasurer: TextMeasurer,
    textColor: Color,
    graphDepth: Float,
    oneInterval: Float
) {
    var xCursor1 = 0f
    textData.forEach { value ->

        val textResult = textMeasurer.measure(
            "$value ֯",
            style = TextStyle(
                color = textColor,
                fontWeight = FontWeight.Bold
            )
        )

        drawText(
            textLayoutResult = textResult,
            topLeft = Offset(x = xCursor1 - textResult.firstBaseline / 2, y = graphDepth + 10f)
        )

        xCursor1 += oneInterval
    }
}

private fun DrawScope.drawGraphXAxisSubtext(
    textData: List<Int>,
    textMeasurer: TextMeasurer,
    textColor: Color,
    graphDepth: Float,
    oneInterval: Float
) {
    var xCursor1 = 0f
    textData.forEach { value ->

        val textResult = textMeasurer.measure(
            "$value:00",
            style = TextStyle(
                color = textColor,
                fontSize = 11.sp
            )
        )

        drawText(
            textLayoutResult = textResult,
            topLeft = Offset(x = xCursor1 - textResult.firstBaseline / 2, y = graphDepth + 50f)
        )

        xCursor1 += oneInterval
    }
}

fun DrawScope.drawQuadrant(
    color: Color = Color.Red,
    gradient: Brush = Brush.linearGradient(
        listOf(
            Color.Red,
            Color.Yellow,
            Color.Green
        )
    ),
    handleColor: Color = Color.Magenta
) {
    drawWithLayer {
        drawCircle(
            brush = gradient,
            radius = 1000f,
            style = Stroke(
                width = 55f,
            ),
            center = Offset(x = size.width / 2, y = 1100f),
        )

        drawRoundRect(
            color = handleColor,
            topLeft = Offset(x = size.width / 2 - 45f, y = -170f),
            size = Size(100f, 50f),
            cornerRadius = CornerRadius(50f, 50f),
            blendMode = BlendMode.Clear
        )
    }

    drawRoundRect(
        color = handleColor,
        topLeft = Offset(x = size.width / 2 - 45f, y = -170f),
        size = Size(100f, 50f),
        cornerRadius = CornerRadius(50f, 50f),
        style = Stroke(
            width = 5f,
        ),
    )
}


@PreviewLightDark
@Preview(showBackground = true)
@Composable
private fun ForecastQuadrantPrev() {
    ComposeScaffoldProjectTheme {
        LineChart(yAxisData = generateTemperatureList(20, 30, 10))
    }
}

fun generateTemperatureList(minTemp: Int, maxTemp: Int, maxNumber: Int): List<Int> {
    require(minTemp <= maxTemp) { "Minimum temperature must be less than or equal to maximum temperature" }
    require(maxNumber > 0) { "Number of elements must be greater than zero" }

    return List(maxNumber) { Random.nextInt(minTemp, maxTemp + 1) }
}