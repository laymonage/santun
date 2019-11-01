package id.ac.ui.cs.mobileprogramming.sage.santun.ui.main

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import id.ac.ui.cs.mobileprogramming.sage.santun.databinding.MainFragmentBinding
import id.ac.ui.cs.mobileprogramming.sage.santun.R
import id.ac.ui.cs.mobileprogramming.sage.santun.ComposeActivity
import kotlinx.android.synthetic.main.main_fragment.view.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: MainFragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.main_fragment, container, false
        )
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.detail_button.setOnClickListener {
            fragmentManager!!.beginTransaction()
                .replace(R.id.container, DetailFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
        view.compose_button.setOnClickListener {
            val intent = Intent(this.context, ComposeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}
