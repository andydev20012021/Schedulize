package uz.techie.schedulize

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import uz.techie.schedulize.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {

    private var _binding:FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var toolbar:Toolbar


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.apply {
            setTitle(R.string.setting_fragment)
            setBackgroundResource(R.color.primary)
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            setNavigationOnClickListener { findNavController().popBackStack() }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater,container,false)
        binding.versionEntities.text = BuildConfig.VERSION_NAME
        binding.telegramLink.setOnClickListener {
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse("http://t.me/matjon")
            startActivity(intent)
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}