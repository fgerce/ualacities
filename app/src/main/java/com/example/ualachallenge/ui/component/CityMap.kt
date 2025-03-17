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
                osmdroidBasePath = File(context.filesDir, "osmdroid") // Usa almacenamiento interno
                osmdroidTileCache = File(osmdroidBasePath, "tiles")
            }

            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK) // Usa OpenStreetMap
                setMultiTouchControls(true) // Permite hacer zoom con los dedos

                val mapController = controller
                mapController.setZoom(10.0)
                val startPoint = GeoPoint(lat, lon)
                mapController.setCenter(startPoint)

                val marker = Marker(this)
                marker.position = startPoint
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                overlays.add(marker)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
