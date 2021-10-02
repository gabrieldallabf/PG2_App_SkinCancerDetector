package com.example.skincancerdetector.classificador

import android.graphics.Bitmap
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.Tensor
import org.pytorch.torchvision.TensorImageUtils


class Classificador {
    var model: Module? = null
    var mean = floatArrayOf(0.485f, 0.456f, 0.406f)
    var std = floatArrayOf(0.229f, 0.224f, 0.225f)

    fun classifier(modelPath: String?) {
        model = Module.load(modelPath)
    }

    fun preprocess(bitmap: Bitmap?, size: Int): Tensor {
        var imagem = bitmap
        imagem = Bitmap.createScaledBitmap(bitmap!!, size, size, false)
        return TensorImageUtils.bitmapToFloat32Tensor(imagem, mean, std)
    }

    fun metadata20(data: FloatArray?): Tensor {
        return Tensor.fromBlob(data, longArrayOf(1, 20))
    }

    fun argMax(inputs: FloatArray): Int {
        var maxIndex = -1
        var maxvalue = 0.0f
        for (i in inputs.indices) {
            if (inputs[i] > maxvalue) {
                maxIndex = i
                maxvalue = inputs[i]
            }
        }
        return maxIndex
    }

    fun predict(bitmap: Bitmap?, data: FloatArray, type: String): String {
        val tensor = preprocess(bitmap, 224)
        val inputs = IValue.from(tensor)
        val metadataTensor = metadata20(data)
        val metaInputs = IValue.from(metadataTensor)
        val outputs: Tensor = model!!.forward(inputs, metaInputs).toTensor()
        val scores = outputs.dataAsFloatArray
        val classIndex = argMax(scores)
        val finalScore = scores[classIndex] * 100
        if (type == "C") {
            return Constants.MOBILENET_CLASSNAME_CANCER[classIndex]
        }else {
            return Constants.MOBILENET_CLASSNAME_MELANOMA[classIndex]
        }

    }
    fun confidenceScore(bitmap: Bitmap?, data: FloatArray): Float {
        val tensor = preprocess(bitmap, 224)
        val inputs = IValue.from(tensor)
        val metadataTensor = metadata20(data)
        val metaInputs = IValue.from(metadataTensor)
        val outputs: Tensor = model!!.forward(inputs, metaInputs).toTensor()
        val scores = outputs.dataAsFloatArray
        val classIndex = argMax(scores)
        return scores[classIndex] * 100
    }
}