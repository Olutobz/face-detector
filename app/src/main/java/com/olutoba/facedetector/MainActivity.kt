package com.olutoba.facedetector

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.*

class MainActivity : AppCompatActivity() {

    lateinit var imageView: ImageView
    lateinit var faceButton: Button
    lateinit var selectedImage: Bitmap
    lateinit var graphicOverlay: GraphicOverlay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.image_view)
        faceButton = findViewById(R.id.button_face)

        faceButton.setOnClickListener {
            runFaceContourDetection()
        }

    }

    fun runFaceContourDetection() {
        val image = InputImage.fromBitmap(selectedImage, 0)
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .build()

        faceButton.isEnabled = false

        val detector = FaceDetection.getClient(options)
        detector.process(image)
            .addOnSuccessListener {
                faceButton.isEnabled = true
                processFaceContourDetectionResult(it)
            }
            .addOnFailureListener { exception ->
                // Task failed with an exception
                faceButton.isEnabled = true
                exception.printStackTrace()
            }

    }

    private fun processFaceContourDetectionResult(faces: List<Face>) {
        // Task completed successfully
        if (faces.isEmpty()) {
            showToast("No face Found")
            return
        }

        graphicOverlay.clear()
        for (face in faces) {
            val bounds = face.boundingBox
            val rotY = face.headEulerAngleY // Head is rotated to the right rotY degrees
            val rotZ = face.headEulerAngleZ // Head is tilted sideways rotZ degrees

            // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
            // nose available):
            val leftEar = face.getLandmark(FaceLandmark.LEFT_EAR)
            leftEar?.let {
                val leftEarPos = leftEar.position
            }

            // If contour detection was enabled:
            val leftEyeContour = face.getContour(FaceContour.LEFT_EYE)?.points
            val upperLipBottomContour = face.getContour(FaceContour.UPPER_LIP_BOTTOM)?.points

            // If classification was enabled:
            if (face.smilingProbability != null) {
                val smileProb = face.smilingProbability
            }
            if (face.rightEyeOpenProbability != null) {
                val rightEyeOpenProb = face.rightEyeOpenProbability
            }

            // If face tracking was enabled:
            if (face.trackingId != null) {
                val id = face.trackingId
            }
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }


}