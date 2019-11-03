package id.ac.ui.cs.mobileprogramming.sage.santun.ui.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import id.ac.ui.cs.mobileprogramming.sage.santun.R
import id.ac.ui.cs.mobileprogramming.sage.santun.databinding.ComposeFragmentBinding
import id.ac.ui.cs.mobileprogramming.sage.santun.model.MessageViewModel
import kotlinx.android.synthetic.main.compose_fragment.view.*

class ComposeFragment : Fragment() {

    companion object {
        fun newInstance() = ComposeFragment()
    }

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
                messageViewModel.insert(viewModel.getMessage())
                activity!!.finish()
            } else {
                Toast.makeText(context, R.string.empty_message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}
