package com.sevban.home.components.forecastquadrant

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun ForecastQuadrant(
    temperatures: List<Int>,
    modifier: Modifier = Modifier
) {

    Canvas(modifier = Modifier
        .size(400.dp)
        .border(1.dp, Color.Red)) {
        val graphDepth = size.height
        val oneDegree = graphDepth / 6.dp.toPx()
        val oneInterval = size.width / 8f

        var xCursor = oneInterval
        var yCursor = graphDepth - (temperatures.first() * oneDegree)

        val lineStroke = 6f
        val jointRadius = 10f
        val jointStroke = 6f

        val temperaturePath = Path().apply {
            moveTo(0f, yCursor)
            temperatures.drop(1).forEach {
                yCursor = graphDepth - (it * oneDegree)
                lineTo(xCursor, yCursor)
                drawLine(Color.Gray, start = Offset(xCursor, 0f), end = Offset(xCursor, graphDepth))
                drawCircle(
                    color = Color.Black,
                    radius = jointRadius,
                    center = Offset(xCursor, yCursor),
                    style = Stroke(jointStroke),
                )
                moveTo(xCursor, yCursor)
                xCursor += oneInterval
            }
        }

        repeat(temperatures.size) {

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
    ForecastQuadrant(temperatures = createRandomDailyTemps())
}

fun createRandomDailyTemps(): List<Int> {
    val mutableList = mutableListOf<Int>()
    for (i in 1..9) {
        if (Random.nextBoolean())
            mutableList.add(i + 1)
        else
            mutableList.add(i - 2)
    }
    println(mutableList)
    return mutableList
}

val randomList = listOf(
    10,
    20,
    30,
    40,
    50,
    60,
    70,
    80,
    /*    15,
        10,
        19,
        17,
        11,
        25,
        -7,
        3,
        49,
        54,
        10,
        11,
        -8,
        40,
        21,
        43,
        -11,
        59,
        -5,
        -10,
        33,
        57,
        53,
        34,
        -1,
        -5,
        18,
        54,
        -14,
        7,
        -5,
        26*/
)