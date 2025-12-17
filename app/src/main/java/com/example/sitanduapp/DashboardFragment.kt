package com.example.sitanduapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sitanduapp.adapter.DashboardAdapter
import com.example.sitanduapp.api.Request
import com.example.sitanduapp.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardFragment : Fragment() {

    private lateinit var rvTtd: RecyclerView
    private lateinit var rvJt: RecyclerView
    private lateinit var adapterTtd: DashboardAdapter
    private lateinit var adapterJt: DashboardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        rvTtd = view.findViewById(R.id.rv_dashboard_ttd)
        rvTtd.layoutManager = GridLayoutManager(context, 2)
        adapterTtd = DashboardAdapter(emptyList()) { req ->
            val intent = Intent(context, DetailTtdActivity::class.java)
            intent.putExtra("EXTRA_REQUEST", req)
            startActivity(intent)
        }
        rvTtd.adapter = adapterTtd

        rvJt = view.findViewById(R.id.rv_dashboard_jt)
        rvJt.layoutManager = GridLayoutManager(context, 2) // INI JUGA GRID
        adapterJt = DashboardAdapter(emptyList()) { req ->
            val intent = Intent(context, DetailJtActivity::class.java)
            intent.putExtra("EXTRA_REQUEST", req)
            startActivity(intent)
        }
        rvJt.adapter = adapterJt

        view.findViewById<Button>(R.id.lainnya_ttd).setOnClickListener {
            view.findViewById<Button>(R.id.lainnya_ttd).setOnClickListener {
                findNavController().navigate(R.id.ttdFragment)
            }

            view.findViewById<Button>(R.id.lainnya_jt).setOnClickListener {
                findNavController().navigate(R.id.jtFragment)
            }
        }

        loadData()

        return view
    }

    private fun loadData() {
        RetrofitClient.instance.getRequests().enqueue(object : Callback<List<Request>> {
            override fun onResponse(call: Call<List<Request>>, response: Response<List<Request>>) {
                if (response.isSuccessful) {
                    val allData = response.body() ?: emptyList()

                    val listTtd = allData.filter { it.tipe == "ttd" }.take(4)
                    adapterTtd.updateData(listTtd)

                    val listJt = allData.filter { it.tipe == "jt" }.take(4)
                    adapterJt.updateData(listJt)
                }
            }

            override fun onFailure(call: Call<List<Request>>, t: Throwable) {
                Toast.makeText(context, "Gagal load dashboard", Toast.LENGTH_SHORT).show()
            }
        })
    }
}