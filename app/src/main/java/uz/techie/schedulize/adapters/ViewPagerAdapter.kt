package uz.techie.schedulize.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.techie.schedulize.ui.list.CardListFragment

class ViewPagerAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {

    private val listDayOfWeek = mutableListOf<Int>()


    fun setListDayOfWeek(list:List<Int>){
        listDayOfWeek.clear()
        listDayOfWeek.addAll(list)
    }

    fun getListDayOfWeek():List<Int> = listDayOfWeek

    override fun getItemCount(): Int = listDayOfWeek.size

    override fun createFragment(position: Int) = CardListFragment.newInstance(listDayOfWeek[position])

}