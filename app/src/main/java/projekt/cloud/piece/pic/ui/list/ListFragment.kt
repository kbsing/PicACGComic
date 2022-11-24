package projekt.cloud.piece.pic.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import projekt.cloud.piece.pic.base.BaseFragment
import projekt.cloud.piece.pic.databinding.FragmentListBinding

class ListFragment: BaseFragment() {
    
    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding
        get() = _binding!!
    private val root get() = binding.root
    private val recyclerView: RecyclerView
        get() = binding.recyclerView
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListBinding.inflate(inflater)
        return root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerVewAdapter = RecyclerViewAdapter {
        }
        recyclerView.adapter = recyclerVewAdapter
    }
    
}