package uz.techie.schedulize.ui.screens.cardview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import uz.techie.schedulize.R
import uz.techie.schedulize.databinding.ScreenCardDetailsBinding
import uz.techie.schedulize.utils.extentions.doOnApplyWindowInsets
import uz.techie.schedulize.utils.extentions.statusBarsInsets

@AndroidEntryPoint
class CardDetailScreen : Fragment() {
    private val TAG = CardDetailScreen::class.java.canonicalName

    private val args: CardDetailScreenArgs by navArgs()
    private val screenViewModel: CardViewScreenViewModel by viewModels()

    private var _binding: ScreenCardDetailsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenCardDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setClickListeners()
        observe()

        val id = args.argSubjectId
        screenViewModel.loadData(id)

        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW){
            param(FirebaseAnalytics.Param.SCREEN_NAME,TAG)
        }

        binding.root.doOnApplyWindowInsets { windowInsets ->
            updatePadding(top = windowInsets.statusBarsInsets.top)
            windowInsets
        }

        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)

        sharedElementReturnTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)

    }

    private fun observe() {
        lifecycleScope.launchWhenStarted {
            screenViewModel.subject.collect { subject ->
                binding.apply {
                    rootLayout.setBackgroundResource(subject.color.colorRes)
                    toolbar.setBackgroundResource(subject.color.colorRes)

                    subjectName.text = subject.subjectName
                    subjectDay.text = getString(subject.dayOfWeek.dayRes)
                    subjectPeriod.text = subject.subjectPeriod.toString()
                    subjectTeacher.text = subject.subjectTeacher
                    subjectPlace.text = subject.subjectPlace

                    teacherContainer.isVisible = subject.subjectTeacher.isNotEmpty()
                    placeContainer.isVisible = subject.subjectPlace.isNotEmpty()
                }
            }
        }
    }

    private fun setClickListeners() {
        binding.toolbar.apply {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.edit_subject -> {
                        editSubjectScreen()
                        true
                    }
                    R.id.delete_subject -> {
                        requireDelete()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun editSubjectScreen() {
        val id = screenViewModel.subject.value.id
        val action =
            CardDetailScreenDirections.actionCardViewScreenToAddScreen(id!!)
        Log.e(TAG, "editSubjectScreen: ${action.actionId}, ${action.arguments}", )
        findNavController().navigate(action)
    }

    private fun requireDelete() {
        screenViewModel.deleteSubject()
        findNavController().popBackStack()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}