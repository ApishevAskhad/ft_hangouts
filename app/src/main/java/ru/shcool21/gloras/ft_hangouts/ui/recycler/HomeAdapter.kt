package ru.shcool21.gloras.ft_hangouts.ui.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_home_contact.view.*
import ru.shcool21.gloras.ft_hangouts.R
import ru.shcool21.gloras.ft_hangouts.data.entity.Contact

internal class HomeAdapter(
    private val onClick: (contactId: Int) -> Unit
) : RecyclerView.Adapter<HomeViewHolder>() {

    private val contacts = mutableListOf<Contact>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_contact, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(contacts[position], onClick)
    }

    override fun getItemCount() = contacts.size

    fun updateContacts(list: List<Contact>) {
        contacts.clear()
        contacts.addAll(list)
        notifyDataSetChanged()
    }
}

internal class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(contact: Contact, onClick: (contactId: Int) -> Unit) {
        with(itemView) {
            avatarImageView.setImageResource(R.drawable.ic_account_circle_black_24dp)
            nameTextView.text = contact.name
            phoneNumberTextView.text = contact.phoneNumber

            setOnClickListener { onClick.invoke(contact.id) }
        }
    }
}
