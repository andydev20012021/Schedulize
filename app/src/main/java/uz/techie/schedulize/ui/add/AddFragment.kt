package uz.techie.schedulize.ui.add

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.text.format.DateFormat
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import uz.techie.schedulize.R
import uz.techie.schedulize.databinding.AddFragmentBinding
import uz.techie.schedulize.models.Subject
import uz.techie.schedulize.utils.SubjectPeriod
import java.sql.Time

@AndroidEntryPoint
class AddFragment : Fragment() {
    private val EXTRA_START_DIALOG_TIME_PICKER = 0
    private val EXTRA_END_DIALOG_TIME_PICKER = 1

    private val viewModel: AddViewModel by viewModels()

    private val args: AddFragmentArgs by navArgs()

    private var _binding: AddFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var toolbar: Toolbar
    private lateinit var dropDownAdaper: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddFragmentBinding.inflate(inflater, container, false)

        viewModel.dayOfWeek = resources.getStringArray(R.array.day_of_week)
        dropDownAdaper =
            ArrayAdapter(requireContext(), R.layout.drop_down_item, viewModel.dayOfWeek)

        viewModel.arrayOfColorRes.forEachIndexed { index, _ ->
            val imageView = (binding.cardsColorItemsContainer.getChildAt(index) as ImageView)
            imageView.setOnClickListener {
                if (index != viewModel.selectedColorResPosition) {
                    changeCardColor(index)
                }
            }
        }

        binding.subjectStartTime.editText?.apply {
            isCursorVisible = false
            keyListener = null
            isClickable = true
            setOnClickListener { pickTime(EXTRA_START_DIALOG_TIME_PICKER) }
        }
        binding.subjectEndTime.editText?.apply {
            isCursorVisible = false
            keyListener = null
            setOnClickListener { pickTime(EXTRA_END_DIALOG_TIME_PICKER) }
        }


        viewModel.subjectLiveData.observe(viewLifecycleOwner, getObserverInitIfEditing())

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.subjectDay.setAdapter(dropDownAdaper)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val subjectId = args.argSubjectId

