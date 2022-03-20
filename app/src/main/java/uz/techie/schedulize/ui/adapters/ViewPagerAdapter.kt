package uz.techie.schedulize.ui.adapters

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.techie.schedulize.ui.models.DayOfWeek
import uz.techie.schedulize.ui.screens.list.CardListScreen

class ViewPagerAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    private val listDayOfWeek = mutableListOf<DayOfWeek>()

    fun setData(list: List<DayOfWeek>) {
        listDayOfWeek.clear()
        listDayOfWeek.addAll(list)
    }

    fun getListDayOfWeek(): List<DayOfWeek> = listDayOfWeek

    override fun getItemCount(): Int = listDayOfWeek.size

    override fun createFragment(position: Int) =
        CardListScreen.newInstance(listDayOfWeek[position])

}