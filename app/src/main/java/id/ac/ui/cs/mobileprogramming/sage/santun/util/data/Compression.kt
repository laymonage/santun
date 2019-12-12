package id.ac.ui.cs.mobileprogramming.sage.santun.util.data

import com.jiechic.library.android.snappy.Snappy

fun compress(input: String): ByteArray {
    return Snappy.compress(input)
}
