package uz.techie.schedulize.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import uz.techie.schedulize.R
import uz.techie.schedulize.models.Subject

class CardRecyclerAdapter(private val resources: Resources) :
    RecyclerView.Adapter<CardRecyclerAdapter.CardHolder>() {
    private lateinit var itemClickListener: (position: Int) -> Unit
    private val subjects = mutableListOf<Subject>()

    fun setSubjects(_subjects: List<Subject>) {
        subjects.clear()
        subjects.addAll(_subjects)
        notifyDataSetChanged()
    }


    inner class CardHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        //region init
        private val root = itemView.findViewById<LinearLayout>(R.id.root)
        private val subjectNameTextView = itemView.findViewById<TextView>(R.id.subject_name)
        private val subjectPeriodTextView = itemView.findViewById<TextView>(R.id.subject_period)
        private val subjectTeacherTextView = itemView.findViewById<TextView>(R.id.subject_teacher)
        private val subjectPlaceTextView = itemView.findViewById<TextView>(R.id.subject_place)
        //endregion

        fun binding(subject: Subject) {
            itemView.setOnClickListener { itemClickListener.invoke(adapterPosition) }

            if (subject.color != R.color.card_color_default)
                root.backgroundTintList = resources.getColorStateList(subject.color)
            root.setBackgroundResource(R.drawable.cart_item_background)

            subjectNameTextView.text = subject.subjectName
            subjectPeriodTextView.text = subject.subjectPeriod.toString()

            if (subject.subjectTeacher.isNullOrEmpty())
                subjectTeacherTextView.visibility = View.GONE
            else subjectTeacherTextView.text = subject.subjectTeacher

            if (subject.subjectPlace.isNullOrEmpty())
                subjectPlaceTextView.visibility = View.GONE
            else subjectPlaceTextView.text = subject.subjectPlace
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

    fun setOnItemClickListener(l: (position: Int) -> Unit) {
        itemClickListener = l
    }
}