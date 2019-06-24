package gig.com.google;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.ImageTiledLayer;
import com.esri.arcgisruntime.layers.WebTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private MapView mMapView;
    private Button vectorMap,aMapImage,aMapVector,road,imageMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.map_view);
        vectorMap = (Button) findViewById(R.id.vector);
        aMapImage = (Button) findViewById(R.id.amapimage);
        aMapVector = (Button) findViewById(R.id.amapvector);
        road = (Button) findViewById(R.id.road);
        imageMap = (Button) findViewById(R.id.image_map);

        vectorMap.setOnClickListener(this);
        aMapVector.setOnClickListener(this);
        aMapImage.setOnClickListener(this);
        road.setOnClickListener(this);
        imageMap.setOnClickListener(this);
    }


    private void setGoogleMap(GoogleMapLayer.MapType mapType) {
        ImageTiledLayer imageTiledLayer = new GoogleMapLayer(mapType,
                GoogleMapLayer.buildTileInfo(this),
                new Envelope(-22041257.773878001D,
                -32673939.672751699D, 22041257.773878001D,
                20851350.0432886D, SpatialReferences.getWebMercator()));
        ArcGISMap map = new ArcGISMap(new Basemap(imageTiledLayer));
        mMapView.getGraphicsOverlays().clear();//可能错误
        mMapView.setMap(map);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.pause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.vector:
                setGoogleMap(GoogleMapLayer.MapType.VECTOR);
                break;
            case R.id.amapimage:
                setGoogleMap(GoogleMapLayer.MapType.AMapImage);
                break;
            case R.id.amapvector:
                setGoogleMap(GoogleMapLayer.MapType.AMapVector);
                break;
            case R.id.road:
                setGoogleMap(GoogleMapLayer.MapType.ROAD);
                break;
            case R.id.image_map:
                setGoogleMap(GoogleMapLayer.MapType.IMAGE);
                break;
        }
    }

}
