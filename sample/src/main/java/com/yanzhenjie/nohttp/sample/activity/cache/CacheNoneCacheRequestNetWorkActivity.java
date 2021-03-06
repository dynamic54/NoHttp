/*
 * Copyright 2015 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.nohttp.sample.activity.cache;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanzhenjie.nohttp.sample.R;
import com.yanzhenjie.nohttp.sample.activity.BaseActivity;
import com.yanzhenjie.nohttp.sample.adapter.RecyclerListSingleAdapter;
import com.yanzhenjie.nohttp.sample.nohttp.CallServer;
import com.yanzhenjie.nohttp.sample.nohttp.HttpListener;
import com.yanzhenjie.nohttp.sample.util.Constants;
import com.yanzhenjie.nohttp.sample.util.OnItemClickListener;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.CacheMode;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import java.util.Arrays;
import java.util.List;

/**
 * <p>如果缓存为空才去请求网络。</p>
 * Created by YOLANDA on 2016/3/13.
 *
 * @author Yan Zhenjie.
 */
public class CacheNoneCacheRequestNetWorkActivity extends BaseActivity {
    /**
     * 显示String或Json类型的请求结果。
     */
    private TextView mTvResult;
    /**
     * 显示图片。
     */
    private ImageView mIvResult;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cache_demo);

        List<String> cacheDataTypes = Arrays.asList(getResources().getStringArray(R.array.activity_cache_item));
        RecyclerListSingleAdapter listAdapter = new RecyclerListSingleAdapter(cacheDataTypes, mItemClickListener);
        RecyclerView recyclerView = findView(R.id.rv_cache_demo_activity);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(listAdapter);
    }

    private OnItemClickListener mItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            if (0 == position) {// 请求String。
                requestString();
            } else if (1 == position) {// 请求图片。
                requestImage();
            }
        }
    };

    /**
     * 请求String。
     */
    private void requestString() {
        Request<String> request = NoHttp.createStringRequest(Constants.URL_NOHTTP_METHOD);
        request.setCacheKey("CacheKeyNoneCacheRequestNetworkString");// 这里的key是缓存数据的主键，默认是url，使用的时候要保证全局唯一，否则会被其他相同url数据覆盖。
        request.setCacheMode(CacheMode.NONE_CACHE_REQUEST_NETWORK);//设置为NONE_CACHE_REQUEST_NETWORK表示先去读缓存，如果没有缓存才请求服务器。
        CallServer.getRequestInstance().add(this, 0, request, stringHttpListener, false, true);
    }

    private HttpListener<String> stringHttpListener = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            String string = response.isFromCache() ? getString(R.string.request_from_cache) : getString(R.string.request_from_network);
            showMessageDialog(string, response.get());
        }

        @Override
        public void onFailed(int what, Response<String> response) {
            showMessageDialog(R.string.request_failed, response.getException().getMessage());
        }
    };

    /**
     * 请求Image。
     */
    private void requestImage() {
        Request<Bitmap> request = NoHttp.createImageRequest(Constants.URL_NOHTTP_IMAGE);
        request.setCacheKey("CacheKeyNoneCacheRequestNetworkImage");// 这里的key是缓存数据的主键，默认是url，使用的时候要保证全局唯一，否则会被其他相同url数据覆盖。
        request.setCacheMode(CacheMode.NONE_CACHE_REQUEST_NETWORK);//设置为NONE_CACHE_REQUEST_NETWORK表示先去读缓存，如果没有缓存才请求服务器。
        CallServer.getRequestInstance().add(this, 0, request, imageHttpListener, false, true);
    }

    private HttpListener<Bitmap> imageHttpListener = new HttpListener<Bitmap>() {
        @Override
        public void onSucceed(int what, Response<Bitmap> response) {
            String string = response.isFromCache() ? getString(R.string.request_from_cache) : getString(R.string.request_from_network);
            showImageDialog(string, response.get());
        }

        @Override
        public void onFailed(int what, Response<Bitmap> response) {
            showMessageDialog(R.string.request_failed, response.getException().getMessage());
        }
    };
}
