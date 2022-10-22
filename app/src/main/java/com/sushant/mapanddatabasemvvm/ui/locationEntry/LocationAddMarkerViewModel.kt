package com.sushant.mapanddatabasemvvm.ui.locationEntry

import android.app.Application
import androidx.lifecycle.*
import com.sushant.mapanddatabasemvvm.database.model.LocationEntry
import com.sushant.mapanddatabasemvvm.database.model.UiLocationEntry
import com.sushant.mapanddatabasemvvm.database.repository.LocationEnterRepository
import com.sushant.mapanddatabasemvvm.dependancy.DependencyExtensions
import com.sushant.mapanddatabasemvvm.dependancy.convertToUiLocationEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.ArrayList

class LocationAddMarkerViewModel(
    application: Application,
    private val repository: LocationEnterRepository,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val _entryList: MutableLiveData<ArrayList<UiLocationEntry>> = getEntryListResponse()
    private var entryList: LiveData<ArrayList<UiLocationEntry>> = _entryList
    private val _isInserted = MutableLiveData(false)
    private val isInserted: LiveData<Boolean> = _isInserted

    fun insert(locationEntry: LocationEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(locationEntry)
                .flowOn(Dispatchers.IO)
                .collect {
                    _isInserted.postValue(it)
                }
        }
    }

    fun fetchEntryList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getLocationEntry()
                .map {
                    it.map { entry ->
                        entry.convertToUiLocationEntry()
                    }
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    withContext(Dispatchers.Main) {
                        setEntryList(ArrayList(it))
                    }
                }
        }
    }

    fun getIsInserted() = isInserted
    fun getEntryList() = entryList
    fun isPersistedAvailable(): LiveData<Boolean> =
        savedStateHandle.getLiveData<Boolean>(PERSISTED, false)

    fun setPersisted(boolean: Boolean) = savedStateHandle.set(PERSISTED, boolean)
    private fun setEntryList(response: ArrayList<UiLocationEntry>) =
        savedStateHandle.set(RESPONSE, response)

    private fun getEntryListResponse(): MutableLiveData<ArrayList<UiLocationEntry>> =
        savedStateHandle.getLiveData(RESPONSE)

    class ViewModelFactory(private var app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val repo = DependencyExtensions().provideLocationEntryRepository(app)
            val mSavedStateHandle = SavedStateHandle()
            return modelClass.getConstructor(
                Application::class.java,
                LocationEnterRepository::class.java,
                SavedStateHandle::class.java
            ).newInstance(app, repo, mSavedStateHandle)
        }
    }

    companion object {
        const val PERSISTED = "PERSISTED"
        const val RESPONSE = "RESPONSE"
    }
}