package id.ac.ui.cs.mobileprogramming.sage.santun.ui.compose

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import id.ac.ui.cs.mobileprogramming.sage.santun.R
import id.ac.ui.cs.mobileprogramming.sage.santun.databinding.PermissionRationaleFragmentBinding

class PermissionRationaleFragment(val resId: Int) : Fragment() {

    companion object {
        fun newInstance(resId: Int) = PermissionRationaleFragment(resId)
    }

    private lateinit var viewModel: PermissionRationaleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: PermissionRationaleFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.permission_rationale_fragment, container, false
        )
        viewModel = ViewModelProvider(this).get(PermissionRationaleViewModel::class.java)
        viewModel.message.value = getString(resId)
        binding.viewModel = viewModel
        return binding.root
    }

}
