package com.example.napptilustechtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.napptilustechtest.databinding.ActivityOompaDetailBinding
import com.example.napptilustechtest.network.OompaDetailItem
import com.example.napptilustechtest.network.OompaInterface
import com.example.napptilustechtest.network.RetrofitProvider
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OompaDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOompaDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOompaDetailBinding.inflate(layoutInflater)
        getOompaData(intent.getIntExtra("id", 1))
    }

    private fun handleData(oompa: OompaDetailItem) {
        Picasso.get().load(oompa.image).into(binding.oompaDetailImage)
        binding.oompaDetailAge.text = "Age: ${oompa.age}"
        binding.oompaDetailCountry.text = "Country: ${oompa.country}"
        binding.oompaDetailEmail.text = "Email: ${oompa.email}"
        binding.oompaDetailId.text = "Id: ${oompa.id}"
        binding.oompaDetailFavColor.text = "Favorite color: ${oompa.favoriteList.color}"
        binding.oompaDetailFavFood.text = "Favorite food: ${oompa.favoriteList.food}"
        binding.oompaDetailGender.text = "Gender: ${oompa.gender}"
        binding.oompaDetailHeight.text = "Height: ${oompa.height}"
        binding.oompaDetailName.text = "${oompa.firstName} ${oompa.lastName}"
        binding.oompaDetailProfession.text = oompa.profession
    }

    private fun getOompaData(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitProvider(this@OompaDetailActivity).retrofitService.getOompaById(id.toString())
            val oompa = call.body()
            runOnUiThread {
                if (call.isSuccessful) {
                    oompa?.let {
                        setContentView(binding.root)
                        handleData(it)
                    }
                } else {
                    Snackbar.make(binding.root, "Error getting API", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }


}