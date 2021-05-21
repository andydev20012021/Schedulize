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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
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

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CardViewFragmentBinding.inflate(inflater,container,false)

        viewModel.subjectLiveData.observe(viewLifecycleOwner, Observer {
            Log.d(TAG,"observed subject = $it")
            binding.layoutRoot.setBackgroundResource(it.color)


            binding.subjectName.text = it.subjectName
            binding.subjectDay.text = resources.getStringArray(R.array.day_of_week)[it.dayOfWeek]
            binding.subjectPeriod.text = it.subjectPeriod.toString()

            if (it.subjectTeacher.isNullOrEmpty()){
                binding.iconTeacher.visibility = View.GONE
                binding.subjectTeacher.visibility = View.GONE
            } else binding.subjectTeacher.text = it.subjectTeacher

            if (it.subjectPlace.isNullOrEmpty()){
                binding.iconPlace.visibility = View.GONE
                binding.subjectPlace.visibility = View.GONE
            } else binding.subjectPlace.text = it.subjectPlace

        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = args.argViewSubjectId
        viewModel.loadData(id)

        toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        toolbar?.apply {
            title = ""
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            viewModel.subjectLiveData.observe(viewLifecycleOwner, Observer {
                setBackgroundResource(it.color)
                (activity as StatusBarSetColorCallBack).setStatusBarColor(it.color)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        toolbar.apply {
            setBackgroundResource(R.color.primary)
            (activity as StatusBarSetColorCallBack).setStatusBarColor(R.color.primary)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.card_view_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.edit_subject ->{
                val id = viewModel.subjectLiveData.value?.id!!
                val action = CardViewFragmentDirections.actionCardViewFragmentToAddFragment(id)
                findNavController().navigate(action)
                true
            }
            R.id.delete_subject ->{
                viewModel.deleteSubject()
                findNavController().popBackStack()
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}