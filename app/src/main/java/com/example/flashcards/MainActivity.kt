package com.example.flashcards

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flashcards.adapters.FlashcardsAdapter
import com.example.flashcards.database.MyDbHelper
import com.example.flashcards.databinding.ActivityMainBinding
import com.example.flashcards.databinding.ItemDialogBinding
import com.example.flashcards.models.fc_model

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: MyDbHelper
    private lateinit var adapter: FlashcardsAdapter
    private lateinit var list: ArrayList<fc_model>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = MyDbHelper(this)
        list = dbHelper.getAllFc()
        adapter = FlashcardsAdapter(list, ::deleteCard, ::editCard)

        checkEmptyState()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.fab.setOnClickListener { showAddDialog() }
    }

    private fun checkEmptyState() {
        if (list.isEmpty()) {
            binding.emptyLogo.visibility = View.VISIBLE
        } else {
            binding.emptyLogo.visibility = View.GONE
        }
    }

    private fun showAddDialog() {
        val background = GradientDrawable()
        background.setColor(android.graphics.Color.WHITE) // Dialog foni oq rang
        background.cornerRadius = 38f

        val dialogBinding = ItemDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this).setView(dialogBinding.root).create()

        dialogBinding.addBtn.setOnClickListener {
            val ask = dialogBinding.askTxt.text.toString().trim()
            val answer = dialogBinding.answerTxt.text.toString().trim()

            var boshmi = true

            if (ask.isEmpty()) {
                dialogBinding.askTxt.error = "Savol maydoni bo‘sh bo‘lishi mumkin emas!"
                boshmi = false
            }

            if (answer.isEmpty()) {
                dialogBinding.answerTxt.error = "Javob maydoni bo‘sh bo‘lishi mumkin emas!"
                boshmi = false
            }

            if (boshmi) {
                val card = fc_model(0, ask, answer)
                dbHelper.addFc(card)
                list.add(card)
                adapter.notifyItemInserted(list.size - 1)
                checkEmptyState()
                dialog.dismiss()
            }
        }

        dialog.window?.setBackgroundDrawable(background)
        dialog.show()
    }

    private fun deleteCard(fcModel: fc_model) {
        dbHelper.deleteFc(fcModel)
        list.remove(fcModel)
        adapter.notifyDataSetChanged()
        checkEmptyState()
    }

    private fun editCard(fcModel: fc_model) {
        val background = GradientDrawable()
        background.setColor(android.graphics.Color.WHITE) // Dialog foni oq rang
        background.cornerRadius = 38f

        val dialogBinding = ItemDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this).setView(dialogBinding.root).create()


        dialogBinding.askTxt.setText(fcModel.ask)
        dialogBinding.answerTxt.setText(fcModel.answer)
        dialogBinding.addBtn.text = "Yangilash" // Tugma nomini o‘zgartirish

        dialogBinding.addBtn.setOnClickListener {
            val ask = dialogBinding.askTxt.text.toString().trim()
            val answer = dialogBinding.answerTxt.text.toString().trim()

            var isValid = true

            if (ask.isEmpty()) {
                dialogBinding.askTxt.error = "Savol maydoni bo‘sh bo‘lishi mumkin emas!"
                isValid = false
            }

            if (answer.isEmpty()) {
                dialogBinding.answerTxt.error = "Javob maydoni bo‘sh bo‘lishi mumkin emas!"
                isValid = false
            }

            if (isValid) {
                val updatedCard = fc_model(fcModel.id, ask, answer)
                dbHelper.updateFc(updatedCard)

                val index = list.indexOfFirst { it.id == fcModel.id }
                if (index != -1) {
                    list[index] = updatedCard
                    adapter.notifyItemChanged(index)
                }

                checkEmptyState()
                dialog.dismiss()
            }
        }

        dialog.window?.setBackgroundDrawable(background)
        dialog.show()
    }

}
