package com.example.fetchapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fetchapp.databinding.ActivityMainBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var requestList: List<Request>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        fetchData().start()

        binding.ApplyFilters.setOnClickListener {
            applyFilter(requestList)
        }

        binding.Reset.setOnClickListener {
            updateUI(requestList)
        }

        binding.GoToTop.setOnClickListener {
            binding.RV.scrollToPosition(0)
        }
    }

    private fun fetchData(): Thread {
        return Thread {
            val url = URL("https://hiring.fetch.com/hiring.json")
            val connection = url.openConnection() as HttpsURLConnection

            if (connection.responseCode == 200) {
                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                requestList = Gson().fromJson(inputStreamReader, Array<Request>::class.java).toList()
                updateUI(requestList)
                inputStreamReader.close()
                inputSystem.close()
            }
            else {
                runOnUiThread {
                    Toast.makeText(this, "Failed to fetch data: ${connection.responseCode}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateUI(requests: List<Request>) {
        val defaultList = requests.map { RequestGroup.DataItem(it) }
        runOnUiThread {
            binding.RV.layoutManager = LinearLayoutManager(this)
            binding.RV.adapter = RVAdapter(defaultList)
            val toolbar = findViewById<MaterialToolbar>(R.id.materialToolbar)
            toolbar.subtitle = "List Size: ${defaultList.size}/${defaultList.size} (default)"
        }
    }

    private fun applyFilter(requests: List<Request>) {
        val filteredReq = requests.filter { it.name != null && it.name != ""}
        val sortedReq = filteredReq.sortedWith(compareBy<Request> { it.listId }.thenBy { it.name })
        val groupedReq = sortedReq.groupBy { it.listId }

        val finalList: List<RequestGroup> = groupedReq.flatMap { (listId, items) ->
            listOf(RequestGroup.GroupItem("List ID: $listId")) + items.map {RequestGroup.DataItem(it) }
        }

        binding.RV.adapter = RVAdapter(finalList)
        val groupCount = finalList.count { it is RequestGroup.GroupItem }
        val toolbar = findViewById<MaterialToolbar>(R.id.materialToolbar)
        toolbar.subtitle = "List Size: ${finalList.size - groupCount}/${requests.size} (filtered)"
    }
}