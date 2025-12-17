package com.example.sitanduapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sitanduapp.adapter.RequestAdapter
import com.example.sitanduapp.api.Request
import com.example.sitanduapp.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TtdFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RequestAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ttd, container, false)

        recyclerView = view.findViewById(R.id.rv_request_ttd)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = RequestAdapter(emptyList()) { requestTerpilih ->
            val intent = Intent(requireContext(), DetailTtdActivity::class.java)

            intent.putExtra("EXTRA_REQUEST", requestTerpilih)

            startActivity(intent)
        }
        recyclerView.adapter = adapter

        fetchDataRequests()

        return view
    }

    private fun fetchDataRequests() {
        RetrofitClient.instance.getRequests().enqueue(object : Callback<List<Request>> {
            override fun onResponse(call: Call<List<Request>>, response: Response<List<Request>>) {
                if (response.isSuccessful) {
                    val semuaData = response.body() ?: emptyList()

                    val dataKhususTtd = semuaData.filter { it.tipe == "ttd" }

                    adapter.updateData(dataKhususTtd)

                } else {
                    Toast.makeText(context, "Gagal ambil data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Request>>, t: Throwable) {
                Toast.makeText(context, "Error Internet: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}