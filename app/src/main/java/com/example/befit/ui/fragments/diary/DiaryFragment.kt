package com.example.befit.ui.fragments.diary

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.befit.R
import com.example.befit.databinding.CalendarDayLayoutBinding
import com.example.befit.databinding.FragmentDiaryBinding
import com.example.befit.repository.ApiRepository
import com.example.befit.util.Utility.swipeToWeekMode
import com.example.befit.viewmodel.auth.AuthViewModel
import com.example.befit.viewmodel.auth.AuthViewModelFactory
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.model.InDateStyle
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.yearMonth
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

class DiaryFragment : Fragment() {

    private var _binding: FragmentDiaryBinding? = null
    private val binding: FragmentDiaryBinding get() = _binding!!
    private lateinit var viewModel: AuthViewModel

    private val today = LocalDate.now()
    private var selectedDate: LocalDate = today

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        (activity as MainActivity).btm_nav.visibility = View.VISIBLE
        (activity as AppCompatActivity).supportActionBar?.hide()
        setHasOptionsMenu(true)
        _binding = FragmentDiaryBinding.inflate(inflater, container, false)

        val repository = ApiRepository()
        val viewModelFactory = AuthViewModelFactory(
            PreferenceManager.getDefaultSharedPreferences(requireActivity()),
            repository
        )
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[AuthViewModel::class.java]

        createCalendar()
        setProgressBar()

        binding.logoutBtn.setOnClickListener {
            logout()
        }

        return binding.root
    }

    private fun setProgressBar() {
        binding.caloriesProgressBar.max = 1651
        binding.caloriesProgressBar.progress = 1200

        binding.proteinsProgressBar.max = 128
        binding.proteinsProgressBar.progress = 67

        binding.fatsProgressBar.max = 412
        binding.fatsProgressBar.progress = 300

        binding.carbohydratesProgressBar.max = 162
        binding.carbohydratesProgressBar.progress = 120
    }

    override fun onResume() {
        super.onResume()
        checkAuthorizationUser()
    }

    private fun setGreeting(name: String) {
        val greeting =
            when (LocalDateTime.now().hour) {
                in 5..12 -> getString(R.string.morning, name)
                in 13..17 -> getString(R.string.afternoon, name)
                in 18..22 -> getString(R.string.evening, name)
                else -> {getString(R.string.night, name)}
            }
        binding.greetingTv.text = greeting
    }

    private fun checkAuthorizationUser() {
        viewModel.getUser(requireContext())
        viewModel.getResponseUser().observe(viewLifecycleOwner, Observer { response ->
            setGreeting(response.first_name)
        })
        viewModel.getErrorUser().observe(viewLifecycleOwner) {
            if (it != "") {
                if (it == "Unauthorized")
                    findNavController().navigate(R.id.action_diaryFragment_to_welcomeFragment)
                else
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logout() {
        viewModel.logout(requireContext())
        viewModel.getResponseLogout().observe(viewLifecycleOwner, Observer {
            findNavController().navigate(R.id.action_diaryFragment_to_welcomeFragment)
        })
        viewModel.getErrorLogout().observe(viewLifecycleOwner, Observer {
            if (it != "") Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    fun createCalendar() {
        binding.calendarView.updateMonthConfiguration(
            inDateStyle = InDateStyle.FIRST_MONTH,
            maxRowCount = 1,
            hasBoundaries = false
        )
        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = DayOfWeek.MONDAY
        binding.calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        binding.calendarView.scrollToDate(selectedDate)

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val dayLayout = CalendarDayLayoutBinding.bind(view).dayLayout
            val textView = CalendarDayLayoutBinding.bind(view).dayText

            init {
                textView.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDate != day.date) {
                            val oldDate = selectedDate
                            selectedDate = day.date
                            binding.calendarView.notifyDateChanged(day.date)
                            oldDate.let { binding.calendarView.notifyDateChanged(oldDate) }
                        }
                    }
//                    getNotesToDate(selectedDate.toString())
                }
            }
        }

        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.setTextColor(resources.getColor(R.color.gray))

                    when (day.date) {
                        selectedDate -> {
                            textView.setTextColor(resources.getColor(R.color.gray))
                            container.dayLayout.setBackgroundResource(R.drawable.bg_white_10)
                        }
                        today -> {
                            textView.setTextColor(resources.getColor(R.color.dark_orange))
                            container.dayLayout.background = null
                        }
                        else -> {
                            textView.setTextColor(resources.getColor(R.color.gray))
                            container.dayLayout.background = null
                        }
                    }
                } else {
                    textView.setTextColor(resources.getColor(R.color.gray_50))
                    container.dayLayout.background = null
                }
            }
        }

        binding.calendarView.monthScrollListener = {
            binding.currentMonth.text = resources.getStringArray(R.array.month)[it.month - 1]
            binding.currentYear.text = it.year.toString()

            if (binding.calendarView.maxRowCount == 6) {
                binding.currentMonth.text = resources.getStringArray(R.array.month)[it.month - 1]
                binding.currentYear.text = it.yearMonth.year.toString()
            } else {
                val firstDate = it.weekDays.first().first().date
                val lastDate = it.weekDays.last().last().date
                if (firstDate.yearMonth == lastDate.yearMonth) {
                    binding.currentMonth.text =
                        resources.getStringArray(R.array.month)[firstDate.monthValue - 1]
                    binding.currentYear.text = firstDate.yearMonth.year.toString()
                } else {
                    binding.currentMonth.text =
                        "${resources.getStringArray(R.array.month)[firstDate.monthValue - 1]} - ${
                            resources.getStringArray(R.array.month)[lastDate.monthValue - 1]
                        }"
                    if (firstDate.year == lastDate.year) {
                        binding.currentYear.text = firstDate.yearMonth.year.toString()
                    } else {
                        binding.currentYear.text =
                            "${firstDate.yearMonth.year} - ${lastDate.yearMonth.year}"
                    }
                }
            }
        }

        binding.diaryLayout.setOnTouchListener(object : OnSwipeTouchListener(requireContext()) {
            override fun onSwipeTop() {
                if (binding.calendarView.maxRowCount == 6)
                    swipeToWeekMode(binding.calendarView, true, selectedDate)
            }

            override fun onSwipeBottom() {
                if (binding.calendarView.maxRowCount == 1) {
                    binding.currentMonth.text =
                        resources.getStringArray(R.array.month)[selectedDate.monthValue - 1]
                    swipeToWeekMode(binding.calendarView, false, selectedDate)
                    binding.currentMonth.layoutParams.width = ConstraintLayout.LayoutParams.WRAP_CONTENT
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}