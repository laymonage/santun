package id.ac.ui.cs.mobileprogramming.sage.santun.util.data

import com.google.gson.GsonBuilder

fun toJson(obj: Any): String {
    return GsonBuilder().setPrettyPrinting().create().toJson(obj)
}
