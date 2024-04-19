package uz.gita.bootcamp.abdulbosit.apps.screen.main

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.gita.bootcamp.abdulbosit.apps.R
import uz.gita.bootcamp.abdulbosit.apps.data.MyPref
import uz.gita.bootcamp.abdulbosit.apps.databinding.ScreenMainBinding

class MainScreen : Fragment() {
    private lateinit var dialog: Dialog
    private var _binding: ScreenMainBinding? = null
    private val binding get() = _binding!!
    private val pref = MyPref

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = Dialog(requireContext())
        checkSwitch()
    }

    private fun checkSwitch() {
        binding.batterySwitch.isChecked = pref.getTag("battery")
        binding.bluetoothSwitch.isChecked = pref.getTag("bluetooth")
        binding.screenSwitch.isChecked = pref.getTag("screen")
        binding.pilotSwitch.isChecked = pref.getTag("pilot")
        binding.internetSwitch.isChecked = pref.getTag("internet")

        binding.batterySwitch.setOnCheckedChangeListener { _, checked ->
            pref.setTag("battery", checked)
        }

        binding.internetSwitch.setOnCheckedChangeListener { _, checked ->
            pref.setTag("internet", checked)
        }

        binding.pilotSwitch.setOnCheckedChangeListener { _, checked ->
            pref.setTag("pilot", checked)
        }

        binding.screenSwitch.setOnCheckedChangeListener { _, checked ->
            pref.setTag("screen", checked)
        }

        binding.bluetoothSwitch.setOnCheckedChangeListener { _, checked ->
            pref.setTag("bluetooth", checked)
        }

        binding.wifiSwitch.setOnCheckedChangeListener { _, checked ->
            pref.setTag("wifi", checked)
        }

        binding.hotspotSwitch.setOnCheckedChangeListener { _, checked ->
            pref.setTag("mobile_data", checked)
        }

        binding.timezoneSwitch.setOnCheckedChangeListener { _, checked ->
            pref.setTag("time_zone", checked)
        }

        binding.unlockSwitch.setOnCheckedChangeListener { _, checked ->
            pref.setTag("unlock", checked)
        }

        binding.shutdownSwitch.setOnCheckedChangeListener { _, checked ->
            pref.setTag("shutdown", checked)
        }

        binding.languageSwitch.setOnCheckedChangeListener { _, checked ->
            pref.setTag("language", checked)
        }

        binding.more.setOnClickListener {
            showBottomSheetDialog()
        }
    }

    private fun showBottomSheetDialog() {

        dialog.setContentView(R.layout.dialog_edit_delete)


        dialog.findViewById<TextView>(R.id.share).setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val shareBody =
                "Event App ilovasini bu yerdan yuklab olishingiz mumkin " + "https://play.google.com/store/apps/details?id=org.telegram.messenger"
            intent.setType("text/plain")
            intent.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(androidx.appcompat.R.string.abc_action_bar_home_description)
            )
            intent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(intent, getString(R.string.app_name)))
        }
        dialog.findViewById<TextView>(R.id.feedback).setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val recipients = arrayOf("abdulbosit9730@gmail.com")
            intent.putExtra(Intent.EXTRA_EMAIL, recipients)
            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject text here...")
            intent.putExtra(Intent.EXTRA_TEXT, "Body of the content here...")
            intent.putExtra(Intent.EXTRA_CC, "mailcc@gmail.com")
            intent.setType("text/html")
            intent.setPackage("com.google.android.gm")
            startActivity(Intent.createChooser(intent, "Send mail"))
        }
        dialog.findViewById<TextView>(R.id.rate).setOnClickListener {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=${requireContext().packageName}")
                    )
                )
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=${requireContext().packageName}")
                    )
                )
            }

        }

        dialog.findViewById<TextView>(R.id.about).setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(MainScreenDirections.actionMainScreenToInfoScreen())
        }


        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}