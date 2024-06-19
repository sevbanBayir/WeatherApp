package com.sevban.home.components.forecastquadrant

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sevban.designsystem.theme.ComposeScaffoldProjectTheme
import kotlin.random.Random

@Composable
fun LineChart(
    data: List<Int>,
    modifier: Modifier = Modifier,
    graphStyle: GraphStyle = GraphStyle(
        lineColor = MaterialTheme.colorScheme.onBackground,
        jointColor = MaterialTheme.colorScheme.primary,
        textColor = MaterialTheme.colorScheme.onBackground,
        backgroundColor = MaterialTheme.colorScheme.background,
        lineStroke = 6f,
        jointRadius = 10f
    )
) {
    val textMeasurer = rememberTextMeasurer()
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .background(graphStyle.backgroundColor)
            .height(350.dp)
    ) {
        val path = Path()
        val graphDepth = size.height
        val oneDegree = graphDepth / (data.max() - data.min())
        val oneInterval = size.width / (data.size - 1)

        var xCursor = 0f
        var yCursor : Float

        path.apply {
            data.forEach { value ->

                val textResult = textMeasurer.measure(value.toString())
                val textOffsetX = -20f - textResult.firstBaseline

                yCursor = graphDepth - ((value - data.min()) * oneDegree)
                val textOffsetY = yCursor - textResult.lastBaseline / 2

                drawText(textResult, color = graphStyle.textColor, topLeft = Offset(textOffsetX, textOffsetY))
                drawLine(
                    Color.Gray,
                    start = Offset(0f, yCursor),
                    end = Offset(xCursor, yCursor),
                    strokeWidth = 3f,
                    pathEffect = PathEffect.dashPathEffect(
                        intervals = floatArrayOf(
                            10f,
                            5.dp.toPx()
                        )
                    )
                )
                moveTo(xCursor, yCursor)
                xCursor += oneInterval
            }
            xCursor = 0f
            yCursor = graphDepth - ((data.first() - data.min()) * oneDegree)
        }

        path.apply {
            moveTo(0f, yCursor)
            data.forEach { value ->

                yCursor = graphDepth - ((value - data.min()) * oneDegree)
                lineTo(xCursor, yCursor)
                drawLine(Color.Gray, start = Offset(xCursor, 0f), end = Offset(xCursor, graphDepth))
                moveTo(xCursor, yCursor)
                xCursor += oneInterval
            }
            xCursor = 0f
            yCursor = graphDepth - ((data.first() - data.min()) * oneDegree)
        }

        val circlePath = Path().apply {
            moveTo(0f, yCursor)
            data.forEach { value ->

                yCursor = graphDepth - ((value - data.min()) * oneDegree)
                drawCircle(
                    color = graphStyle.jointColor,
                    radius = graphStyle.jointRadius,
                    center = Offset(xCursor, yCursor),
                )
                moveTo(xCursor, yCursor)
                xCursor += oneInterval
            }
        }

        drawPath(
            path,
            color = graphStyle.lineColor.copy(alpha = 0.5f),
            style = Stroke(width = graphStyle.lineStroke),
        )

        drawPath(
            circlePath,
            color = graphStyle.jointColor,
        )
    }
}

@PreviewLightDark
@Preview(showBackground = true)
@Composable
private fun ForecastQuadrantPrev() {
    ComposeScaffoldProjectTheme {
        LineChart(data = generateTemperatureList(20, 30, 10))
    }
}

fun generateTemperatureList(minTemp: Int, maxTemp: Int, maxNumber: Int): List<Int> {
    require(minTemp <= maxTemp) { "Minimum temperature must be less than or equal to maximum temperature" }
    require(maxNumber > 0) { "Number of elements must be greater than zero" }

    return List(maxNumber) { Random.nextInt(minTemp, maxTemp + 1) }
}