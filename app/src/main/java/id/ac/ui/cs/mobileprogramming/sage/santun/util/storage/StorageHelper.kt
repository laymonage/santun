package id.ac.ui.cs.mobileprogramming.sage.santun.util.storage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

const val CREATE_REQUEST_CODE = 42
const val GET_REQUEST_CODE = 43
const val TAKE_PHOTO_REQUEST_CODE = 44

@RequiresApi(Build.VERSION_CODES.KITKAT)
fun createDocument(fragment: Fragment, mimeType: String, fileName: String) {
    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = mimeType
        putExtra(Intent.EXTRA_TITLE, fileName)
    }
    fragment.startActivityForResult(intent, CREATE_REQUEST_CODE)
}

@Throws(IOException::class)
fun createImageFile(activity: Activity, destName: String): File {
    val storageDir: File = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
    return File.createTempFile(destName, ".jpg", storageDir)
}

fun getContent(fragment: Fragment, mimeType: String) {
    val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = mimeType
    }
    fragment.startActivityForResult(intent, GET_REQUEST_CODE)
}

suspend fun writeStringToFile(activity: Activity, content: String, uri: Uri) {
    withContext(Dispatchers.IO) {
        activity.contentResolver.openOutputStream(uri)!!.write(content.toByteArray())
    }
}

suspend fun writeStringToFile(activity: Activity, content: String, fileName: String) {
    withContext(Dispatchers.IO) {
        val file = File(activity.applicationContext.filesDir, fileName)
        file.bufferedWriter().use { it.write(content) }
    }
}

suspend fun copyFileToAppDir(fragment: Fragment, uriSrc: Uri, destName: String): File {
    val file = File(
        fragment.activity!!.applicationContext.getExternalFilesDir(null), destName
    )
    withContext(Dispatchers.IO) {
        fragment.activity!!.contentResolver.openInputStream(uriSrc).use {
            input -> file.outputStream().use {
                output -> input!!.copyTo(output)
            }
        }
    }
    return file
}
