package com.example.ExpenseTracker

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import android.content.res.AssetFileDescriptor
import android.util.Log
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


data class VectorizerData(
    val vocabulary_: Map<String, Int>,
    val idf_: List<Double>,
    val stop_words: List<String>
)
data class LabelEncoderData(
    val classes_: List<String>
)



class ExpenseCategoryModelLoader(val context: Context) {
    private var interpreter: Interpreter? = null
    private var vectorizerData: VectorizerData? = null
    private var labelEncoderData: LabelEncoderData? = null
    init {
        loadModel(context)
        vectorizerData = loadVectorizerData(context)
        labelEncoderData = loadLabelEncoderData(context)
    }
    private fun loadModel(context: Context)
    {
        try {
            Log.d("Shayantan", "Loading model")
            interpreter = Interpreter(loadModelFile(context))
        } catch (e: Exception) {
            Log.e("Shayantan", "Error loading model", e)
            e.printStackTrace()
        }
    }

    private fun loadModelFile(context: Context): MappedByteBuffer {
        val fileDescriptor: AssetFileDescriptor = context.assets.openFd("expense_model.tflite")
        val fileInputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = fileInputStream.channel
        val startOffset = fileDescriptor.startOffset
        val length = fileDescriptor.length
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, length)
    }
    private fun loadVectorizerData(context: Context): VectorizerData {
        val inputStream = context.assets.open("vectorizer.json")
        val reader = InputStreamReader(inputStream)
        return Gson().fromJson(reader, object : TypeToken<VectorizerData>() {}.type)
    }
    private fun loadLabelEncoderData(context: Context): LabelEncoderData {
        val inputStream = context.assets.open("label_encoder.json")
        val reader = InputStreamReader(inputStream)
        return Gson().fromJson(reader, object : TypeToken<LabelEncoderData>() {}.type)
    }

    fun getVectorizerData(): VectorizerData? {
        return vectorizerData!!
    }
    fun getLabelEncoderData(): LabelEncoderData? {
        return labelEncoderData
    }
    fun getInterpreter(): Interpreter? {
        return interpreter
    }
}

class ExpensePredictor(val context: Context) {
    private val interpreter: Interpreter
    private val labelEncoderData: LabelEncoderData
    private val vectorizerData: VectorizerData
    init {
        val loader = ExpenseCategoryModelLoader(context)
        interpreter = loader.getInterpreter()!!
        vectorizerData = loader.getVectorizerData()!!
        labelEncoderData = loader.getLabelEncoderData()!!
    }
    private fun preprocessInput(description: String): FloatArray {
        val tokens = description.split(" ")
        val tfidfVector = FloatArray(vectorizerData.vocabulary_.size) { 0.0f }

        tokens.filterNot { it in vectorizerData.stop_words }
            .forEach { token ->
                vectorizerData.vocabulary_[token]?.let { index ->
                    tfidfVector[index] += 1.0f
                }
            }

        for (i in tfidfVector.indices) {
            tfidfVector[i] *= vectorizerData.idf_[i].toFloat()
        }

        return tfidfVector
    }
    fun predict(description: String): String {
        val input = preprocessInput(description)
        val inputShape = interpreter.getInputTensor(0).shape()
        val inputSize = inputShape[1]
        val inputBuffer = ByteBuffer.allocateDirect(4 * inputSize).order(ByteOrder.nativeOrder())

        for (i in 0 until inputSize) {
            if (i < input.size) {
                inputBuffer.putFloat(input[i])
            } else {
                inputBuffer.putFloat(0.0f) // Padding if necessary
            }
        }
        inputBuffer.rewind() // Reset buffer position

        val output = Array(1) { FloatArray(labelEncoderData.classes_.count()) }
        interpreter.run(inputBuffer, output)

        return getCategory(output[0])
    }
    private fun getCategory(probabilities: FloatArray): String {
        val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: 0
        return labelEncoderData.classes_[maxIndex]
    }
}


