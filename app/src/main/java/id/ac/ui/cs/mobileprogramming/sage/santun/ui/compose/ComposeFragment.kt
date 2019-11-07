package id.ac.ui.cs.mobileprogramming.sage.santun.ui.compose

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import id.ac.ui.cs.mobileprogramming.sage.santun.R
import id.ac.ui.cs.mobileprogramming.sage.santun.databinding.ComposeFragmentBinding
import id.ac.ui.cs.mobileprogramming.sage.santun.model.Message
import id.ac.ui.cs.mobileprogramming.sage.santun.model.MessageViewModel
import id.ac.ui.cs.mobileprogramming.sage.santun.util.storage.GET_REQUEST_CODE
import id.ac.ui.cs.mobileprogramming.sage.santun.util.storage.copyFileToAppDir
import id.ac.ui.cs.mobileprogramming.sage.santun.util.storage.getContent
import kotlinx.android.synthetic.main.compose_fragment.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ComposeFragment : Fragment() {

    companion object {
        fun newInstance() = ComposeFragment()
    }

    private val ioScope = CoroutineScope(Dispatchers.IO)
    private lateinit var viewModel: ComposeViewModel
    private lateinit var messageViewModel: MessageViewModel

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
                saveMessage()
                activity!!.finish()
            } else {
                Toast.makeText(context, R.string.empty_message, Toast.LENGTH_LONG).show()
            }
        }
        view.imageButton.setOnClickListener {
            getContent(this, "image/*")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GET_REQUEST_CODE && data != null) {
                viewModel.imageUri.value = data.data
            }
        }
    }

    private fun saveMessage() {
        val fragment = this
        ioScope.launch {
            val message = if (viewModel.imageUri.value != null) {
                val newUri = ioScope.async {
                    copyFileToAppDir(fragment, viewModel.imageUri.value!!).toUri()
                }
                Message(
                    null, viewModel.sender.value!!, viewModel.receiver.value!!,
                    viewModel.message.value!!, newUri.await().toString()
                )
            } else {
                Message(
                    null, viewModel.sender.value!!, viewModel.receiver.value!!, viewModel.message.value!!
                )
            }
            messageViewModel.insert(message)
        }
    }
}
