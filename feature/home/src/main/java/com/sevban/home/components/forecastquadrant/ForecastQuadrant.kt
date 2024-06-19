package com.sevban.home.components.forecastquadrant

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun ForecastQuadrant(
    temperatures: List<Int>,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp)
    ) {
        val path = Path()
        val graphDepth = size.height
        val oneDegree = graphDepth / (temperatures.max() - temperatures.min())
        val oneInterval = size.width / (temperatures.size - 1)

        var xCursor = 0f
        var yCursor = graphDepth - ((temperatures.first() - temperatures.min()) * oneDegree)

        val lineStroke = 6f
        val jointRadius = 10f

        path.apply {
            temperatures.forEach { value ->

                val textResult = textMeasurer.measure(value.toString())
                val textOffsetX = -20f - textResult.firstBaseline

                yCursor = graphDepth - ((value - temperatures.min()) * oneDegree)
                val textOffsetY = yCursor - textResult.lastBaseline / 2

                drawText(textResult, topLeft = Offset(textOffsetX, textOffsetY))
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
            yCursor = graphDepth - ((temperatures.first() - temperatures.min()) * oneDegree)
        }

        val temperaturePath = path.apply {
            moveTo(0f, yCursor)
            temperatures.forEach { value ->

                yCursor = graphDepth - ((value - temperatures.min()) * oneDegree)
                lineTo(xCursor, yCursor)
                drawLine(Color.Gray, start = Offset(xCursor, 0f), end = Offset(xCursor, graphDepth))
                drawCircle(
                    color = Color.Black,
                    radius = jointRadius,
                    center = Offset(xCursor, yCursor),
                )
                moveTo(xCursor, yCursor)
                xCursor += oneInterval
            }
        }

        drawPath(
            temperaturePath,
            color = Color.Gray,
            style = Stroke(width = lineStroke),
            blendMode = BlendMode.Modulate
        )

    }
}


@Preview(showBackground = true)
@Composable
private fun ForecastQuadrantPrev() {
    ForecastQuadrant(temperatures = generateTemperatureList(20, 32, 8))
}

fun generateTemperatureList(minTemp: Int, maxTemp: Int, maxNumber: Int): List<Int> {
    require(minTemp <= maxTemp) { "Minimum temperature must be less than or equal to maximum temperature" }
    require(maxNumber > 0) { "Number of elements must be greater than zero" }

    return List(maxNumber) { Random.nextInt(minTemp, maxTemp + 1) }
}

fun main() {
    val minTemp = 20  // Minimum temperature in Celsius
    val maxTemp = 35   // Maximum temperature in Celsius
    val maxNumber = 8 // Number of temperature readings to generate

    val temperatureList = generateTemperatureList(minTemp, maxTemp, maxNumber)
    println("Generated temperature list: $temperatureList")
}