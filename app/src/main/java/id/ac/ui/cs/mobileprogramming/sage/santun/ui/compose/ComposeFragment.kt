package id.ac.ui.cs.mobileprogramming.sage.santun.ui.compose

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import id.ac.ui.cs.mobileprogramming.sage.santun.MainActivity
import id.ac.ui.cs.mobileprogramming.sage.santun.R
import id.ac.ui.cs.mobileprogramming.sage.santun.data.model.Message
import id.ac.ui.cs.mobileprogramming.sage.santun.data.model.MessageViewModel
import id.ac.ui.cs.mobileprogramming.sage.santun.data.remote.APIWise
import id.ac.ui.cs.mobileprogramming.sage.santun.data.remote.MessageBody
import id.ac.ui.cs.mobileprogramming.sage.santun.databinding.ComposeFragmentBinding
import id.ac.ui.cs.mobileprogramming.sage.santun.util.permissions.Camera
import id.ac.ui.cs.mobileprogramming.sage.santun.util.permissions.Permission
import id.ac.ui.cs.mobileprogramming.sage.santun.util.storage.*
import kotlinx.android.synthetic.main.compose_fragment.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.util.*

class ComposeFragment : Fragment() {

    companion object {
        fun newInstance() = ComposeFragment()
    }

    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val service = APIWise.getAPIService()
    private lateinit var viewModel: ComposeViewModel
    private lateinit var messageViewModel: MessageViewModel
    private var currentPhotoUri: Uri? = null
    private val uuid: UUID = UUID.randomUUID()
    private val cameraPermission: Permission = Camera()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: ComposeFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.compose_fragment, container, false
        )
        viewModel = ViewModelProvider(this).get(ComposeViewModel::class.java)
        messageViewModel = ViewModelProvider(this).get(MessageViewModel::class.java)
        binding.viewModel = viewModel
        binding.messageViewModel = messageViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.fab_compose_send.setOnClickListener {
            onSendButtonClicked()
        }
        view.imageButton.setOnClickListener {
            getContent(this, "image/*")
        }
        view.cameraButton.setOnClickListener {
            onCameraButtonClicked()
        }
    }

    private fun onSendButtonClicked() {
        if (viewModel.messageIsValid()) {
            sendMessage()
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            }
            startActivity(intent)
        } else {
            Toast.makeText(context, R.string.empty_message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GET_REQUEST_CODE && data != null) {
                viewModel.imageUri.value = data.data
            } else if (requestCode == TAKE_PHOTO_REQUEST_CODE && currentPhotoUri != null) {
                viewModel.imageUri.value = currentPhotoUri
                currentPhotoUri = null
            }
        }
    }

    private fun sendMessage() {
        val fragment = this
        ioScope.launch {
            withContext(ioScope.coroutineContext) {
                val message: Message
                if (viewModel.imageUri.value != null) {
                    val newUri = copyFileToAppDir(fragment, viewModel.imageUri.value!!, uuid.toString()).toUri()
                    message = Message(
                        null, viewModel.sender.value!!, viewModel.receiver.value!!,
                        viewModel.message.value!!, newUri.toString(), uuid = uuid.toString()
                    )
                    val file = File(newUri.path!!)
                    val requestFile = RequestBody.create(MediaType.parse(activity!!.contentResolver.getType(viewModel.imageUri.value!!)!!), file)
                    val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
                    val uuidBody = RequestBody.create(MultipartBody.FORM, uuid.toString())
                    val call = service.upload(uuidBody, body)
                    if (call.isSuccessful) {
                        Log.d("upload success", call.message())
                    } else {
                        Log.d("upload failed", call.message())
                    }

                } else {
                    message = Message(
                        null, viewModel.sender.value!!, viewModel.receiver.value!!, viewModel.message.value!!,
                        uuid = uuid.toString()
                    )
                }
                val call = service.createMessage(MessageBody(message))
                if (call.isSuccessful) {
                    activity!!.runOnUiThread {
                        Toast.makeText(context, R.string.message_send_success, Toast.LENGTH_LONG).show()
                    }
                } else {
                    activity!!.runOnUiThread {
                        Toast.makeText(context, R.string.message_send_failed, Toast.LENGTH_LONG).show()
                    }
                }
                messageViewModel.insert(message)
                activity!!.finish()
            }
        }
    }

    private fun onCameraButtonClicked() {
        if (!cameraPermission.verifyPermission(activity!!)) {
            cameraPermission.requestPermission(this)
            return
        }
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { imageCaptureIntent ->
            imageCaptureIntent.resolveActivity(activity!!.packageManager)?.also {
                val photoFile = try {
                    createImageFile(activity!!, uuid.toString())
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    currentPhotoUri = FileProvider.getUriForFile(activity!!, "id.ac.ui.cs.mobileprogramming.sage.santun", it)
                    imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri)
                    startActivityForResult(imageCaptureIntent, TAKE_PHOTO_REQUEST_CODE)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Camera.REQUEST_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onCameraButtonClicked()
                } else {
                    activity!!.supportFragmentManager.beginTransaction()
                        .replace(R.id.container, PermissionRationaleFragment.newInstance(R.string.camera_permission_rationale))
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }
}
