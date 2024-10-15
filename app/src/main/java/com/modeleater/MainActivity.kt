package com.modeleater

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.modeleater.ml.Cartoonizer
import com.modeleater.ui.theme.ModelEaterTheme
import org.tensorflow.lite.support.image.TensorImage


class MainActivity : ComponentActivity() {
    private val cartoonizerModel by lazy { Cartoonizer.newInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val originalImage = loadBitmapFromAsset()
        val tensorImage = TensorImage.fromBitmap(loadBitmapFromAsset())
        val processedImage = cartoonizerModel.process(tensorImage).cartoonizedImageAsTensorImage.bitmap
        cartoonizerModel.close()

        setContent {
            ModelEaterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Before")
                        Image(
                            bitmap = originalImage.asImageBitmap(),
                            contentScale = ContentScale.FillBounds,
                            contentDescription = "Cartoonized iamge"
                        )
                        Text(text = "After")
                        Image(
                            bitmap = processedImage.asImageBitmap(),
                            contentScale = ContentScale.FillBounds,
                            contentDescription = "Cartoonized iamge"
                        )
                    }
                }
            }
        }
    }

    /** Fetch the image stored in asset folder */
    private fun loadBitmapFromAsset(): Bitmap {
        val inputStream = applicationContext.assets.open("test-image.jpeg")
        val bitmap = BitmapFactory.decodeStream(inputStream)

        return Bitmap.createScaledBitmap(bitmap, 224, 224, false)
    }

}