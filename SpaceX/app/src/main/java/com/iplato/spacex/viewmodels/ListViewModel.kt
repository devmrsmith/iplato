package com.iplato.spacex.viewmodels

import androidx.lifecycle.*
import com.iplato.spacex.api.Result
import com.iplato.spacex.data.SpaceXShips
import com.iplato.spacex.repository.Repository
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider


class ListViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    // LiveData to flag network error
    private val _networkError = MutableLiveData<String>()
    val networkError: LiveData<String>
        get() = _networkError

    // LiveData to update UI after data refresh
    private val _refreshComplete = MutableLiveData<Boolean>()
    val refreshComplete: LiveData<Boolean>
        get() = _refreshComplete

    init {
        // Call API to get Ships
        viewModelScope.launch {
            getShips()
        }
    }

    // External list
    val listShips: MutableLiveData<SpaceXShips>
        get() = _shipList

    // Internal list
    private val _shipList: MutableLiveData<SpaceXShips> = MutableLiveData<SpaceXShips>()
    fun setShipList(listShips: SpaceXShips): MutableLiveData<SpaceXShips> {
        _shipList.setValue(listShips)
        return _shipList
    }

    /**
     * Calls repository to fetch the SpaceX ships
     */
    fun getShips() {

        viewModelScope.launch {

            val shipsResponse = repository.fetchShips()

            if (shipsResponse.status == Result.Status.SUCCESS) {

                shipsResponse.data?.let { setShipList(it) }
                _refreshComplete.value = true

            } else if (shipsResponse.status == Result.Status.ERROR) {
                postError(shipsResponse.message!!)
            }
        }
    }

    private fun postError(message: String) {
        Timber.e("An error occurred: $message")
        _networkError.value = message
    }
}

class ListViewModelFactory @Inject constructor(private val viewModelProvider: Provider<ListViewModel>) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModelProvider.get() as T
    }
}