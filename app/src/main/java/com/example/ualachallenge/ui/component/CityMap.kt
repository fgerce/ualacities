package com.example.ualachallenge.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.io.File

@Composable
fun CityMap(lat: Double, lon: Double) {
    AndroidView(
        factory = { context ->
            Configuration.getInstance().apply {
                userAgentValue = context.packageName
                osmdroidBasePath = File(context.filesDir, "osmdroid")
                osmdroidTileCache = File(osmdroidBasePath, "tiles")
            }

            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
            }
        },
        update = { mapView ->
            val mapController = mapView.controller
            val newPoint = GeoPoint(lat, lon)

            mapController.setCenter(newPoint)
            mapController.setZoom(13.0)

            mapView.overlays.clear()
            val marker = Marker(mapView).apply {
                position = newPoint
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            }
            mapView.overlays.add(marker)
            mapView.invalidate()
        },
        modifier = Modifier.fillMaxSize()
    )
}
