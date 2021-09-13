package uz.techie.schedulize.ui.cardview

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.techie.schedulize.R
import uz.techie.schedulize.databinding.CardViewFragmentBinding
import uz.techie.schedulize.utils.StatusBarSetColorCallBack

@AndroidEntryPoint
class CardViewFragment : Fragment() {
    private val TAG = CardViewFragment::class.java.canonicalName

    private val args: CardViewFragmentArgs by navArgs()
    private val  viewModel: CardViewViewModel by viewModels()

    private var _binding:CardViewFragmentBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CardViewFragmentBinding.inflate(inflater,container,false)

        binding.toolbar.apply {
            title = ""
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            inflateMenu(R.menu.card_view_menu)
            setOnMenuItemClickListener{
                when(it.itemId){
                    R.id.edit_subject ->{
                        val id = viewModel.subjectLiveData.value?.id!!
                        val action = CardViewFragmentDirections.actionCardViewFragmentToAddFragment(id)
                        findNavController().navigate(action)
                        true
                    }
                    R.id.delete_subject ->{
                        viewModel.deleteSubject()
                        findNavController().navigate(R.id.action_cardViewFragment_to_fragmentMain)
                        true
                    }
                    else -> false
                }
            }
        }
        viewModel.subjectLiveData.observe(viewLifecycleOwner, Observer {
            Log.d(TAG,"observed subject = $it")
            binding.toolbar.setBackgroundResource(it.color)
            binding.layoutRoot.setBackgroundResource(it.color)

            binding.subjectName.text = it.subjectName
            binding.subjectDay.text = resources.getStringArray(R.array.day_of_week)[it.dayOfWeek]
            binding.subjectPeriod.text = it.subjectPeriod.toString()

            if (it.subjectTeacher.isNullOrEmpty()){
                binding.iconTeacher.visibility = View.GONE
                binding.subjectTeacher.visibility = View.GONE
            } else {
                binding.iconTeacher.visibility = View.VISIBLE
                binding.subjectTeacher.visibility = View.VISIBLE
                binding.subjectTeacher.text = it.subjectTeacher
            }

            if (it.subjectPlace.isNullOrEmpty()){
                binding.iconPlace.visibility = View.GONE
                binding.subjectPlace.visibility = View.GONE
            } else {
                binding.iconPlace.visibility = View.VISIBLE
                binding.subjectPlace.visibility = View.VISIBLE
                binding.subjectPlace.text = it.subjectPlace
            }
            binding.toolbar.setBackgroundResource(it.color)

            lifecycleScope.launch {
                delay(150)
                (activity as StatusBarSetColorCallBack).setStatusBarColor(it.color)
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = args.argViewSubjectId
        viewModel.loadData(id)

        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as StatusBarSetColorCallBack).setStatusBarColor(R.color.primary)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}