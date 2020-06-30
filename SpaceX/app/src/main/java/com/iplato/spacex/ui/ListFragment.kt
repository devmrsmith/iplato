package com.iplato.spacex.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.iplato.spacex.App
import com.iplato.spacex.R
import com.iplato.spacex.data.SpaceXShipsItem
import com.iplato.spacex.databinding.ListFragmentBinding
import com.iplato.spacex.databinding.ListItemBinding
import com.iplato.spacex.repository.Repository
import com.iplato.spacex.viewmodels.ListViewModel
import javax.inject.Inject


class ListFragment : Fragment() {

    companion object {
        fun newInstance() = ListFragment()
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var viewModel: ListViewModel

    @Inject
    lateinit var repository: Repository

    private var viewModelAdapter: Adapter? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var switchActive: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: ListFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.list_fragment,
            container,
            false
        )

        // Configure databinding with viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel = ViewModelProvider(this, factory).get(ListViewModel::class.java)
        binding.viewModel = viewModel

        // Configure swipeRefreshLayout to refresh content
        swipeRefreshLayout = binding.swipeContainer
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.getShips()
        }

        // Configure switch to call adapter filter methods
        switchActive = binding.switchActive
        switchActive.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModelAdapter?.filter?.filter("active")
            } else {
                viewModelAdapter?.filter?.filter("all")
            }
        }

        viewModelAdapter = Adapter()

        recyclerView = binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Observe the Viewmodel's list of ships
        viewModel.listShips.observe(viewLifecycleOwner, Observer<List<SpaceXShipsItem>> { ships ->
            ships.apply {
                viewModelAdapter?.ships = ships as MutableList<SpaceXShipsItem>
            }
        })

        // Observe the Viewmodel's refreshComplete LiveData - hide swipeContainer
        viewModel.refreshComplete.observe(viewLifecycleOwner, Observer<Boolean> {
            if (it == true) swipeRefreshLayout.setRefreshing(false);
        })

        // Observe the Viewmodel's networkError LiveData - feedback to user
        viewModel.networkError.observe(viewLifecycleOwner, Observer {
            Toast.makeText(this.activity, it, Toast.LENGTH_LONG).show()
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as App).appComponent.inject(this)
    }
}

class Adapter() : RecyclerView.Adapter<ViewHolder>(), Filterable {

    var shipFilterList: List<SpaceXShipsItem> = listOf()

    var ships: List<SpaceXShipsItem> = listOf()
        set(value) {
            field = value
            shipFilterList = ships
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val withDataBinding: ListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            ViewHolder.LAYOUT,
            parent,
            false
        )
        return ViewHolder(withDataBinding)
    }

    override fun getItemCount(): Int {
        return shipFilterList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.item = shipFilterList[position]
        }
    }

    /**
     * Returns a custom filtered set of results - either all, or just active ones
     */
    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.equals("all")) {
                    shipFilterList = ships
                } else if (charSearch.equals("active")) {
                    shipFilterList = ships.filter { it.active == true }
                }
                val filterResults = FilterResults()
                filterResults.values = shipFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                notifyDataSetChanged()
            }
        }
    }

}

class ViewHolder(val viewDataBinding: ListItemBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.list_item
    }
}

@BindingAdapter("imageUrl")
fun setImageUrl(imgView: ImageView, imgUrl: String?) {

    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .into(imgView)
    }
}

@BindingAdapter("goneIfNotNull")
fun goneIfNotNull(view: View, it: Any?) {
    view.visibility = if (it != null) View.GONE else View.VISIBLE
}

@BindingAdapter("goneIfNotZero")
fun goneIfNoDate(view: View, it: Any?) {
    view.visibility = if (it == 0) View.GONE else View.VISIBLE
}