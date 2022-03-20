package uz.techie.schedulize.ui.adapters

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import uz.techie.schedulize.R
import uz.techie.schedulize.databinding.ItemCardBinding
import uz.techie.schedulize.ui.models.CardColor
import uz.techie.schedulize.ui.models.SubjectModel
import uz.techie.schedulize.utils.extentions.dp

class CardRecyclerAdapter :
    RecyclerView.Adapter<CardRecyclerAdapter.CardHolder>() {
    var itemClickListener: ((subject: SubjectModel, extras: Navigator.Extras) -> Unit)? = null
    private val subjects = mutableListOf<SubjectModel>()

    fun setSubjects(_subjects: List<SubjectModel>) {
        subjects.clear()
        subjects.addAll(_subjects)
        notifyDataSetChanged()
    }

    inner class CardHolder(private val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun binding(subject: SubjectModel) {
            setTransitionName(subject.id!!)
            binding.apply {
                rootLayout.updateLayoutMargin()
                rootLayout.background = getBackgroundDrawable(subject)
                subjectName.text = subject.subjectName
                period.text = subject.subjectPeriod.toString()
                place.text = subject.subjectPlace
                teacherName.text = subject.subjectTeacher

                teacherName.isVisible = subject.subjectTeacher.isNotEmpty()
                place.isVisible = subject.subjectPlace.isNotEmpty()

                itemView.setOnClickListener {
                    val extras = FragmentNavigatorExtras(
                        rootLayout to "root",
                        subjectName to "subject_name",
                        period to "subject_period",
                        teacherName to "subject_teacher",
                        place to "subject_place"
                    )
                    itemClickListener?.invoke(subject, extras)
                }
            }
        }

        private fun getBackgroundDrawable(subject: SubjectModel): Drawable {
            return GradientDrawable().apply {
                val context = binding.root.context
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 4f.dp

                if (subject.color == CardColor.DEFAULT)
                    setStroke(1.dp, context.resources.getColor(R.color.on_surface_400))
                setColor(context.resources.getColor(subject.color.colorRes))

            }
        }

        private fun setTransitionName(id: Int) {
            binding.apply {
                ViewCompat.setTransitionName(rootLayout, "root$id")
                ViewCompat.setTransitionName(subjectName, "nametextView$id")
                ViewCompat.setTransitionName(period, "period$id")
                ViewCompat.setTransitionName(teacherName, "teacher$id")
                ViewCompat.setTransitionName(place, "place$id")
            }
        }

        private fun View.updateLayoutMargin() {
            val marginLeft = 16.dp
            val marginRight = 16.dp
            val spaceBetween = 16.dp
            val marginFirstAndLast = 12.dp

            val isFirst = adapterPosition == 0
            val isLast = adapterPosition == subjects.lastIndex

            this.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = marginLeft
                rightMargin = marginRight
                topMargin = if (isFirst) marginFirstAndLast else spaceBetween / 2
                bottomMargin = if (isLast) marginFirstAndLast else spaceBetween / 2
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardRecyclerAdapter.CardHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCardBinding.inflate(inflater, parent, false)
        return CardHolder(binding)
    }

    override fun getItemCount(): Int = subjects.size

    override fun onBindViewHolder(holder: CardRecyclerAdapter.CardHolder, position: Int) {
        holder.binding(subjects[position])
    }

    override fun getItemId(position: Int): Long {
        return subjects[position].id!!.toLong()
    }
}