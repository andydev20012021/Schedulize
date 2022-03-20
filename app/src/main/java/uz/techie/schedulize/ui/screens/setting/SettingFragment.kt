package uz.techie.schedulize.ui.screens.setting

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import uz.techie.schedulize.BuildConfig
import uz.techie.schedulize.MainActivity
import uz.techie.schedulize.R
import uz.techie.schedulize.databinding.ScreenSettingsBinding
import uz.techie.schedulize.ui.screens.setting.SettingViewModel.FileResult
import uz.techie.schedulize.utils.Constants
import uz.techie.schedulize.utils.LanguageUtils
import uz.techie.schedulize.utils.ThemeUtil
import uz.techie.schedulize.utils.extentions.doOnApplyWindowInsets
import uz.techie.schedulize.utils.extentions.gone
import uz.techie.schedulize.utils.extentions.systemBarsInsets
import uz.techie.schedulize.utils.extentions.visible

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private val viewModel by viewModels<SettingViewModel>()

    private var _binding: ScreenSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPref: SharedPreferences

    private fun getCurrentLanguage(): LanguageUtils {
        val localCode = (requireActivity() as LocalizationActivity).getCurrentLanguage().language
        return LanguageUtils.findByCode(localCode)
    }

    private fun getCurrentTheme(): ThemeUtil {
        val themeId = sharedPref.getInt(ThemeUtil.name, ThemeUtil.MODE_NIGHT_FOLLOW_SYSTEM.id)
        return ThemeUtil.findById(themeId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ScreenSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initData()
        observe()
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW){
            param(FirebaseAnalytics.Param.SCREEN_NAME, TAG)
        }

        binding.apply {
            root.doOnApplyWindowInsets { windowInsets ->
                updatePadding(top = windowInsets.systemBarsInsets.top, bottom = windowInsets.systemBarsInsets.bottom)
                windowInsets
            }
            toolbar.apply {
                setNavigationOnClickListener {
                    findNavController().popBackStack()
                }
            }

            notification.setOnClickListener { setNotifications() }
            language.setOnClickListener { setAppLanguage() }
            theme.setOnClickListener { setAppTheme() }
            exportData.setOnClickListener { exportData() }
            importData.setOnClickListener { importData() }
            telegramLink.setOnClickListener { openAppTelegramLink(Constants.APP_TELEGRAM_URL) }

        }
    }

    private fun initData() {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext())

        binding.themeSummary.setText(getCurrentTheme().themeName)
        binding.languageSummary.setText(getCurrentLanguage().langName)
        binding.versionEntities.text = BuildConfig.VERSION_NAME
    }

    private fun setNotifications() {
        val intent = Intent()
        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        //for android 5-7
        intent.putExtra("app_package", requireContext().packageName)
        intent.putExtra("app_uid", requireContext().applicationInfo.uid)

        //for android 8 and above
        intent.putExtra("android.provider.extra.APP_PACKAGE", requireContext().packageName)

        startActivity(intent)
    }

    private fun setAppLanguage() {
        val languages = LanguageUtils.values()
        val builder = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.language_title)
            .setSingleChoiceItems(
                languages.map { getString(it.langName) }.toTypedArray(),
                getCurrentLanguage().id,
                DialogInterface.OnClickListener { dialog, which ->
                    val selectedLang = languages[which]
                    (requireActivity() as LocalizationActivity).setLanguage(selectedLang.code)
                    binding.languageSummary.setText(selectedLang.langName)
                    Firebase.analytics.logEvent("setLang"){
                        param("lang",selectedLang.code)
                    }
                    dialog.dismiss()
                }
            )
            .setNegativeButton(R.string.dialog_cancel) { dialog, which ->
                dialog.cancel()
            }.show()
    }

    private fun setAppTheme() {
        val themes = ThemeUtil.values()

        val builder = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.theme_title)
            .setSingleChoiceItems(
                themes.map { getString(it.themeName) }.toTypedArray(),
                getCurrentTheme().id,
                DialogInterface.OnClickListener { dialog, which ->
                    val theme = themes[which]

                    AppCompatDelegate.setDefaultNightMode(theme.mode)
                    sharedPref.edit().putInt(ThemeUtil.name, which).apply()
                    Firebase.analytics.logEvent("setTheme"){
                        param("theme",theme.mode.toLong())
                    }
                    binding.themeSummary.setText(theme.themeName)
                    dialog.dismiss()
                }
            )
            .setNegativeButton(R.string.dialog_cancel) { dialog, which ->
                dialog.cancel()
            }
            .show()
    }

    private fun exportData() {
        val intent = Intent().apply {
            action = Intent.ACTION_CREATE_DOCUMENT
            type = "text/*"
            putExtra(Intent.EXTRA_TITLE, "back_up.sch")
        }

        startActivityForResult(intent, CREATE_FILE)
    }

    private fun importData() {
        val intent = Intent().apply {
            action = Intent.ACTION_OPEN_DOCUMENT
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/*"
        }
        startActivityForResult(intent, OPEN_FILE)
    }

    private fun openAppTelegramLink(uri: Uri) {
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        intent.data = uri
        Firebase.analytics.logEvent("open_tg") {}
        startActivity(intent)
    }

    private fun observe(){
        lifecycleScope.launch {
            viewModel.processData.collect {
                MainActivity.isBackPressable = true
                when(it){
                    is FileResult.IDLE -> {}
                    is FileResult.Error -> {
                        binding.showProgress.gone()
                        Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                    }
                    is FileResult.Loading -> {
                        MainActivity.isBackPressable = false
                        binding.showProgress.visible()
                    }
                    is FileResult.Success -> {
                        binding.showProgress.gone()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CREATE_FILE -> {
                    viewModel.exportData(data?.data)
                }
                OPEN_FILE -> {
                    viewModel.importData(data?.data)
                }
            }
        } else {
            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val CREATE_FILE = 111
        const val OPEN_FILE = 222
        const val TAG = "SettingFragment"
    }
}