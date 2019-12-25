package id.ac.ui.cs.mobileprogramming.sage.santun.ui.compose

import android.app.Activity
import android.content.Intent
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
import id.ac.ui.cs.mobileprogramming.sage.santun.R
import id.ac.ui.cs.mobileprogramming.sage.santun.databinding.ComposeFragmentBinding
import id.ac.ui.cs.mobileprogramming.sage.santun.data.model.Message
import id.ac.ui.cs.mobileprogramming.sage.santun.data.model.MessageViewModel
import id.ac.ui.cs.mobileprogramming.sage.santun.data.remote.APIWise
import id.ac.ui.cs.mobileprogramming.sage.santun.data.remote.MessageBody
import id.ac.ui.cs.mobileprogramming.sage.santun.util.storage.*
import kotlinx.android.synthetic.main.compose_fragment.view.*
import kotlinx.coroutines.*
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
            if (viewModel.messageIsValid()) {
                sendMessage()
                activity!!.finish()
            } else {
                Toast.makeText(context, R.string.empty_message, Toast.LENGTH_LONG).show()
            }
        }
        view.imageButton.setOnClickListener {
            getContent(this, "image/*")
        }
        view.cameraButton.setOnClickListener {
            onCameraButtonClicked()
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
            val message = if (viewModel.imageUri.value != null) {
                val newUri = ioScope.async {
                    copyFileToAppDir(fragment, viewModel.imageUri.value!!, uuid.toString()).toUri()
                }
                Message(
                    null, viewModel.sender.value!!, viewModel.receiver.value!!,
                    viewModel.message.value!!, newUri.await().toString(), uuid = uuid.toString()
                )
            } else {
                Message(
                    null, viewModel.sender.value!!, viewModel.receiver.value!!, viewModel.message.value!!,
                    uuid = uuid.toString()
                )
            }
            service.createMessage(MessageBody(message))
            messageViewModel.insert(message)
        }
    }

    fun onCameraButtonClicked() {
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
}
