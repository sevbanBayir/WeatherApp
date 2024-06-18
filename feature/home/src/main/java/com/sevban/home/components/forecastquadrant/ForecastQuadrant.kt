package com.sevban.home.components.forecastquadrant

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
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
        modifier = modifier.size(400.dp)
    ) {
        val graphDepth = size.height
        Log.d("Graph Depth", graphDepth.toString())
        val oneDegree = graphDepth / (temperatures.max() - temperatures.min())
        Log.d("One Degree", oneDegree.toString())
        Log.d(
            "Temperatures",
            temperatures.toString() + "Max: ${temperatures.max()} Min: ${temperatures.min()}"
        )

        val oneInterval = size.width / temperatures.size

        var xCursor = 0f
        var yCursor = graphDepth - ((temperatures.first() - temperatures.min()) * oneDegree)

        val lineStroke = 6f
        val jointRadius = 10f

        val temperaturePath = Path().apply {
            moveTo(0f, yCursor)
            temperatures.forEach { value ->
                val textResult = textMeasurer.measure(value.toString())
                yCursor = graphDepth - ((value - temperatures.min()) * oneDegree)
                drawText(textResult, topLeft = Offset( -20f-textResult.firstBaseline, yCursor))
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