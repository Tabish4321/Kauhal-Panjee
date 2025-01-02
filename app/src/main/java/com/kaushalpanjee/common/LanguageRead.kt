import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kaushalpanjee.R

class LanguageRead(
    private val languages: List<Language>,
    private val selectedLanguageStates: MutableMap<String, MutableMap<String, Boolean>>
) : RecyclerView.Adapter<LanguageRead.LanguageViewHolder>() {

    inner class LanguageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val languageName: TextView = view.findViewById(R.id.languageName)
        val readCheckbox: CheckBox = view.findViewById(R.id.readCheckbox)
        val writeCheckbox: CheckBox = view.findViewById(R.id.writeCheckbox)
        val speakCheckbox: CheckBox = view.findViewById(R.id.speakCheckbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.speak_read_language_selection, parent, false)
        return LanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val language = languages[position]
        holder.languageName.text = language.name

        // Initialize the state map for the current language if it doesn't exist yet
        val languageState = selectedLanguageStates.getOrPut(language.name) { mutableMapOf() }

        // Set checkbox states from selectedLanguageStates map
        holder.readCheckbox.isChecked = languageState["canRead"] ?: false
        holder.writeCheckbox.isChecked = languageState["canWrite"] ?: false
        holder.speakCheckbox.isChecked = languageState["canSpeak"] ?: false

        // Update the selectedLanguageStates map when checkboxes are changed
        holder.readCheckbox.setOnCheckedChangeListener { _, isChecked ->
            languageState["canRead"] = isChecked
        }

        holder.writeCheckbox.setOnCheckedChangeListener { _, isChecked ->
            languageState["canWrite"] = isChecked
        }

        holder.speakCheckbox.setOnCheckedChangeListener { _, isChecked ->
            languageState["canSpeak"] = isChecked
        }
    }

    override fun getItemCount() = languages.size
}
