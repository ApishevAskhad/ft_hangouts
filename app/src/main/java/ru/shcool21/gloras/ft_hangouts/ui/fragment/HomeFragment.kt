package ru.shcool21.gloras.ft_hangouts.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fmt_home.*
import kotlinx.android.synthetic.main.menu_home_color.*
import ru.shcool21.gloras.ft_hangouts.R
import ru.shcool21.gloras.ft_hangouts.data.entity.Contact
import ru.shcool21.gloras.ft_hangouts.system.App
import ru.shcool21.gloras.ft_hangouts.ui.model.HomeAction.AddContactClickAction
import ru.shcool21.gloras.ft_hangouts.ui.model.HomeAction.ChangeHeaderColorAction
import ru.shcool21.gloras.ft_hangouts.ui.model.HomeAction.InitAction
import ru.shcool21.gloras.ft_hangouts.ui.model.HomeAction.LoadContactsAction
import ru.shcool21.gloras.ft_hangouts.ui.model.HomeAction.OnContactClickAction
import ru.shcool21.gloras.ft_hangouts.ui.model.HomeState.ColorChange
import ru.shcool21.gloras.ft_hangouts.ui.model.HomeState.EmptyContactsLis
import ru.shcool21.gloras.ft_hangouts.ui.model.HomeState.ShowContacts
import ru.shcool21.gloras.ft_hangouts.ui.recycler.HomeAdapter
import ru.shcool21.gloras.ft_hangouts.ui.viewmodel.HomeViewModel
import javax.inject.Inject

internal class HomeFragment : Fragment() {

    private val homeAdapter by lazy {
        HomeAdapter { contactId ->
            viewModel.perform(OnContactClickAction(contactId))
        }
    }

    @Inject
    lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).daggerComponent.inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                is ShowContacts -> onShowContacts(state.contacts)
                is EmptyContactsLis -> onEmptyContactsList()
                is ColorChange -> onColorChange(state.headerColor, state.itemRes)
            }
        })
    }

    private fun onColorChange(headerColor: Int, itemRes: Int) {
        toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), headerColor))
        colorImageView.setImageResource(itemRes)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fmt_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.inflateMenu(R.menu.menu_home)

        toolbar.menu
            .findItem(R.id.changeColorItem)
            .actionView
            .setOnClickListener { viewModel.perform(ChangeHeaderColorAction) }

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.addContactItem -> {
                    viewModel.perform(AddContactClickAction)
                    true
                }
                else -> false
            }
        }
        homeRecyclerView.adapter = homeAdapter
        homeRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.perform(InitAction)
        viewModel.perform(LoadContactsAction)
    }

    private fun onShowContacts(contacts: List<Contact>) {
        defaultTextView.visibility = GONE
        homeRecyclerView.visibility = VISIBLE
        homeAdapter.updateContacts(contacts)
    }

    private fun onEmptyContactsList() {
        defaultTextView.visibility = VISIBLE
        homeRecyclerView.visibility = GONE
    }
}
