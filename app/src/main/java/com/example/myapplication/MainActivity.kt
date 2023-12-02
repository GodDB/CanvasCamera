package com.example.myapplication

import android.graphics.Camera
import android.graphics.Matrix
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.withMatrix
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val matrix = Matrix()
            MyApplicationTheme {
                val rotateX = remember { mutableStateOf(0f) }
                val rotateY = remember { mutableStateOf(0f) }
                val rotateZ = remember { mutableStateOf(0f) }
                val translateX = remember { mutableStateOf(0f) }
                val translateY = remember { mutableStateOf(0f) }
                val translateZ = remember { mutableStateOf(0f) }
                val density = LocalDensity.current
                val screenWidth = with(density) { LocalConfiguration.current.screenWidthDp.dp.toPx() }
                val screenHeight = with(density) { LocalConfiguration.current.screenHeightDp.dp.toPx() }

                val image = ImageBitmap.imageResource(R.drawable.bee_bg_apeach)
                val camera = Camera()
                val dp_10 = with(density) { 10.dp.toPx() }
                // A surface container using the 'background' color from the theme
                Column(modifier = Modifier.fillMaxSize()) {
                    FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            modifier = Modifier.clickable { rotateX.value = rotateX.value + 10f },
                            text = "x축 10도 회전"
                        )

                        Text(
                            modifier = Modifier.clickable { rotateY.value = rotateY.value + 10f },
                            text = "y축 10도 회전"
                        )

                        Text(
                            modifier = Modifier.clickable { rotateZ.value = rotateZ.value + 10f },
                            text = "z축 10도 회전"
                        )

                        Text(
                            modifier = Modifier.clickable { translateX.value = translateX.value + dp_10 },
                            text = "x축 10dp 이동 "
                        )

                        Text(
                            modifier = Modifier.clickable { translateY.value = translateY.value + dp_10 },
                            text = "y축 10dp 이동 "
                        )

                        Text(
                            modifier = Modifier.clickable { translateZ.value = translateZ.value + dp_10 },
                            text = "z축 10dp 이동 "
                        )

                        Text(
                            modifier = Modifier.clickable {
                                rotateX.value = 0f
                                rotateY.value = 0f
                                rotateZ.value = 0f
                                translateX.value = 0f
                                translateY.value = 0f
                                translateZ.value = 0f
                            },
                            text = "클리어"
                        )
                    }


                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 100.dp, start = 100.dp)
                    ) {
                        camera.withSave {
                            camera.translate(
                                matrix = matrix,
                                translateX = translateX.value,
                                translateY = translateY.value,
                                translateZ = translateZ.value
                            )
                            camera.rotateOfPoint(
                                matrix = matrix,
                                targetX = image.width / 2f,
                                targetY = image.height / 2f,
                                rotateX = rotateX.value,
                                rotateY = rotateY.value,
                                rotateZ = rotateZ.value
                            )
                        }

                        drawContext.canvas.nativeCanvas.withMatrix(matrix = matrix) {
                            drawImage(image)
                        }
                    }
                }

            }
        }
    }
}

inline fun Camera.withSave(block: () -> Unit) {
    save()
    block()
    restore()
}

fun Camera.rotateOfPoint(
    matrix: Matrix,
    targetX: Float = 0f,
    targetY: Float = 0f,
    rotateX: Float = 0f,
    rotateY: Float = 0f,
    rotateZ: Float = 0f
) {
    rotate(rotateX, rotateY, rotateZ)
    getMatrix(matrix)
    matrix.preTranslate(-targetX, -targetY)
    matrix.postTranslate(targetX, targetY)
}

fun Camera.translate(
    matrix: Matrix,
    translateX: Float = 0f,
    translateY: Float = 0f,
    translateZ: Float = 0f
) {
    translate(translateX, -translateY, -translateZ)
    getMatrix(matrix)
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}
