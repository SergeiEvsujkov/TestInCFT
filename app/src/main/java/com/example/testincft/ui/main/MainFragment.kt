package com.example.testincft.ui.main

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testincft.R
import com.example.testincft.databinding.MainFragmentBinding
import com.example.testincft.room.CurrencyRepository
import kotlinx.android.synthetic.main.item_currency.view.*


private const val TAG = "CurrenciesListFragment"
private const val UPDATE_TIME = 600000L

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var currencyRecyclerView: RecyclerView
    private var adapter: CurrencyAdapter? = CurrencyAdapter(emptyList())

    private val currencyListViewModel: MainFragmentViewModel by lazy {
        ViewModelProvider(this).get(MainFragmentViewModel::class.java)
    }
    private lateinit var curRep: CurrencyRepository
    private val mInterval: Long = UPDATE_TIME
    private var mHandler: Handler? = null
    private var isFirstStartHandler = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        curRep = CurrencyRepository.get()
        mHandler = Handler(Looper.getMainLooper())
        startRepeatingTask()
    }

    private var mStatusChecker: Runnable = object : Runnable {
        override fun run() {
            try {
                if (!isFirstStartHandler) {
                    currencyListViewModel.requestData()
                }
                isFirstStartHandler = false
            } finally {
                mHandler!!.postDelayed(this, mInterval)
            }
        }
    }

    private fun startRepeatingTask() {
        mStatusChecker.run()
    }

    private fun stopRepeatingTask() {
        mHandler!!.removeCallbacks(mStatusChecker)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        currencyRecyclerView = binding.recyclerView
        currencyRecyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currencyListViewModel.currencyListLiveData.observe(
            viewLifecycleOwner,
            { currencies ->
                currencies?.let {
                    Log.i(TAG, "Got currencies ${currencies.size}")
                    updateUI(currencies)
                }
            })
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )

        binding.headerToolbar.setOnMenuItemClickListener { menuItem ->

            when (menuItem.itemId) {
                R.id.update -> {
                    currencyListViewModel.requestData()
                    Toast.makeText(
                        requireContext(),
                        resources.getText(R.string.data_update),
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                }

                else -> false
            }
        }

    }


    private fun updateUI(currencies: List<Currency>) {
        if (currencies.isEmpty()) {
            currencyListViewModel.requestData()
        }
        adapter = CurrencyAdapter(currencies)
        currencyRecyclerView.adapter = adapter
    }


    private inner class CurrencyHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        private lateinit var currency: Currency

        init {
            itemView.setOnClickListener(this)
        }

        var isVisible = false

        fun bind(currency: Currency) {

            if (layoutPosition != RecyclerView.NO_POSITION) {
                this.currency = currency
                itemView.code_currency.text = this.currency.charCode
                itemView.number_currency.text = this.currency.numCode
                itemView.name_currency.text = this.currency.name
                itemView.number.text = this.currency.nominal.toString()
                itemView.value_currency.text = this.currency.value.toString()
                itemView.result_button.setOnClickListener {
                    if (!itemView.text_converter_into.text.isNullOrEmpty()) {
                        var result: String? = null
                        result = String.format(
                            "%.2f",
                            itemView.text_converter_into.text.toString()
                                .toFloat() / (itemView.value_currency.text.toString()
                                .toFloat() / itemView.number.text.toString().toFloat())
                        )
                        itemView.converter_result.text = result
                    } else {
                        Toast.makeText(
                            requireContext(),
                            resources.getText(R.string.need_value),
                            Toast.LENGTH_SHORT
                        ).show()
                        itemView.converter_result.text = resources.getText(R.string.not_converter)
                    }
                }

            }

        }

        override fun onClick(v: View?) {
            if (isVisible) {
                itemView.layout_converter.isGone = true
                isVisible = false
            } else {
                itemView.layout_converter.isGone = false
                isVisible = true
            }
        }

    }

    private inner class CurrencyAdapter(var currency: List<Currency>) :
        RecyclerView.Adapter<CurrencyHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : CurrencyHolder {
            val view = layoutInflater.inflate(R.layout.item_currency, parent, false)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.recyclerView.setOnScrollChangeListener { _, _, _, _, _ ->
                    binding.header.isSelected = binding.recyclerView.canScrollVertically(-1)
                }
            }
            return CurrencyHolder(view)
        }

        override fun getItemCount() = currency.size

        override fun onBindViewHolder(holder: CurrencyHolder, position: Int) {
            holder.bind(currency[position])
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        stopRepeatingTask()
        _binding = null
    }

    companion object {
        fun newInstance() = MainFragment()
    }


}