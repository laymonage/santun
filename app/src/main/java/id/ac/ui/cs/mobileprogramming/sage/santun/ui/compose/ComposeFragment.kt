package id.ac.ui.cs.mobileprogramming.sage.santun.ui.compose

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.ac.ui.cs.mobileprogramming.sage.santun.R

class ComposeFragment : Fragment() {

    companion object {
        fun newInstance() = ComposeFragment()
    }

    private lateinit var viewModel: ComposeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.compose_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ComposeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
