package com.example.flashcards.database

import com.example.flashcards.models.fc_model

interface FlashCardsInterface {
    fun getAllFc(): ArrayList<fc_model>
    fun addFc(fcModel: fc_model)
    fun deleteFc(fcModel: fc_model)
    fun updateFc(fcModel: fc_model)
}
