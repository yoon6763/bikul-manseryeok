package com.example.manseryeok.page

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.manseryeok.R
import com.example.manseryeok.databinding.ActivityCompassBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission

class CompassActivity : AppCompatActivity(), OnMapReadyCallback, SensorEventListener {
    private lateinit var googleMap: GoogleMap
    private val locationManager by lazy { getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    private val gpsListener = GPSListener()
    private val binding by lazy { ActivityCompassBinding.inflate(layoutInflater) }
    private var recentRefreshTime = 0L

    lateinit var mSensorManager: SensorManager
    lateinit var mAccelerometer: Sensor
    lateinit var mMagnetometer: Sensor
    var mR = FloatArray(9)
    var mLastAccelerometer = FloatArray(3)
    var mLastMagnetometer = FloatArray(3)
    var mLastAccelerometerSet = false
    var mLastMagnetometerSet = false
    var mOrientation = FloatArray(3)
    var mCurrentDegree = 0f

    private val permissionListener = object : PermissionListener {
        override fun onPermissionGranted() {

        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            Toast.makeText(
                this@CompassActivity,
                getString(R.string.msg_permission_denied),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarCompass)
        supportActionBar?.run {
            // 앱 바 뒤로가기 버튼 설정
            setDisplayHomeAsUpEnabled(true)
        }

        // Location Permission Check
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setRationaleMessage("지도 기능 사용을 위해서는 GPS 및 위치 접근 권한이 필요합니다")
            .setDeniedMessage("[설정] > [권한] 에서 권한 허용을 할 수 있습니다")
            .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)
            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .check()

        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.frag_map) as SupportMapFragment
        mapFragment.getMapAsync(this@CompassActivity)
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        googleMap.isMyLocationEnabled = true

        if (ContextCompat.checkSelfPermission(
                this@CompassActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            // 위치권한 있음

            startLocationService()
        } else {
            // 위치권한 없음
            val SEOUL = LatLng(37.56, 126.98)

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL))
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(11f))
        }
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME)
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this, mAccelerometer)
        mSensorManager.unregisterListener(this, mMagnetometer)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        recentRefreshTime = System.currentTimeMillis()
        if (event?.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.size)
            mLastAccelerometerSet = true
        } else if (event?.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.size)
            mLastMagnetometerSet = true
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer)
            val azimuthinDegress = Math.toDegrees(
                (SensorManager.getOrientation(
                    mR,
                    mOrientation
                )[0] + 360).toDouble()
            ) % 360 as Int

            val ra = RotateAnimation(
                mCurrentDegree,
                -azimuthinDegress.toFloat(),
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            )
            ra.duration = 250
            ra.fillAfter = true

            val currentPlace = CameraPosition.builder().target(
                LatLng(
                    googleMap.cameraPosition.target.latitude,
                    googleMap.cameraPosition.target.longitude
                )
            )
                .bearing(azimuthinDegress.toFloat())
                .zoom(googleMap.cameraPosition.zoom)
                .build()

            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentPlace))
            //mPointer.startAnimation(ra);
            binding.ivCompass.startAnimation(ra)
            mCurrentDegree = -azimuthinDegress.toFloat()
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    private fun startLocationService() {
        try {
            var location: Location? = null

            val minTime = 0L
            val minDistance = 0f

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    showCurrentLocation(latitude, longitude)
                }

                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener
                )
            } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    showCurrentLocation(latitude, longitude)
                }
            }

        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun showCurrentLocation(latitude: Double, longitude: Double) {
        val currentPoint = LatLng(latitude, longitude)
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPoint, 15f))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 앱 바 클릭 이벤트
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    class GPSListener : LocationListener {
        override fun onLocationChanged(p0: Location) {
            val latitude = p0.latitude
            val longitude = p0.longitude
        }
    }
}