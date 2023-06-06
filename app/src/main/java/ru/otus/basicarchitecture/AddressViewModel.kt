package ru.otus.basicarchitecture

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import ru.otus.basicarchitecture.databinding.AddressItemBinding
import javax.inject.Inject


class AddressViewModel constructor(private val cache: WizardCache): ViewModel() {
    // TODO: Implement the ViewModel


    private var data = MutableLiveData<AddressData>().apply {
        value = cache.getAddressData()
    }

    fun getData(): LiveData<AddressData> = data
    fun setData(data: AddressData) {
        cache.setAddressData(data)
        this.data = MutableLiveData<AddressData>().apply { value = cache.getAddressData() }
    }


}

@Suppress("UNCHECKED_CAST")
@RegistrationScope
class AddressViewModelFactory @Inject constructor(private val cache: WizardCache): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Log.d("cache", cache.toString())
        return AddressViewModel(cache) as T
    }
}


class AddressAutoCompleteAdapter constructor(private val context: Context): BaseAdapter(), Filterable {

    @Inject
    lateinit var retrofit: Retrofit

    private var mResults = List<String>(0) { "" }

    init {
        DaggerDaDataComponent.create().inject(this)
    }

    override fun getCount(): Int = mResults.size

    override fun getItem(position: Int): String = mResults[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.address_item, parent, false)
        val b = AddressItemBinding.bind(view)
        b.addressItem.text = getItem(position)
        return view
    }

    override fun getFilter() = object: Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {

            val result = FilterResults().apply {
                count = 0
            }

            if ((constraint?.length ?: 1) % 3 == 0) {

                Log.d("DaData", "Starting to get hints")

                val srv = retrofit.create(DaDataService::class.java)

                val rsp = srv.getAddressHint(QueryString(constraint.toString()))

                rsp.enqueue(
                    object : Callback<Suggestion> {
                        override fun onResponse(
                            call: Call<Suggestion>,
                            response: Response<Suggestion>
                        ) {
                            if (response.isSuccessful) {
                                val l = MutableList<String>(0) { "" }

                                if (response.body() != null) {
                                    (response.body() as Suggestion).apply {
                                        suggestions.forEach {
                                            l.add(it.value)
                                        }
                                    }
                                }

                                Log.d("DaData", "Query result (${l.size})" + l.toString())

                                result.values = l.toList()
                                result.count = l.size

                            }

                            publishResults(constraint, result)
                        }

                        override fun onFailure(call: Call<Suggestion>, t: Throwable) {
                            publishResults(constraint, result)
                        }

                    }
                )
            }

            Log.d("DaData", "Result count: ${result.count}")
            return result
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if ((results?.count ?: 0) > 0) {
                mResults = results?.values as List<String>
                notifyDataSetChanged()
            } else
                notifyDataSetInvalidated()
        }
    }

}
