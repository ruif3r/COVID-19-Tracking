package com.example.ncov19traking.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkCall<T> {

    private lateinit var call : Call<T>

    fun makeCall(call: Call<T>): LiveData<T> {
        this.call = call
        val callback = GenericCallBack<T>()
        this.call.clone().enqueue(callback)
        return callback.result
    }

    class GenericCallBack<T> : Callback<T> {

        var result = MutableLiveData<T>()
        override fun onFailure(call: Call<T>, t: Throwable) {
            Log.d("callErr", t.message?:"ok")
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            Log.d("callSuccess", response.message()?:"ok")
            if (response.isSuccessful) {
                result.value = response.body()
            }
        }
    }
}