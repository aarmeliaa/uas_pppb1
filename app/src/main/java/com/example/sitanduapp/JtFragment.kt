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
import com.example.sitanduapp.adapter.JtAdapter
import com.example.sitanduapp.api.Request
import com.example.sitanduapp.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JtFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JtAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_jt, container, false)

        recyclerView = view.findViewById(R.id.rv_request_jt)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = JtAdapter(emptyList()) { requestTerpilih ->
            val intent = Intent(requireContext(), DetailJtActivity::class.java)

            intent.putExtra("EXTRA_REQUEST", requestTerpilih)

            startActivity(intent)
        }
        recyclerView.adapter = adapter

        fetchDataJanjiTemu()

        return view
    }

    private fun fetchDataJanjiTemu() {
        RetrofitClient.instance.getRequests().enqueue(object : Callback<List<Request>> {
            override fun onResponse(call: Call<List<Request>>, response: Response<List<Request>>) {
                if (response.isSuccessful) {
                    val semuaData = response.body() ?: emptyList()

                    val dataJanjiTemu = semuaData.filter { it.tipe == "jt" }

                    adapter.updateData(dataJanjiTemu)

                } else {
                    Toast.makeText(context, "Gagal load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Request>>, t: Throwable) {
                Toast.makeText(context, "Error Koneksi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}