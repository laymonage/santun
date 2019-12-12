package id.ac.ui.cs.mobileprogramming.sage.santun.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import id.ac.ui.cs.mobileprogramming.sage.santun.databinding.MainFragmentBinding
import id.ac.ui.cs.mobileprogramming.sage.santun.R
import id.ac.ui.cs.mobileprogramming.sage.santun.ComposeActivity
import id.ac.ui.cs.mobileprogramming.sage.santun.model.Message
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment.view.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MessageListAdapter

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: MainFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.main_fragment, container, false
        )
        viewModel = ViewModelProvider(activity!!).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        val options = FirebaseRecyclerOptions.Builder<Message>()
            .setQuery(viewModel.query, Message::class.java)
            .setLifecycleOwner(activity)
            .build()
        adapter = MessageListAdapter(onItemClickListener, options)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.recyclerview
        recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(context!!)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        recyclerView.layoutManager = layoutManager

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
