package uz.techie.schedulize.ui

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import uz.techie.schedulize.BuildConfig
import uz.techie.schedulize.R
import uz.techie.schedulize.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPref: SharedPreferences

    private val themeModes =
        listOf(
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
            AppCompatDelegate.MODE_NIGHT_NO,
            AppCompatDelegate.MODE_NIGHT_YES
        )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext())

        binding.toolbar.apply {
            setTitle(R.string.setting_fragment)
            setBackgroundResource(R.color.primary)
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            setNavigationOnClickListener {
                findNavController().navigate(R.id.action_settingFragment_to_fragmentMain)
            }
        }
        val localLanguage =
            (requireActivity() as LocalizationActivity).getCurrentLanguage().language
        val localItemsPosition =
            resources.getStringArray(R.array.languages_values).indexOf(localLanguage)

        binding.languageSummary.text =
            resources.getStringArray(R.array.languages_entries)[localItemsPosition]

        var currentThemeMode:Int = sharedPref.getInt("theme",0)
        binding.themeSummary.setText(resources.getStringArray(R.array.theme_entries)[currentThemeMode])





        binding.versionEntities.text = BuildConfig.VERSION_NAME

        binding.telegramLink.setOnClickListener {
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse("http://t.me/matjon")
            startActivity(intent)
        }

        binding.language.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.language_title)
                .setSingleChoiceItems(
                    resources.getStringArray(R.array.languages_entries),
                    localItemsPosition,
                    DialogInterface.OnClickListener { dialog, which ->
                        (requireActivity() as LocalizationActivity).setLanguage(
                            resources.getStringArray(
                                R.array.languages_values
                            )[which]
                        )
                        binding.languageSummary.text =
                            resources.getStringArray(R.array.languages_entries)[which]
                        dialog.dismiss()
                    }
                )
                .setNegativeButton(R.string.dialog_cancel) { dialog, which ->
                    dialog.cancel()
                }.show()
        }
        binding.theme.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.theme_title)
                .setSingleChoiceItems(
                    resources.getStringArray(R.array.theme_entries),
                    currentThemeMode,
                    DialogInterface.OnClickListener { dialog, which ->

                        val theme = themeModes[which]

                        Log.d("Setting", "$which ${resources.getStringArray(R.array.theme_entries)[which]}")
                        AppCompatDelegate.setDefaultNightMode(theme)

                        sharedPref.edit().putInt("theme",which).commit()
                        currentThemeMode = which
                        binding.themeSummary.text =
                            resources.getStringArray(R.array.theme_entries)[which]
                        dialog.dismiss()
                    }
                )
                .setNegativeButton(R.string.dialog_cancel) { dialog, which ->
                    dialog.cancel()
                }
                .show()
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}