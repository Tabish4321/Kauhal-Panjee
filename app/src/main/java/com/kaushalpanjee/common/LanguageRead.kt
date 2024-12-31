package com.kaushalpanjee.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.kaushalpanjee.R
import com.kaushalpanjee.common.model.Language


    class LanguageAdapter(
        private val context: Context,
        private val languages: MutableList<Language>
    ) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.speak_read_language_selection, parent, false)
            return LanguageViewHolder(view)
        }

        override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
            val language = languages[position]
            holder.languageName.text = language.name

            // Set the state of the chips
            holder.readChip.isChecked = language.isRead
            holder.writeChip.isChecked = language.isWrite
            holder.speakChip.isChecked = language.isSpeak

            // Handle chip state changes
            holder.readChip.setOnCheckedChangeListener { _, isChecked ->
                language.isRead = isChecked
            }
            holder.writeChip.setOnCheckedChangeListener { _, isChecked ->
                language.isWrite = isChecked
            }
            holder.speakChip.setOnCheckedChangeListener { _, isChecked ->
                language.isSpeak = isChecked
            }
        }

        override fun getItemCount(): Int = languages.size

        class LanguageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val languageName: TextView = view.findViewById(R.id.languageName)
            val readChip: Chip = view.findViewById(R.id.readChip)
            val writeChip: Chip = view.findViewById(R.id.writeChip)
            val speakChip: Chip = view.findViewById(R.id.speakChip)
        }
    }

