package com.example.tryingtomakefetchhappen.sampledata

import android.content.Context
import com.example.tryingtomakefetchhappen.model.HiringItem
import timber.log.Timber

object HiringItemSampleDataCreator {
    fun create(itemMap: Map<Int, List<HiringItem>>): String {
        val sb = StringBuilder("val hiringItemMap = mapOf(\n    ")
        itemMap.forEach { (listId, items) ->
            sb.append("""$listId to listOf(${items.joinToString(", ")}),
                |    
            """.trimMargin())
        }
        sb.append(")")
        return sb.toString()
    }

    fun writeToFile(context: Context, text: String, filename: String = "hiring_item_sample_data.txt") {
        try {
            context.openFileOutput(filename, Context.MODE_PRIVATE).use { outputStream ->
                outputStream.write(text.toByteArray())
            }
        } catch (e: Exception) {
            Timber.e(e, "Error writing sample data to file $filename")
        }
    }
}