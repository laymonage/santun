package id.ac.ui.cs.mobileprogramming.sage.santun.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.ac.ui.cs.mobileprogramming.sage.santun.databinding.MainFragmentBinding
import id.ac.ui.cs.mobileprogramming.sage.santun.R
import id.ac.ui.cs.mobileprogramming.sage.santun.ComposeActivity
import id.ac.ui.cs.mobileprogramming.sage.santun.data.model.Message
import id.ac.ui.cs.mobileprogramming.sage.santun.data.model.MessageList
import id.ac.ui.cs.mobileprogramming.sage.santun.data.model.MessageViewModel
import id.ac.ui.cs.mobileprogramming.sage.santun.util.broadcast.ConnectionIdentifier
import id.ac.ui.cs.mobileprogramming.sage.santun.util.storage.CREATE_REQUEST_CODE
import id.ac.ui.cs.mobileprogramming.sage.santun.util.storage.createDocument
import id.ac.ui.cs.mobileprogramming.sage.santun.util.storage.writeByteArrayToFile
import id.ac.ui.cs.mobileprogramming.sage.santun.util.storage.writeStringToFile
import kotlinx.android.synthetic.main.main_fragment.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val ioScope = CoroutineScope(Dispatchers.IO)

    private val onItemClickListener = object : OnItemClickListener {
        override fun onItemClicked(message: Message) {
            val isTablet = resources.getBoolean(R.bool.is_tablet)
            viewModel.message.value = message
            if (!isTablet) {
                activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.container, DetailFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var messageViewModel: MessageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: MainFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.main_fragment, container, false
        )
        viewModel = ViewModelProvider(activity!!).get(MainViewModel::class.java)
        messageViewModel = ViewModelProvider(activity!!).get(MessageViewModel::class.java)
        binding.viewModel = viewModel
        binding.composeViewModel = messageViewModel
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.recyclerview
        val adapter = MessageListAdapter(onItemClickListener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context!!)

        messageViewModel.allMessages.observe(
            viewLifecycleOwner, Observer {
                    messages -> messages?.let { adapter.setMessages(it) }
            }
        )

        view.fab_main_compose.setOnClickListener {
            if (ConnectionIdentifier.getConnectionType(context) == ConnectionIdentifier.CONNECTION_NONE) {
                Toast.makeText(context, R.string.connect_internet_hint, Toast.LENGTH_LONG).show()
            } else {
                val intent = Intent(context, ComposeActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_backup_all -> {
            onBackup()
            true
        }
        R.id.action_about -> {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.container, AboutFragment.newInstance())
                .addToBackStack(null)
                .commit()
            true
        }
        else -> {
            false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CREATE_REQUEST_CODE && data != null) {
                onBackupFileUriReceived(data)
            }
        }
    }

    private fun onBackup() {
        val fileName = "${DateTime.now().millis}.santun"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            createDocument(
                this, "text/plain", fileName
            )
        } else {
            ioScope.launch {
                messageViewModel.getCompressedJsonMessageList()?.let {
                    writeByteArrayToFile(activity!!, it, fileName)
                }
            }
        }
    }

    private fun onBackupFileUriReceived(data: Intent) {
        ioScope.launch {
            messageViewModel.getCompressedJsonMessageList()?.let {
                writeByteArrayToFile(activity!!, it, data.data!!)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}
