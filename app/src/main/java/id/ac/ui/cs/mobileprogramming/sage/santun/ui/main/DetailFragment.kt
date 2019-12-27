package id.ac.ui.cs.mobileprogramming.sage.santun.ui.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import id.ac.ui.cs.mobileprogramming.sage.santun.R
import id.ac.ui.cs.mobileprogramming.sage.santun.databinding.DetailFragmentBinding
import id.ac.ui.cs.mobileprogramming.sage.santun.util.storage.*
import kotlinx.android.synthetic.main.detail_fragment.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    companion object {
        fun newInstance() = DetailFragment()
    }

    private val ioScope = CoroutineScope(Dispatchers.IO)
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: DetailFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.detail_fragment, container, false
        )
        viewModel = ViewModelProvider(activity!!).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.fab_detail_save.setOnClickListener {
            onMessageSave()
        }
        viewModel.message.value?.imageUri?.let {
            view.imageDetail.setImageURI(Uri.parse(it))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CREATE_REQUEST_CODE && data != null) {
                onMessageFileUriReceived(data)
            }
        }
    }

    private fun onMessageSave() {
        val message = viewModel.message.value!!
        val fileName = "${message.id.toString()}.json"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            createDocument(
                this, "application/json", fileName
            )
        } else {
            ioScope.launch {
                writeStringToFile(activity!!, viewModel.getJsonMessage(), fileName)
            }
        }
    }

    private fun onMessageFileUriReceived(data: Intent) {
        ioScope.launch {
            writeStringToFile(activity!!, viewModel.getJsonMessage(), data.data!!)
        }
    }

}
