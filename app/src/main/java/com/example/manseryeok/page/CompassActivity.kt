package com.example.manseryeok.page

import android.Manifest
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.manseryeok.R
import com.example.manseryeok.adapter.MapLayerListAdapter
import com.example.manseryeok.databinding.ActivityCompassBinding
import com.example.manseryeok.utils.Utils
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.util.FusedLocationSource

class CompassActivity : AppCompatActivity(), SensorEventListener, OnMapReadyCallback {
    private val TAG = "CompassActivity"
    private val locationManager by lazy { getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    private val gpsListener = GPSListener()
    private val binding by lazy { ActivityCompassBinding.inflate(layoutInflater) }
    private val fm by lazy { supportFragmentManager }
    private lateinit var mapFragment: MapFragment
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private lateinit var btnLocationSource: FusedLocationSource


    private lateinit var mSensorManager: SensorManager
    private lateinit var mAccelerometer: Sensor
    private lateinit var mMagnetometer: Sensor
    private var mR = FloatArray(9)
    private var mLastAccelerometer = FloatArray(3)
    private var mLastMagnetometer = FloatArray(3)
    private var mLastAccelerometerSet = false
    private var mLastMagnetometerSet = false
    private var mOrientation = FloatArray(3)
    private var mCurrentDegree = 0f
    private var mapIsReady = false
    private var isRotationFixed = false

    private lateinit var mapLayerListAdapter: MapLayerListAdapter

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

    override fun onCreate(savedInstanceState: Bundle?) = with(binding) {
        super.onCreate(savedInstanceState)
        setContentView(root)

        // android status bar color
        window.statusBarColor = getColor(R.color.navy)
        // set status bar icon color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }


        val tempList = ArrayList<String>()
        for (i in 0..16) {
            tempList.add("test")
        }

        mapLayerListAdapter = MapLayerListAdapter(this@CompassActivity, tempList)

        mapLayerListAdapter.degree = mCurrentDegree
        mapLayerListAdapter.notifyDataSetChanged()

        binding.run {
            rvCompassInfo.adapter = mapLayerListAdapter
            rvCompassInfo.layoutManager = GridLayoutManager(this@CompassActivity, 3)

            rgInfoType.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.rb_compass_nagyeong -> {
                        rvCompassInfo.visibility = View.VISIBLE
                        llSinsal.visibility = View.INVISIBLE
                    }

                    R.id.rb_compass_sinsal -> {
                        rvCompassInfo.visibility = View.INVISIBLE
                        llSinsal.visibility = View.VISIBLE
                    }
                }
            }
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

        mapFragment =
            fm.findFragmentById(R.id.frag_map) as MapFragment? ?: MapFragment.newInstance()
                .also { fm.beginTransaction().add(R.id.frag_map, it).commit() }

        locationSource = FusedLocationSource(this@CompassActivity, LOCATION_PERMISSION_REQUEST_CODE)
        mapFragment.getMapAsync(this@CompassActivity)

        btnCompassQuestion.setOnClickListener {
            val helpFragment = CompassHelpFragment.newInstance()
            helpFragment.show(supportFragmentManager, "HelpDialog")
        }

        btnCompassLocation.setOnClickListener {
            btnLocationSource =
                FusedLocationSource(this@CompassActivity, LOCATION_PERMISSION_REQUEST_CODE)

            naverMap.locationSource = btnLocationSource

            if (locationSource.lastLocation != null) {
                val cameraPosition = CameraPosition(
                    LatLng(
                        locationSource.lastLocation!!.latitude,
                        locationSource.lastLocation!!.longitude
                    ),
                    naverMap.cameraPosition.zoom,
                    naverMap.cameraPosition.tilt,
                    naverMap.cameraPosition.bearing
                )

                naverMap.cameraPosition = cameraPosition
            }
        }

        rgRotation.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                binding.rbRotation.id -> isRotationFixed = false
                binding.rbRotationFix.id -> isRotationFixed = true
            }
        }

        ivClose.setOnClickListener {
            binding.flCompassInfo.visibility = View.GONE
            binding.btnCompassInfo.visibility = View.VISIBLE
        }

        btnCompassInfo.setOnClickListener {
            binding.flCompassInfo.visibility = View.VISIBLE
            it.visibility = View.GONE
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(p0: NaverMap) {
        naverMap = p0
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
        naverMap.uiSettings.isZoomControlEnabled = false

        mapIsReady = true

        naverMap.addOnCameraChangeListener { i, b ->
            if (isRotationFixed) {

                // 네이버 지도 각도 받아오기
                val rotation = naverMap.cameraPosition.bearing

                binding.tvCompassDegree.text = "${Utils.degreeFormat.format(rotation)}°"

                mapLayerListAdapter.degree = rotation.toFloat()
                mapLayerListAdapter.notifyDataSetChanged()
            }
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

    var currentTime = 0L

    override fun onSensorChanged(event: SensorEvent) {
        val nowTime = System.currentTimeMillis()
        if (nowTime - currentTime < 3) {
            return
        }
        currentTime = nowTime


        if (isRotationFixed) {
            val rotation = -naverMap.cameraPosition.bearing.toInt().toFloat()
            binding.ivCompass.rotation = rotation
            return
        }

        if (mapIsReady) {

            if (event.sensor == mAccelerometer) {
                System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.size)
                mLastAccelerometerSet = true
            } else if (event.sensor == mMagnetometer) {
                System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.size)
                mLastMagnetometerSet = true
            }
            if (mLastAccelerometerSet && mLastMagnetometerSet) {
                SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer)
                val azimuthinDegress = ((Math.toDegrees(
                    SensorManager.getOrientation(mR, mOrientation)[0]
                        .toDouble()
                ) + 360) % 360).toFloat()

//                val ra = RotateAnimation(
//                    mCurrentDegree,
//                    -azimuthinDegress,
//                    Animation.RELATIVE_TO_SELF, 0.5f,
//                    Animation.RELATIVE_TO_SELF, 0.5f
//                )
//                ra.duration = 500
//                ra.fillAfter = true
//                binding.ivCompass.startAnimation(ra)

                binding.tvCompassDegree.text = "${Utils.degreeFormat.format(azimuthinDegress)}°"

                mapLayerListAdapter.degree = azimuthinDegress
                mapLayerListAdapter.notifyDataSetChanged()

                binding.ivCompass.rotation = -azimuthinDegress


                // target, zoom, tilt, bearing
                val cameraPosition = CameraPosition(
                    naverMap.cameraPosition.target,
                    naverMap.cameraPosition.zoom,
                    naverMap.cameraPosition.tilt,
                    azimuthinDegress.toDouble()
                )

                val camera = CameraUpdate.toCameraPosition(cameraPosition)
                    .animate(CameraAnimation.Easing, 500)
                naverMap.moveCamera(camera)
                //naverMap.cameraPosition = cameraPosition

                mCurrentDegree = -azimuthinDegress
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

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

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val BTN_LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

}