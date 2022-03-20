package uz.techie.schedulize.ui.screens.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import uz.techie.schedulize.databinding.ScreenCardListBinding
import uz.techie.schedulize.ui.adapters.CardRecyclerAdapter
import uz.techie.schedulize.ui.models.DayOfWeek
import uz.techie.schedulize.ui.screens.main.HomeScreenDirections
import uz.techie.schedulize.utils.extentions.doOnApplyWindowInsets
import uz.techie.schedulize.utils.extentions.navigationBarsInsets

@AndroidEntryPoint
class CardListScreen : Fragment() {

    private var _binding: ScreenCardListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CardListScreenViewModel by viewModels()

    private lateinit var listAdapter: CardRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postponeEnterTransition()
        _binding = ScreenCardListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recycler.doOnApplyWindowInsets { windowInsets ->
            updatePadding(bottom = windowInsets.navigationBarsInsets.bottom)
            windowInsets
        }
        setRecycler()
        observe()
        val day = arguments?.getSerializable(ARG_DAY) as DayOfWeek
        viewModel.loadData(day)
    }

    private fun observe() {
        lifecycleScope.launchWhenStarted {
            viewModel.listOfSubjects.collect {
                (view?.parent as? ViewGroup)?.doOnPreDraw {
                    startPostponedEnterTransition()
                }
                listAdapter.setSubjects(it)
            }
        }
    }

    private fun setRecycler() {
        listAdapter = CardRecyclerAdapter()
        listAdapter.itemClickListener = { subject, extras ->
            val action = HomeScreenDirections.actionFragmentMainToCardDetailScreen(subject.id!!)
            findNavController().navigate(action)
        }
        binding.recycler.adapter = listAdapter
    }

    companion object {
        private val TAG = CardListScreen::class.java.canonicalName
        private const val ARG_DAY = "day_of_week"

        fun newInstance(day: DayOfWeek): CardListScreen {
            val fragment = CardListScreen()
            fragment.arguments = Bundle().apply {
                putSerializable(ARG_DAY, day)
            }
            return fragment
        }
    }
}