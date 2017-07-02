package tingproject.testopensourceapplication.com.zmusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.AlbumList;
import com.ximalaya.ting.android.opensdk.model.category.Category;
import com.ximalaya.ting.android.opensdk.model.category.CategoryList;
import com.ximalaya.ting.android.opensdk.model.tag.Tag;
import com.ximalaya.ting.android.opensdk.model.tag.TagList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "qmyan";
    private static final String APP_SECRET = "2d5e40f87904d6cb292b6388e82680a7";

    private List<Category> mCategoryList = new ArrayList<Category>();//类别列表  有声书 、音乐、娱乐。。。。。
    private List<Tag> mTagList = new ArrayList<Tag>();//类别下的标签列表    悬疑、 言情  幻想  等
    private List<Album> mAlbumList = new ArrayList<Album>();//标签后专辑
    private List<Track> mTrackList = new ArrayList<Track>();//专辑下的声音

//    private Category mCategory;


    private XmPlayerManager mPlayerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayerManager = XmPlayerManager.getInstance(this);
        mPlayerManager.init();

        // sdk初始化
        CommonRequest.getInstanse().init(this, APP_SECRET);

        CommonRequest.getCategories(null, new IDataCallBack<CategoryList>() {
            @Override
            public void onSuccess(CategoryList list) {
//                for (Category c : list.getCategories()) {
//                    if (c.getCategoryName() == "儿童") {
//                        mCategory = c;
//                    }
//                }
                mCategoryList = list.getCategories();
                Log.d(TAG, "*****************所有分类列表**************");
                for (Category c : mCategoryList) {
                    Log.d(TAG, "CategoryName: " + c.getCategoryName() + ",Id: " + c.getId());
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "获取喜马拉雅FM内容分类失败");
            }
        });

        Map<String, String> tagDesc = new HashMap<String, String>();
        tagDesc.put(DTransferConstants.CATEGORY_ID, "6");
        tagDesc.put(DTransferConstants.TYPE, "0");
        CommonRequest.getTags(tagDesc, new IDataCallBack<TagList>() {
            @Override
            public void onSuccess(TagList list) {
                mTagList = list.getTagList();
                Log.d(TAG, "*****************儿童分类标签列表**************");
                for (Tag t : mTagList) {
                    Log.d(TAG, "TagName: " + t.getTagName());
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });

        Map<String, String> albumDesc = new HashMap<String, String>();
        albumDesc.put(DTransferConstants.CATEGORY_ID, "6");
        albumDesc.put(DTransferConstants.TAG_NAME, "儿歌大全");
        albumDesc.put(DTransferConstants.CALC_DIMENSION, "1");
        CommonRequest.getAlbumList(albumDesc, new IDataCallBack<AlbumList>() {
            @Override
            public void onSuccess(AlbumList list) {
                mAlbumList = list.getAlbums();
                Log.d(TAG, "*****************儿歌大全标签专辑列表**************");
                for (Album a : mAlbumList) {
                    Log.d(TAG, "AlbumTitle: " + a.getAlbumTitle() + ",Id: " + a.getId());
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });

        Map<String, String> trackDesc = new HashMap<String, String>();
        trackDesc.put(DTransferConstants.ALBUM_ID, "267101");
        trackDesc.put(DTransferConstants.SORT, "asc");
        CommonRequest.getTracks(trackDesc, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(TrackList list) {
                mTrackList = list.getTracks();
                Log.d(TAG, "*****************天天听儿歌声音列表**************");
                for (Track t : mTrackList) {
                    Log.d(TAG, t.getTrackTitle() + "  " + t.getPlayUrlAmr());
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    public void play(View view) {
        Log.d(TAG, "play");
        mPlayerManager.playList(mTrackList, 0);
        mPlayerManager.play();
    }

    public void stop(View view) {
        Log.d(TAG, "stop");
        mPlayerManager.stop();
    }

    public void pause(View view) {
        Log.d(TAG, "pause");
        mPlayerManager.pause();
    }

    public void next(View view) {
        Log.d(TAG, "next");
        mPlayerManager.playNext();
    }

    public void preview(View view) {
        Log.d(TAG, "preview");
        mPlayerManager.playPre();
    }

    @Override
    protected void onDestroy() {
        mPlayerManager.release();
        super.onDestroy();
    }
}
