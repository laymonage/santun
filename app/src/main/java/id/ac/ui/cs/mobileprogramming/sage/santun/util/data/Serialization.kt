package id.ac.ui.cs.mobileprogramming.sage.santun.util.data

import com.google.gson.Gson

fun toJson(obj: Any): String {
    return Gson().toJson(obj)
}
