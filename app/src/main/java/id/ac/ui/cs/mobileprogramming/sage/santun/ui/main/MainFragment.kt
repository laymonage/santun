package id.ac.ui.cs.mobileprogramming.sage.santun.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.ac.ui.cs.mobileprogramming.sage.santun.databinding.MainFragmentBinding
import id.ac.ui.cs.mobileprogramming.sage.santun.R
import id.ac.ui.cs.mobileprogramming.sage.santun.ComposeActivity
import id.ac.ui.cs.mobileprogramming.sage.santun.model.Message
import id.ac.ui.cs.mobileprogramming.sage.santun.model.MessageViewModel
import kotlinx.android.synthetic.main.main_fragment.view.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

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
            val intent = Intent(context, ComposeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

}
