package uz.techie.schedulize.ui.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import uz.techie.schedulize.R
import uz.techie.schedulize.adapters.CardRecyclerAdapter
import uz.techie.schedulize.ui.MainFragmentDirections

private const val ARG_DAY = "day_of_week"

@AndroidEntryPoint
class CardListFragment : Fragment() {

    companion object {
        private val TAG = CardListFragment::class.java.canonicalName
        fun newInstance(day: Int): CardListFragment {
            val fragment = CardListFragment()
            fragment.arguments = Bundle().apply {
                putInt(ARG_DAY, day)
            }
            Log.d(TAG, "put argument day = $day")
            return fragment
        }
    }

    private val viewModel: CardListFragmentViewModel by viewModels()
    private lateinit var listAdapter: CardRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.card_list_fragment, container, false)
        listAdapter = CardRecyclerAdapter(resources)

        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                adapter = listAdapter
                viewModel.subjectsOfDayLiveData.observe(viewLifecycleOwner, Observer { subjects ->
                    listAdapter.setSubjects(subjects)
                })
                listAdapter.setOnItemClickListener { position ->
                    val id = viewModel.subjectsOfDayLiveData.value?.get(position)?.id ?: -1
                    val action = MainFragmentDirections.actionFragmentMainToCardViewFragment(id)
                    findNavController().navigate(action)
                }
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.takeIf { it.containsKey(ARG_DAY) }?.apply {
            val day = getInt(ARG_DAY, -1)
            if (day != -1) viewModel.loadData(day)
            Log.d(TAG,"get argument day = $day")
        }
    }

}