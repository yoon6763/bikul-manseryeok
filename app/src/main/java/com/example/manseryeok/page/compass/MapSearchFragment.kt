package com.example.manseryeok.page.compass

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.manseryeok.adapter.PlaceSearchAdapter
import com.example.manseryeok.databinding.FragmentMapSearchBinding
import com.example.manseryeok.models.address.AddressSearchDto
import com.example.manseryeok.models.address.Juso
import com.example.manseryeok.models.naversearch.NaverSearchItem
import com.example.manseryeok.service.compass.AddressSearchAPI
import com.example.manseryeok.utils.SecretConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.awaitResponse
import java.util.Locale


class MapSearchFragment : Fragment() {

    var pageIndex = -1
    var pageMaxIndex = -1
    var searchKeyword = ""

    interface OnSearchButtonClickListener {
        fun onSearchButtonClick(lat: Double, lng: Double)
    }

    var onSearchButtonClickListener: OnSearchButtonClickListener? = null

    private lateinit var binding: FragmentMapSearchBinding
    private val addressSearchAPI by lazy { AddressSearchAPI.create() }
    private val placeSearchItems = ArrayList<Juso>()
    private lateinit var placeSearchAdapter: PlaceSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapSearchBinding.inflate(inflater, container, false)

        binding.etSearch.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val keyword = binding.etSearch.text.toString()
                if (keyword.isEmpty() || keyword.isBlank() || keyword == "" || keyword == "null") {
                    Toast.makeText(context, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnEditorActionListener false
                }

                CoroutineScope(Dispatchers.Main).launch {
                    val searchItem = searchItem(keyword, 1)

                    if (!searchItem.isSuccessful) {
                        Toast.makeText(context, "오류가 발생했습니다.\n계속 실패 시 문의해주세요", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    val errorMessage = searchItem.body()?.results?.common?.errorMessage
                    if (errorMessage != "정상") {
                        Toast.makeText(context, errorMessage.toString(), Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    pageMaxIndex = searchItem.body()?.results?.common?.countPerPage?.toInt() ?: 0
                    pageIndex = 1
                    searchKeyword = keyword

                    placeSearchItems.clear()

                    searchItem.body()?.results?.juso?.forEach {
                        placeSearchItems.add(it)
                    }

                    placeSearchAdapter.notifyDataSetChanged()
                }

                return@setOnEditorActionListener true
            }
            false
        }

        placeSearchAdapter = PlaceSearchAdapter(requireContext(), placeSearchItems)
        placeSearchAdapter.onItemClickListener = object : PlaceSearchAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val item = placeSearchItems[position]
                val latLng = getLatLngFromAddress(item.jibunAddr, item.roadAddr)
                onSearchButtonClickListener?.onSearchButtonClick(latLng.first, latLng.second)
            }
        }

        binding.rvSearch.adapter = placeSearchAdapter

        binding.rvSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)


                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()

                if (lastVisibleItemPosition == placeSearchItems.size - 1) {
                    if( pageIndex < pageMaxIndex) {
                        pageIndex++

                        CoroutineScope(Dispatchers.Main).launch {
                            val searchItem = searchItem(searchKeyword, pageIndex + 1)

                            if (!searchItem.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "오류가 발생했습니다.\n계속 실패 시 문의해주세요",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@launch
                            }

                            val errorMessage = searchItem.body()?.results?.common?.errorMessage
                            if (errorMessage != "정상") {
                                Toast.makeText(context, errorMessage.toString(), Toast.LENGTH_SHORT).show()
                                return@launch
                            }

                            searchItem.body()?.results?.juso?.forEach {
                                placeSearchItems.add(it)
                            }

                            placeSearchAdapter.notifyDataSetChanged()
                        }
                    } else {
                        Toast.makeText(context, "마지막 페이지입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        return binding.root
    }

    private suspend fun searchItem(keyword: String, pageIndex:Int): Response<AddressSearchDto> {
        return addressSearchAPI.getSearchResult(keyword = keyword, currentPage = pageIndex).awaitResponse()
    }

    private fun getLatLngFromAddress(address: String, roadAddress: String): Pair<Double, Double> {
        val geoCoder = Geocoder(requireContext(), Locale.KOREA)
        val addresses = geoCoder.getFromLocationName(address, 1)
        return if (addresses!!.isNotEmpty()) {
            Pair(addresses[0].latitude, addresses[0].longitude)
        } else {
            val roadAddresses = geoCoder.getFromLocationName(roadAddress, 1)
            if (roadAddresses!!.isNotEmpty()) {
                Pair(roadAddresses[0].latitude, roadAddresses[0].longitude)
            } else {
                Pair(0.0, 0.0)
            }
        }
    }

    companion object {
        fun newInstance() = MapSearchFragment()
    }
}