package gig.com.google;

import android.app.Activity;
import android.util.Log;
import com.esri.arcgisruntime.arcgisservices.LevelOfDetail;
import com.esri.arcgisruntime.arcgisservices.TileInfo;
import com.esri.arcgisruntime.data.TileKey;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.ImageTiledLayer;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GoogleMapLayer extends ImageTiledLayer {

    // 枚举
    public enum MapType {
        VECTOR,        //矢量标注地图
        IMAGE,        //影像地图
        ROAD,        //道路标注图层
        AMapImage,
        AMapVector
    }

    private static GoogleMapLayer googleMapLayer;

    private static TileInfo mTileInfo;
    private MapType mMapType;
    private Activity mactivity;

    public static double[] iScale =
            {
                    591657527.591555,
                    295828763.795777,
                    147914381.897889,
                    73957190.948944,
                    36978595.474472,
                    18489297.737236,
                    9244648.868618,
                    4622324.434309,
                    2311162.217155,
                    1155581.108577,
                    577790.554289,
                    288895.277144,
                    144447.638572,
                    72223.819286,
                    36111.909643,
                    18055.954822,
                    9027.977411,
                    4513.988705,
                    2256.994353,
                    1128.497176,
            };
    public static double[] iRes =
            {
                    156543.033928,
                    78271.5169639999,
                    39135.7584820001,
                    19567.8792409999,
                    9783.93962049996,
                    4891.96981024998,
                    2445.98490512499,
                    1222.99245256249,
                    611.49622628138,
                    305.748113140558,
                    152.874056570411,
                    76.4370282850732,
                    38.2185141425366,
                    19.1092570712683,
                    9.55462853563415,
                    4.77731426794937,
                    2.38865713397468,
                    1.19432856685505,
                    0.597164283559817,
                    0.298582141647617,
            };


    public GoogleMapLayer(MapType mapType,TileInfo tileInfo, Envelope fullExtent) {
        super(tileInfo, fullExtent);
        this.mMapType = mapType;

//        setBufferSize(BufferSize.MEDIUM);

    }

    public static GoogleMapLayer getInstance(MapType mapType,TileInfo tileInfo, Envelope fullExtent){

        if (googleMapLayer==null){
            googleMapLayer=new GoogleMapLayer(mapType,tileInfo,fullExtent);
        }
        return googleMapLayer;
    }


    private void initLayer() {
//        tile
    }



    @Override
    protected byte[] getTile(TileKey tileKey) {
        byte[] iResult = null;

        try {
            URL iURL = null;
            byte[] iBuffer = new byte[1024];
            HttpURLConnection iHttpURLConnection = null;
            BufferedInputStream iBufferedInputStream = null;
            ByteArrayOutputStream iByteArrayOutputStream = null;

            iURL = new URL(this.getMapUrl(tileKey));
            iHttpURLConnection = (HttpURLConnection) iURL.openConnection();
            iHttpURLConnection.connect();
            iBufferedInputStream = new BufferedInputStream(iHttpURLConnection.getInputStream());
            iByteArrayOutputStream = new ByteArrayOutputStream();
            while (true) {
                int iLength = iBufferedInputStream.read(iBuffer);
                if (iLength > 0) {
                    iByteArrayOutputStream.write(iBuffer, 0, iLength);
                } else {
                    break;
                }
            }
            iBufferedInputStream.close();
            iHttpURLConnection.disconnect();

            iResult = iByteArrayOutputStream.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return iResult;
    }

    private String getMapUrl(TileKey tileKey) {
        String iResult = null;
//        Random iRandom = null;
        int level=tileKey.getLevel();
        int col=tileKey.getColumn();
        int row=tileKey.getRow();
//        iResult = "http://mt";
//        iRandom = new Random();
//        iResult = iResult + iRandom.nextInt(4);
        switch (this.mMapType) {
            case VECTOR:
//                iResult = iResult + ".google.cn/vt/lyrs=m@212000000&hl=zh-CN&gl=CN&src=app&x=" + col + "&y=" + row + "&z=" + level + "&s==Galil";
                iResult = "http://mt" + col % 4 + ".google.cn/vt/lyrs=m@177000000&v=w2.114&hl=zh-CN&gl=cn&" + "x=" + col + "&" + "y=" + row + "&" + "z=" + level + "&s=Galil";
                break;
            case IMAGE:
//                iResult = iResult + ".google.cn/vt/lyrs=s@126&hl=zh-CN&gl=CN&src=app&x=" + col + "&y=" + row + "&z=" + level + "&s==Galil";
                iResult = "http://mt" + col % 4 + ".google.cn/vt/lyrs=s&v=w2.114&hl=zh-CN&gl=cn&" + "x=" + col + "&" + "y=" + row + "&" + "z=" + level + "&s=Galil";
                break;
            case ROAD:
//                iResult = iResult + ".google.cn/vt/imgtp=png32&lyrs=h@212000000&hl=zh-CN&gl=CN&src=app&x=" + col + "&y=" + row + "&z=" + level + "&s==Galil";
                iResult = "http://mt" + col % 4 + ".google.cn/vt/imgtp=png32&lyrs=h@177000000&v=w2.114&hl=zh-CN&gl=cn&" + "x=" + col + "&" + "y=" + row + "&" + "z=" + level + "&s=Galil";
                break;
            case AMapImage:
                iResult = "http://webst0" + (col % 4 + 1) + ".is.autonavi.com/appmaptile?style=6&x=" + col + "&y=" + row + "&z=" + level;
                break;
            case AMapVector:
                iResult = "http://webst0" + (col % 4 + 1) + ".is.autonavi.com/appmaptile?style=7&x=" + col + "&y=" + row + "&z=" + level;
                break;
                default:
                return null;
        }
        return iResult;
    }

    @Override
    public TileInfo getTileInfo() {
//        MyLog.i("getTileInfo");
        return mTileInfo;
    }

    public static TileInfo buildTileInfo(Activity activity) {
//        Point iPoint = new Point(x,y,SpatialReference.create(102113));
        Point iPoint = new Point(-20037508.342787,20037508.342787, SpatialReference.create(102113));
        List<LevelOfDetail> levelOfDetails=new ArrayList<>();
        for (int i=0;i<iRes.length;i++){
            LevelOfDetail levelOfDetail=new LevelOfDetail(i,iRes[i],iScale[i]);
            levelOfDetails.add(levelOfDetail);
        }

        mTileInfo = new TileInfo(160, TileInfo.ImageFormat.UNKNOWN, levelOfDetails, iPoint, SpatialReference.create(102113), 256, 256);
        return mTileInfo;
    }
}
