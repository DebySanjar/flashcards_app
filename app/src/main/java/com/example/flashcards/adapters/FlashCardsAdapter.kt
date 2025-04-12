package com.example.flashcards.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards.R
import com.example.flashcards.databinding.CardItemBinding
import com.example.flashcards.models.fc_model
import kotlin.random.Random

class FlashcardsAdapter(
    private val list: ArrayList<fc_model>,
    private val onDelete: (fc_model) -> Unit,
    private val onEdit: (fc_model) -> Unit
) : RecyclerView.Adapter<FlashcardsAdapter.VH>() {

    private var lastOpenedPosition: Int? = null


    private fun getRandomColor(): Int {
        val red = Random.nextInt(100, 200)
        val green = Random.nextInt(100, 200)
        val blue = Random.nextInt(100, 200)
        return Color.rgb(red, green, blue)
    }


    inner class VH(private val binding: CardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(fcModel: fc_model, position: Int) {
            binding.askText.text = fcModel.ask
            binding.answerText.text = fcModel.answer

            val bgColor = getRandomColor()
            binding.bgCard.backgroundTintList = ColorStateList.valueOf(bgColor)

            binding.answerText.visibility =
                if (lastOpenedPosition == position) View.VISIBLE else View.GONE
            binding.optionsLayout.visibility =
                if (lastOpenedPosition == position) View.VISIBLE else View.GONE

            binding.root.setOnClickListener {
                val fadeInAnimation =
                    AnimationUtils.loadAnimation(binding.root.context, R.anim.item_fade)
                binding.answerText.startAnimation(fadeInAnimation)

                val oldPosition = lastOpenedPosition
                lastOpenedPosition = if (lastOpenedPosition == position) null else position

                notifyItemChanged(position)
                oldPosition?.let { notifyItemChanged(it) }
            }

            binding.root.setOnLongClickListener {
                val oldPosition = lastOpenedPosition
                lastOpenedPosition = if (lastOpenedPosition == position) null else position

                notifyItemChanged(position)
                oldPosition?.let { notifyItemChanged(it) }

                true
            }

            binding.deleteBtn.setOnClickListener {
                val alertDialog =
                    android.app.AlertDialog.Builder(binding.root.context)
                        .setMessage("Ushbu kartani o‘chirishni xohlaysizmi?")
                        .setPositiveButton("Ha") { _, _ ->
                            onDelete(fcModel)  // ✅ Ha bosilsa, kartani o‘chiramiz
                        }.setNegativeButton("Yo‘q") { dialog, _ ->
                            dialog.dismiss()
                        }.create()


                alertDialog.show()
            }


            binding.editBtn.setOnClickListener {
                onEdit(fcModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = CardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount() = list.size
}
