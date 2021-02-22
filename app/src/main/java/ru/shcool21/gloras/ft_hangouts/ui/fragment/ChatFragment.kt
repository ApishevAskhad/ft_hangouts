package ru.shcool21.gloras.ft_hangouts.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fmt_chat.*
import ru.shcool21.gloras.ft_hangouts.R
import ru.shcool21.gloras.ft_hangouts.data.entity.Contact
import ru.shcool21.gloras.ft_hangouts.data.entity.Message
import ru.shcool21.gloras.ft_hangouts.system.App
import ru.shcool21.gloras.ft_hangouts.ui.model.ChatAction.BackClickAction
import ru.shcool21.gloras.ft_hangouts.ui.model.ChatAction.CallContactAction
import ru.shcool21.gloras.ft_hangouts.ui.model.ChatAction.DeleteContactAction
import ru.shcool21.gloras.ft_hangouts.ui.model.ChatAction.EditContactAction
import ru.shcool21.gloras.ft_hangouts.ui.model.ChatAction.InitAction
import ru.shcool21.gloras.ft_hangouts.ui.model.ChatAction.LoadTextMessagesAction
import ru.shcool21.gloras.ft_hangouts.ui.model.ChatAction.SendTextMessageAction
import ru.shcool21.gloras.ft_hangouts.ui.model.ChatState.InitState
import ru.shcool21.gloras.ft_hangouts.ui.model.ChatState.ShowMessagesState
import ru.shcool21.gloras.ft_hangouts.ui.recycler.ChatAdapter
import ru.shcool21.gloras.ft_hangouts.ui.viewmodel.ChatViewModel
import javax.inject.Inject

internal class ChatFragment : Fragment() {

    companion object {
        private const val CONTACT_ID_KEY = "contact_id_key"

        fun newInstance(contactId: Int): ChatFragment {
            return ChatFragment().apply {
                arguments = bundleOf(
                    CONTACT_ID_KEY to contactId
                )
            }
        }
    }

    private val chatAdapter by lazy { ChatAdapter() }

    @Inject
    lateinit var viewModel: ChatViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fmt_chat, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).daggerComponent.inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                is InitState -> onInit(state.headerColor)
                is ShowMessagesState -> onShowMessages(state.contact, state.messages)
            }
        })
    }

    private fun onInit(headerColor: Int) {
        toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), headerColor))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val contactId = requireArguments().getInt(CONTACT_ID_KEY)
        viewModel.perform(LoadTextMessagesAction(contactId))

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.setNavigationOnClickListener {
            viewModel.perform(BackClickAction)
        }
        toolbar.inflateMenu(R.menu.menu_chat)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.callContactItem -> {
                    viewModel.perform(CallContactAction)
                    true
                }
                R.id.editContactItem -> {
                    viewModel.perform(EditContactAction)
                    true
                }
                R.id.deleteContactItem -> {
                    viewModel.perform(DeleteContactAction)
                    true
                }
                else -> false
            }
        }
        messagesRecyclerView.adapter = chatAdapter
        messagesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.perform(InitAction)

        sendMessageImageButton.setOnClickListener {
            viewModel.perform(SendTextMessageAction(messageEditText.text.toString()))
        }
    }

    private fun onShowMessages(contact: Contact, messages: List<Message>) {
        toolbar.title = contact.name
        toolbar.subtitle = contact.phoneNumber
        chatAdapter.updateMessages(messages)
    }
}
