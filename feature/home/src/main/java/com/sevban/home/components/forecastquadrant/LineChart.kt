package com.sevban.home.components.forecastquadrant

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
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
    )
) {
    val textMeasurer = rememberTextMeasurer()
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .background(graphStyle.backgroundColor)
            .height(350.dp)
            .padding(16.dp)
    ) {
        val path = Path()
        val textHeight = textMeasurer.measure("03:00").size.height
        val graphDepth = size.height - textHeight
        val oneDegree = graphDepth / (yAxisData.max() - yAxisData.min())
        val oneInterval = size.width / (yAxisData.size - 1)

        var xCursor = 0f
        var yCursor: Float

        xAxisData.forEach { value ->

            val textResult = textMeasurer.measure(
                value.toString(),
                style = TextStyle(
                    color = graphStyle.textColor,
                    fontSize = 11.sp
                )
            )
            yCursor = graphDepth - ((value - yAxisData.min()) * oneDegree)

            drawText(
                textLayoutResult = textResult,
                topLeft = Offset(x = xCursor - textResult.firstBaseline / 2, y = graphDepth + 10f)
            )

            xCursor += oneInterval
        }

        xCursor = 0f
        yCursor = graphDepth - ((yAxisData.first() - yAxisData.min()) * oneDegree)

        path.apply {
            yAxisData.forEach { value ->
                val textResult = textMeasurer.measure(
                    "$value֯ ", style = TextStyle(
                        color = graphStyle.textColor,
                        fontSize = 11.sp
                    )
                )
                val textOffsetX = -20f - textResult.firstBaseline

                yCursor = graphDepth - ((value - yAxisData.min()) * oneDegree)
                val textOffsetY = yCursor
                drawText(
                    textResult,
                    color = graphStyle.textColor,
                    topLeft = Offset(textOffsetX, textOffsetY - textResult.size.height / 2)
                )

            }
            xCursor = 0f
            yCursor = graphDepth - ((yAxisData.first() - yAxisData.min()) * oneDegree)
        }

        drawWithLayer {

            path.apply {
                yAxisData.forEach { value ->
                    yCursor = graphDepth - ((value - yAxisData.min()) * oneDegree)
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
                yCursor = graphDepth - ((yAxisData.first() - yAxisData.min()) * oneDegree)
            }

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
                blendMode = BlendMode.Clear

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