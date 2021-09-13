package uz.techie.schedulize.adapters

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import uz.techie.schedulize.R
import uz.techie.schedulize.models.Subject

class CardRecyclerAdapter(private val resources: Resources) :
    RecyclerView.Adapter<CardRecyclerAdapter.CardHolder>() {
    private lateinit var itemClickListener: (position: Int,extras: Navigator.Extras) -> Unit
    private val subjects = mutableListOf<Subject>()

    fun setSubjects(_subjects: List<Subject>) {
        subjects.clear()
        subjects.addAll(_subjects)
        notifyDataSetChanged()
    }


    inner class CardHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        //region init
        private val root = itemView.findViewById<LinearLayout>(R.id.rootLayout)
        private val subjectNameTextView = itemView.findViewById<TextView>(R.id.subject_name)
        private val subjectPeriodTextView = itemView.findViewById<TextView>(R.id.subject_period)
        private val subjectTeacherTextView = itemView.findViewById<TextView>(R.id.subject_teacher)
        private val subjectPlaceTextView = itemView.findViewById<TextView>(R.id.subject_place)

        //endregion

        fun binding(subject: Subject) {
            ViewCompat.setTransitionName(root,"root${subject.id}")
            ViewCompat.setTransitionName(subjectNameTextView,"nametextView${subject.id}")
            ViewCompat.setTransitionName(subjectPeriodTextView,"period${subject.id}")
            ViewCompat.setTransitionName(subjectTeacherTextView,"teacher${subject.id}")
            ViewCompat.setTransitionName(subjectPlaceTextView,"place${subject.id}")


            if (subject.color != R.color.card_color_default)
                root.backgroundTintList = resources.getColorStateList(subject.color)
            root.setBackgroundResource(R.drawable.cart_item_background)

            subjectNameTextView.text = subject.subjectName
            subjectPeriodTextView.text = subject.subjectPeriod.toString()

            if (subject.subjectTeacher.isNullOrEmpty()) {
                subjectTeacherTextView.visibility = View.GONE
            } else {

                subjectTeacherTextView.visibility = View.VISIBLE
                subjectTeacherTextView.text = subject.subjectTeacher
            }

            if (subject.subjectPlace.isNullOrEmpty()) {
                subjectPlaceTextView.visibility = View.GONE
            } else {
                subjectPlaceTextView.visibility = View.VISIBLE
                subjectPlaceTextView.text = subject.subjectPlace
            }

            itemView.setOnClickListener {
                val extras = FragmentNavigatorExtras(
                    root to "root",
                    subjectNameTextView to "subject_name",
                    subjectPeriodTextView to "subject_period",
                    subjectTeacherTextView to "subject_teacher",
                    subjectPlaceTextView to "subject_place"
                )
                itemClickListener.invoke(adapterPosition,extras)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val binding = LayoutInflater.from(parent.context).inflate(
            R.layout.card_item,
            parent,
            false
        )
        return CardHolder(binding)

    }

    override fun getItemCount(): Int = subjects.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.binding(subjects[position])
    }

    override fun getItemId(position: Int): Long {
        return subjects[position].id!!.toLong()
    }

    fun setOnItemClickListener(l: (position: Int,extras:Navigator.Extras) -> Unit) {
        itemClickListener = l
    }
}