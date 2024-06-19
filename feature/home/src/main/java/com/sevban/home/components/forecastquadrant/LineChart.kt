package com.sevban.home.components.forecastquadrant

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sevban.designsystem.theme.ComposeScaffoldProjectTheme
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
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .background(graphStyle.backgroundColor)
            .height(200.dp)
        //.border(1.dp, Color.Red)
    ) {
        val path = Path()
        val textHeight = textMeasurer.measure("03:00").size.height
        val graphDepth = size.height - 2 * textHeight
        val oneDegree = graphDepth / (yAxisData.max() - yAxisData.min())
        val oneInterval = size.width / (yAxisData.size - 1)

        var xCursor = 0f
        var yCursor: Float


        drawWithLayer {
            drawCircle(
                color = graphStyle.lineColor,
                radius = 1250f,
                style = Stroke(
                    width = 5f,
                ),
                center = Offset(x = size.width / 2, y = 1100f),
            )

            drawRoundRect(
                color = graphStyle.lineColor,
                topLeft = Offset(x = size.width / 2 - 45f, y = -170f),
                size = Size(100f, 50f),
                cornerRadius = CornerRadius(50f, 50f),
                blendMode = BlendMode.Clear
            )
        }

        drawRoundRect(
            color = graphStyle.lineColor,
            topLeft = Offset(x = size.width / 2 - 45f, y = -170f),
            size = Size(100f, 50f),
            cornerRadius = CornerRadius(50f, 50f),
            style = Stroke(
                width = 5f,
            ),
        )

        xAxisData.forEach { value ->

            val textResult = textMeasurer.measure(
                "$value:00",
                style = TextStyle(
                    color = graphStyle.textColor,
                    fontSize = 11.sp
                )
            )

            drawText(
                textLayoutResult = textResult,
                topLeft = Offset(x = xCursor - textResult.firstBaseline / 2, y = graphDepth + 50f)
            )

            xCursor += oneInterval
        }

        xCursor = 0f
        yCursor = graphDepth - ((yAxisData.first() - yAxisData.min()) * oneDegree)


        yAxisData.forEach { value ->

            val textResult = textMeasurer.measure(
                "$value ֯",
                style = TextStyle(
                    color = graphStyle.textColor,
                    fontWeight = FontWeight.Bold
                )
            )

            drawText(
                textLayoutResult = textResult,
                topLeft = Offset(x = xCursor - textResult.firstBaseline / 2, y = graphDepth + 10f)
            )

            xCursor += oneInterval
        }

        xCursor = 0f
        yCursor = graphDepth - ((yAxisData.first() - yAxisData.min()) * oneDegree)


        drawWithLayer {

            path.apply {
                moveTo(0f, yCursor)
                yAxisData.forEach { value ->

                    yCursor = graphDepth - ((value - yAxisData.min()) * oneDegree)
                    lineTo(xCursor, yCursor)
                    drawLine(
                        Color.Gray,
                        start = Offset(xCursor, 0f),
                        end = Offset(xCursor, graphDepth)
                    )
                    moveTo(xCursor, yCursor)
                    xCursor += oneInterval
                }
                xCursor = 0f
                yCursor = graphDepth - ((yAxisData.first() - yAxisData.min()) * oneDegree)
            }

            drawPath(
                path,
                color = graphStyle.lineColor.copy(alpha = 0.5f),
                style = Stroke(width = graphStyle.lineStroke),
            )

            yAxisData.forEach { value ->
                yCursor = graphDepth - ((value - yAxisData.min()) * oneDegree)
                drawCircle(
                    color = graphStyle.jointColor,
                    radius = graphStyle.jointRadius,
                    center = Offset(xCursor, yCursor),
                    blendMode = BlendMode.Clear
                )
                xCursor += oneInterval
            }
            xCursor = 0f
            yCursor = graphDepth - ((yAxisData.first() - yAxisData.min()) * oneDegree)
        }

        yAxisData.forEach { value ->
            yCursor = graphDepth - ((value - yAxisData.min()) * oneDegree)
            drawCircle(
                color = graphStyle.jointColor,
                radius = graphStyle.jointRadius,
                center = Offset(xCursor, yCursor),
                style = Stroke(graphStyle.jointStroke),
            )
            xCursor += oneInterval
        }
    }
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