        if (subjectId == -1) {
            viewModel.isEditingLiveData.value = false
        } else {
            viewModel.isEditingLiveData.value = true
            viewModel.getSubjectByIdIfEditing(subjectId)
        }
        toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar?.apply {
            if (viewModel.isEditingLiveData.value!!)
                setTitle(R.string.edit_fragment)
            else setTitle(R.string.add_fragment)
            setBackgroundResource(R.color.primary)
            setNavigationIcon(R.drawable.ic_baseline_close_24)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_add_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save -> {
                if (saveSubject())
                    findNavController().popBackStack()
                true
            }
            else -> false
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun saveSubject(): Boolean {
        val isSubjectNameNotEmpty = validateSubjectName()
        val isSubjectDayNotEmpty = validateSubjectDay()
        val isSubjectStartTimeNotEmpty = validateSubjectStartTime()
        if (isSubjectNameNotEmpty and isSubjectDayNotEmpty and isSubjectStartTimeNotEmpty) {

            val id =
                if (viewModel.isEditingLiveData.value == true) viewModel.subjectLiveData.value?.id else null
            val subjectName = binding.subjectName.editText?.text.toString().trim()
            val subjectDay = binding.subjectDay.text.toString().trim()

            val subjectStartTime = binding.subjectStartTime.editText?.text.toString().trim() + ":00"
            var subjectEndTime = binding.subjectEndTime.editText?.text.toString().trim()
            val subjectPeriod =
                if (subjectEndTime.isNullOrEmpty()) {
                    SubjectPeriod(
                        Time.valueOf(subjectStartTime),
                        null
                    )
                } else {
                    subjectEndTime += ":00"
                    SubjectPeriod(Time.valueOf(subjectStartTime), Time.valueOf(subjectEndTime))
                }
            val subjectTeacher = binding.subjectTeacher.editText?.text.toString().trim()
            val subjectPlace = binding.subjectPlace.editText?.text.toString().trim()

            val subject = Subject(
                id = id,
                subjectName = subjectName,
                subjectPeriod = subjectPeriod,
                subjectTeacher = subjectTeacher,
                subjectPlace = subjectPlace,
                dayOfWeek = viewModel.dayOfWeek.indexOf(subjectDay),
                color = viewModel.arrayOfColorRes[viewModel.selectedColorResPosition]
            )
            viewModel.saveSubject(subject)
            return true
        } else return false
    }

    private fun changeCardColor(selectedPosition: Int) {
        (binding.cardsColorItemsContainer.getChildAt(viewModel.selectedColorResPosition) as ImageView)
            .setImageResource(R.drawable.ic_baseline_unchecked_24)
        (binding.cardsColorItemsContainer.getChildAt(selectedPosition) as ImageView)
            .setImageResource(R.drawable.ic_baseline_check_24)
        viewModel.selectedColorResPosition = selectedPosition

    }

    private fun pickTime(extra_dialog: Int) {
        val isSystem24hour = DateFormat.is24HourFormat(context)
        val format = if (isSystem24hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val picker = MaterialTimePicker.Builder()
            .setTitleText(R.string.dialog_pick_time)
            .setTimeFormat(format)
            .setHour(12)
            .setMinute(0)
            .build()
        picker.addOnDismissListener() {
            val hour = "${picker.hour}"
            val minutes = if (picker.minute == 0) "00" else "${picker.minute}"
            when (extra_dialog) {
                EXTRA_START_DIALOG_TIME_PICKER -> {
                    binding.subjectStartTime.editText?.setText("$hour:$minutes")
                }
                EXTRA_END_DIALOG_TIME_PICKER -> {
                    binding.subjectEndTime.editText?.setText("$hour:$minutes")
                }
            }
        }
        picker.show(childFragmentManager, "$extra_dialog")
    }

    private fun getObserverInitIfEditing() = Observer<Subject> {
        val isEditing = viewModel.isEditingLiveData.value

        if (isEditing == true) {
            binding.subjectName.editText?.setText(it.subjectName)

            binding.subjectDay.setText(resources.getStringArray(R.array.day_of_week)[it.dayOfWeek])
            binding.subjectDay.setAdapter(null)
            binding.subjectDay.setAdapter(dropDownAdaper)


            val indexOfSelectedColor = viewModel.arrayOfColorRes.indexOf(it.color)
            changeCardColor(indexOfSelectedColor)

            binding.subjectStartTime.editText?.setText(
                it.subjectPeriod.start.toString().substringBeforeLast(":")
            )

            if (it.subjectPeriod.end != null)
                binding.subjectEndTime.editText?.setText(
                    it.subjectPeriod.end.toString().substringBeforeLast(":")
                )

            if (!it.subjectTeacher.isNullOrEmpty())
                binding.subjectTeacher.editText?.setText(it.subjectTeacher)

            if (!it.subjectPlace.isNullOrEmpty())
                binding.subjectPlace.editText?.setText(it.subjectPlace)
        }
    }


    private fun validateSubjectName(): Boolean {
        val subjectName = binding.subjectName.editText?.text.toString().trim()
        return if (subjectName.isNullOrEmpty()) {
            binding.subjectName.error = resources.getString(R.string.error_empty)
            false
        } else {
            binding.subjectName.error = null
            true
        }
    }

    private fun validateSubjectDay(): Boolean {
        val subjectDay = binding.subjectDay.text.toString().trim()
        return if (subjectDay.isNullOrEmpty()) {
            binding.subjectDay.error = resources.getString(R.string.error_empty)
            false
        } else {
            binding.subjectDay.error = null
            true
        }
    }

    private fun validateSubjectStartTime(): Boolean {
        val subjectStartTime = binding.subjectStartTime.editText?.text.toString().trim()
        return if (subjectStartTime.isNullOrEmpty()) {
            binding.subjectStartTime.error = resources.getString(R.string.error_empty)
            false
        } else {
            binding.subjectStartTime.error = null
            true
        }
    }

}