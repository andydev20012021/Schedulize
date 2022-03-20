package uz.techie.schedulize.ui.adapters

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import uz.techie.schedulize.R
import uz.techie.schedulize.ui.models.CardColor
import uz.techie.schedulize.utils.extentions.dp

class CardColorPickAdapter : RecyclerView.Adapter<CardColorPickAdapter.CardColorHolder>() {
    private val colorDataList = CardColor.values().map { ColorData(it) }
    var colorSelectListener: ((color: CardColor) -> Unit)? = null
    var selectedColor: CardColor = CardColor.DEFAULT

    init {
        selectColor(selectedColor)
    }

    fun selectColor(color: CardColor) {
        colorDataList.forEach {
            if (it.color == color) {
                it.isChecked = true
                colorSelectListener?.invoke(color)
            } else {
                it.isChecked = false
            }
        }
        selectedColor = color
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardColorHolder {
        val imageView = ImageView(parent.context)
        val param = ViewGroup.MarginLayoutParams(32.dp, 32.dp)
        imageView.layoutParams = param
        return CardColorHolder(imageView)
    }

    override fun onBindViewHolder(holder: CardColorHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = colorDataList.size

    inner class CardColorHolder(private val view: ImageView) : RecyclerView.ViewHolder(view) {
        fun bind() {
            val colorData = colorDataList[adapterPosition]
            val checkRes = if (colorData.isChecked) R.drawable.ic_check else R.drawable.ic_uncheck
            view.setImageResource(checkRes)
            view.background = getBackgroundDrawable(colorData.color)
            view.setOnClickListener {
                selectColor(colorData.color)
            }
            view.updateLayoutMargin()
        }

        private fun getBackgroundDrawable(color: CardColor): Drawable {
            val resources = view.context.resources
            return GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                if (color == CardColor.DEFAULT)
                    setStroke(1.dp, resources.getColor(R.color.on_surface_300))
                setColor(resources.getColor(color.colorRes))

            }
        }

        private fun View.updateLayoutMargin() {
            val spaceBetween = 16.dp
            val marginFirstAndLast = 16.dp

            val isFirst = adapterPosition == 0
            val isLast = adapterPosition == colorDataList.lastIndex

            this.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = if (isFirst) marginFirstAndLast else spaceBetween / 2
                rightMargin = if (isLast) marginFirstAndLast else spaceBetween / 2
            }
        }
    }

    private data class ColorData(val color: CardColor, var isChecked: Boolean = false)
}