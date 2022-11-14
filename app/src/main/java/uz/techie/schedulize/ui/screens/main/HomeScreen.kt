package uz.techie.schedulize.ui.screens.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import uz.techie.schedulize.R
import uz.techie.schedulize.databinding.ScreenHomeBinding
import uz.techie.schedulize.ui.adapters.ViewPagerAdapter
import uz.techie.schedulize.ui.models.DayOfWeek
import uz.techie.schedulize.utils.extentions.doOnApplyWindowInsets
import uz.techie.schedulize.utils.extentions.statusBarsInsets

@AndroidEntryPoint
class HomeScreen : Fragment(R.layout.screen_home) {
    private val TAG = HomeScreen::class.java.canonicalName

    private val viewModel: HomeScreenViewModel by viewModels()

    private val binding by viewBinding<ScreenHomeBinding>()

    private lateinit var adapter: ViewPagerAdapter
    private var tabLayoutMediator: TabLayoutMediator? = null

    private var days = emptyList<DayOfWeek>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initClicks()
        initObservers()
        setDataToView()
    }

    private fun initView() {
        binding.root.doOnApplyWindowInsets { windowInsets ->
            updatePadding(top = windowInsets.statusBarsInsets.top)
            windowInsets
        }

        adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 2

        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW){
            param(FirebaseAnalytics.Param.SCREEN_NAME,TAG)
        }
    }


    private fun initObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.listOfDayOfWeek.collect {
                days = it
                setDataToView()
            }
        }
    }

    private fun setDataToView() {
        if (days.isEmpty()) {
            binding.groupViewpager.isVisible = false
            binding.textWhenEmpty.isVisible = true
        } else {
            binding.groupViewpager.isVisible = true
            binding.textWhenEmpty.isVisible = false
        }
        adapter.setData(days)

        tabLayoutMediator?.detach()
        tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = getString(days[position].dayRes)
            }
        tabLayoutMediator?.attach()
    }

    private fun initClicks() {
        binding.toolbar.apply {
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.add_subject -> {
                        findNavController().navigate(R.id.action_fragmentMain_to_addFragment)
                        true
                    }
                    R.id.setting -> {
                        findNavController().navigate(R.id.action_fragmentMain_to_settingFragment)
                        true
                    }
                    else -> false
                }
            }
        }
    }
}