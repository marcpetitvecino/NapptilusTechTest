package com.example.napptilustechtest

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.example.napptilustechtest.adapter.OompaAdapter
import com.example.napptilustechtest.databinding.ActivityMainBinding
import com.example.napptilustechtest.network.OompaDetailItem
import com.example.napptilustechtest.network.OompaInterface
import com.example.napptilustechtest.network.RetrofitProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: OompaAdapter
    private var oompaList = mutableListOf<OompaDetailItem>()
    private var oompaListNoFilter = mutableListOf<OompaDetailItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initList()
        getList("1")
        initListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_filter_gender -> showFilterDialog(FilterType.FILTER_GENDER)
            R.id.menu_filter_profession -> showFilterDialog(FilterType.FILTER_PROFESSION)
            R.id.menu_filter_both -> showFilterDialog(FilterType.FILTER_BOTH)
            R.id.menu_clear_filter -> resetFilters()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun filterList(filterType: FilterType, filterGender: String, filterProfession: String) {
        binding.genderFilterInfo.visibility = View.VISIBLE
        binding.professionFilterInfo.visibility = View.VISIBLE
        binding.genderFilterInfo.text = "Filtering by gender: $filterGender"
        binding.professionFilterInfo.text = "Filtering by profession: $filterProfession"

        when (filterType) {
            FilterType.FILTER_GENDER -> {
                oompaList = oompaList.filter { s -> s.gender.lowercase() == filterGender.lowercase() } as MutableList<OompaDetailItem>
                binding.professionFilterInfo.visibility = View.GONE
            }
            FilterType.FILTER_PROFESSION -> {
                oompaList = oompaList.filter { s -> s.profession.lowercase() == filterProfession.lowercase() } as MutableList<OompaDetailItem>
                binding.genderFilterInfo.visibility = View.GONE
            }
            FilterType.FILTER_BOTH -> {
                oompaList = oompaList.filter { s -> s.gender.lowercase() == filterGender.lowercase() && s.profession.lowercase() == filterProfession.lowercase() } as MutableList<OompaDetailItem>
            }
        }

        adapter = OompaAdapter(oompaList, ::onListClick)
        binding.oompaList.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun showFilterDialog(filterType: FilterType) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Filter by")

        val genderInput = EditText(this)
        genderInput.hint = "Enter gender filter"
        genderInput.inputType = InputType.TYPE_CLASS_TEXT

        val professionInput = EditText(this)
        professionInput.hint = "Enter profession filter"
        professionInput.inputType = InputType.TYPE_CLASS_TEXT

        when (filterType) {
            FilterType.FILTER_GENDER -> {
                builder.setView(genderInput)
            }
            FilterType.FILTER_PROFESSION -> {
                builder.setView(professionInput)
            }
            FilterType.FILTER_BOTH -> {
                val ll = LinearLayout(this)
                ll.orientation = LinearLayout.VERTICAL
                ll.addView(genderInput)
                ll.addView(professionInput)
                builder.setView(ll)
            }
        }
        builder.setPositiveButton("Filter") { dialog, which ->
            resetFilters()
            filterList(filterType, genderInput.text.toString(), professionInput.text.toString())
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }

    private fun initListeners() {
        binding.pageBtn.setOnClickListener {
            if (binding.pageInput.text.toString() == "0" || binding.pageInput.text.isNullOrEmpty()) {
                getList("1")
            } else {
                getList(binding.pageInput.text.toString())
            }
            binding.genderFilterInfo.visibility = View.GONE
            binding.professionFilterInfo.visibility = View.GONE
        }
    }


    private fun initList() {
        adapter = OompaAdapter(oompaList, ::onListClick)
        binding.oompaList.adapter = adapter
    }

    private fun onListClick(v: View) {
        val clickedOompa = v.tag as OompaDetailItem
        val intent = Intent(this, OompaDetailActivity::class.java)
        intent.putExtra("id", clickedOompa.id)
        startActivity(intent)
    }

    private fun getList(page: String)  {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitProvider(this@MainActivity).retrofitService.getOompaListByPage("?page=$page")
            val list = call.body()
            runOnUiThread {
                if (call.isSuccessful) {
                    list?.let {
                        oompaList.clear()
                        oompaList.addAll(it.result)
                        oompaListNoFilter.addAll(it.result)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    Snackbar.make(binding.root, "Error getting API", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun resetFilters() {
        if (oompaList == oompaListNoFilter) {
            Snackbar.make(binding.root, "There are no filters", Snackbar.LENGTH_SHORT).show()
        } else {
            oompaList = oompaListNoFilter
            initList()
            binding.genderFilterInfo.visibility = View.GONE
            binding.professionFilterInfo.visibility = View.GONE
        }
    }

}

enum class FilterType {
    FILTER_GENDER,
    FILTER_PROFESSION,
    FILTER_BOTH
}