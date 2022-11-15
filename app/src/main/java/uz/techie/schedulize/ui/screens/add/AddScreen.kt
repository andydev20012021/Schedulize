package uz.techie.schedulize.ui.screens.add

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import uz.techie.schedulize.R
import uz.techie.schedulize.databinding.ScreenAddBinding
import uz.techie.schedulize.ui.adapters.CardColorPickAdapter
import uz.techie.schedulize.ui.models.DayOfWeek
import uz.techie.schedulize.ui.models.SubjectModel
import uz.techie.schedulize.ui.models.SubjectPeriod
import uz.techie.schedulize.utils.extentions.doOnApplyWindowInsets
import uz.techie.schedulize.utils.extentions.systemBarsInsets
import java.sql.Time

@AndroidEntryPoint
class AddScreen : Fragment(R.layout.screen_add) {

    private val viewModel: AddScreenViewModel by viewModels()

    private val args: AddScreenArgs by navArgs()

    private val binding by viewBinding<ScreenAddBinding>()

    private lateinit var dropDownAdapter: ArrayAdapter<String>
    private lateinit var colorPickAdapter: CardColorPickAdapter
    private var selectedDayOfWeek: DayOfWeek? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: ")
        initView()
        initClick()
    }

    override fun onResume() {
        super.onResume()
        initAdapter()
    }

    private fun observe() {
        lifecycleScope.launchWhenStarted {
            viewModel.subject.collect {
                binding.apply {
                    subjectName.editText?.setText(it.subjectName)
                    subjectDay.setText(it.dayOfWeek.dayRes)
                    subjectStartTime.editText?.setText(it.subjectPeriod.start())
                    subjectEndTime.editText?.setText(it.subjectPeriod.end())
                    subjectPlace.editText?.setText(it.subjectPlace)
                    subjectTeacher.editText?.setText(it.subjectTeacher)
                    colorPickAdapter.selectColor(it.color)
                }
                selectedDayOfWeek = it.dayOfWeek
            }
        }
    }

    private fun initView() {
        val subjectId = args.argSubjectId
        if (subjectId == -1) {
            viewModel.isEditing = false
        } else {
            viewModel.isEditing = true
            viewModel.getSubjectByIdIfEditing(subjectId)
            observe()
        }

        colorPickAdapter = CardColorPickAdapter()
        binding.colorRecycler.adapter = colorPickAdapter
        
        binding.apply {
            root.doOnApplyWindowInsets { windowInsets ->
                updatePadding(
                    top = windowInsets.systemBarsInsets.top,
                    bottom = windowInsets.systemBarsInsets.bottom
                )
                WindowInsetsCompat.CONSUMED
            }
            toolbar.apply {
                if (viewModel.isEditing) setTitle(R.string.edit_fragment)
                else setTitle(R.string.add_fragment)
            }
        }

        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, TAG)
        }
    }

    private fun initAdapter() {
        dropDownAdapter =
            ArrayAdapter(
                requireContext(),
                R.layout.item_drop_down,
                DayOfWeek.values().map { getString(it.dayRes) })
        binding.subjectDay.setAdapter(dropDownAdapter)
        binding.subjectDay.setOnItemClickListener { parent, view, position, id ->
            selectedDayOfWeek = DayOfWeek.values()[position]
        }
    }

    private fun initClick() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.save -> {
                        checkField()
                        true
                    }
                    else -> false
                }
            }

            subjectStartTime.editText?.apply {
                keyListener = null
                setOnClickListener { pickTime(EXTRA_START_DIALOG_TIME_PICKER) }
            }

            subjectEndTime.editText?.apply {
                keyListener = null
                setOnClickListener { pickTime(EXTRA_END_DIALOG_TIME_PICKER) }
            }
        }
    }

    private fun checkField() {
        binding.apply {
            if (subjectName.editText?.text.isNullOrBlank()) {
                subjectName.error = getString(R.string.error_empty)
                return@apply
            }
            if (subjectDay.text.isNullOrBlank()) {
                subjectDay.error = getString(R.string.error_empty)
                return
            }
            if (subjectStartTime.editText?.text.isNullOrBlank()) {
                subjectStartTime.error = getString(R.string.error_empty)
                return
            }
            saveSubject()
        }
    }

    private fun saveSubject() {
        val id = viewModel.subjectId
        val subjectName = binding.subjectName.editText?.text.toString().trim()
        val subjectStartTime =
            Time.valueOf(binding.subjectStartTime.editText?.text.toString().trim() + ":00")
        var subjectEndTime =
            if (binding.subjectEndTime.editText?.text.toString()
                    .isNullOrBlank()
            ) Time.valueOf("00:00:00")
            else Time.valueOf(binding.subjectEndTime.editText?.text.toString() + ":00")

        val subjectPeriod = SubjectPeriod(subjectStartTime, subjectEndTime)
        val subjectTeacher = binding.subjectTeacher.editText?.text.toString().trim()
        val subjectPlace = binding.subjectPlace.editText?.text.toString().trim()

        val subject = SubjectModel(
            id = id,
            subjectName = subjectName,
            subjectPeriod = subjectPeriod,
            subjectPlace = subjectPlace,
            subjectTeacher = subjectTeacher,
            color = colorPickAdapter.selectedColor,
            dayOfWeek = selectedDayOfWeek!!
        )
        viewModel.saveSubject(subject)

        Firebase.analytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART) {
            param(FirebaseAnalytics.Param.ITEM_ID, (subject.id ?: -1).toLong())
            param("save type", if (viewModel.isEditing) "edit subject" else "add subject")
        }

        findNavController().popBackStack()
    }


    private fun pickTime(extra_dialog: Int) {
        val format = TimeFormat.CLOCK_24H

        val picker = MaterialTimePicker.Builder()
            .setTitleText(R.string.dialog_pick_time)
            .setTimeFormat(format)
            .setHour(12)
            .setMinute(0)
            .build()
        picker.addOnPositiveButtonClickListener() {
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

    companion object {
        const val TAG = "AddScreen"
        private val EXTRA_START_DIALOG_TIME_PICKER = 0
        private val EXTRA_END_DIALOG_TIME_PICKER = 1
    }
}