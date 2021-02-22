package ru.shcool21.gloras.ft_hangouts.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fmt_creation.*
import ru.shcool21.gloras.ft_hangouts.R
import ru.shcool21.gloras.ft_hangouts.data.entity.Contact
import ru.shcool21.gloras.ft_hangouts.system.App
import ru.shcool21.gloras.ft_hangouts.ui.model.CreationAction.CreateContactAction
import ru.shcool21.gloras.ft_hangouts.ui.model.CreationAction.LoadContactAction
import ru.shcool21.gloras.ft_hangouts.ui.model.CreationAction.UpdateContactAction
import ru.shcool21.gloras.ft_hangouts.ui.model.CreationState.CreationSuccess
import ru.shcool21.gloras.ft_hangouts.ui.model.CreationState.LoadContact
import ru.shcool21.gloras.ft_hangouts.ui.model.CreationState.UpdateSuccess
import ru.shcool21.gloras.ft_hangouts.ui.viewmodel.CreationViewModel
import javax.inject.Inject

internal class CreationFragment : Fragment() {

    companion object {

        private const val CONTACT_ID_KEY = "contact_id_key"

        fun newInstance(id: Int): CreationFragment {
            return CreationFragment().apply {
                arguments = bundleOf(
                    CONTACT_ID_KEY to id
                )
            }
        }
    }

    @Inject
    lateinit var viewModel: CreationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).daggerComponent.inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                is LoadContact -> onLoadContact(state.contact)
                is CreationSuccess -> onCreationSuccess()
                is UpdateSuccess -> onUpdateSuccess()
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fmt_creation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //TODO refactor: bad decision
        arguments?.let { bundle ->
            val contactId = bundle.getInt(CONTACT_ID_KEY)

            viewModel.perform(LoadContactAction(contactId))

            actionButton.text = resources.getText(R.string.creation_update)
            actionButton.setOnClickListener {
                val contact = Contact(
                    id = contactId,
                    name = nameEditText.text.toString(),
                    phoneNumber = phoneNumberEditText.text.toString()
                )
                viewModel.perform(UpdateContactAction(contact))
            }
        } ?: run {
            actionButton.text = resources.getText(R.string.creation_create)
            actionButton.setOnClickListener {
                val contact = Contact(
                    name = nameEditText.text.toString(),
                    phoneNumber = phoneNumberEditText.text.toString()
                )
                viewModel.perform(CreateContactAction(contact))
            }
        }
    }

    private fun onLoadContact(contact: Contact) {
        nameEditText.setText(contact.name)
        phoneNumberEditText.setText(contact.phoneNumber)
    }

    private fun onCreationSuccess() {
        Toast.makeText(requireContext(), getString(R.string.creation_message_success), Toast.LENGTH_LONG).show()
    }

    private fun onUpdateSuccess() {
        Toast.makeText(requireContext(), getString(R.string.creation_message_update_success), Toast.LENGTH_LONG).show()
    }
}
