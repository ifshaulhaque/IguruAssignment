package com.ifsha.iguruassignment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ifsha.iguruassignment.databinding.ActivityMainBinding
import com.ifsha.iguruassignment.db.AppDatabase
import com.ifsha.iguruassignment.helper.LocationHelper
import com.ifsha.iguruassignment.home.UserAdapter
import com.ifsha.iguruassignment.home.UserViewModel
import com.ifsha.iguruassignment.home.UserViewModelFactory
import com.ifsha.iguruassignment.network.Api
import com.ifsha.iguruassignment.network.ApiService
import com.ifsha.iguruassignment.network.UserRepository

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository(Api.getApi().create(ApiService::class.java), AppDatabase.getDatabase(this).userDao()))
    }
    private lateinit var locationHelper: LocationHelper
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationHelper = LocationHelper(this)
        userAdapter = UserAdapter(
            this, emptyList()
        )

        // Setup RecyclerView
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = userAdapter
        }

        // Observe the users LiveData
        userViewModel.users.observe(this, Observer { users ->
            users?.let {
                userAdapter.updateData(users)
            }
        })

        // Fetch users from API
        userViewModel.fetchUsers()

        // Check and request location permissions
        if (checkPermissions()) {
            getUserLocation()
        } else {
            requestPermissions()
        }
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_LOCATION_PERMISSION
        )
    }

    private fun getUserLocation() {
        locationHelper.getUserLocation { location ->
//            binding.locationTextView.text = "Lat: ${location.latitude}, Long: ${location.longitude}"
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation()
            }
        }
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }
}
