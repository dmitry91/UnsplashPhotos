package com.dmitry.unsplashphotos.sevices;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.dmitry.unsplashphotos.MainActivity;
import com.dmitry.unsplashphotos.entities.ImageItem;
import com.dmitry.unsplashphotos.sevices.http.HeaderInterceptor;
import com.dmitry.unsplashphotos.sevices.http.TLSSocketFactory;
import com.dmitry.unsplashphotos.sevices.interfaces.IUnsplashApi;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UnsplashApi {
    //return list ImageItem from url
    public ArrayList<ImageItem> getArrayImageItem(String url) {
        final SSLSocketFactory[] tLSSocketFactory = {null};

        try {
            //it is necessary to include protocols on android 4.0-4.4
            SSLContext context = SSLContext.getInstance("TLSv1.2");
            context.init(null, null, null);
            tLSSocketFactory[0] = new TLSSocketFactory(context.getSocketFactory());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }

        OkHttpClient client = null;
        try {
            client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60,TimeUnit.SECONDS)
                    .sslSocketFactory(tLSSocketFactory[0], getTrustManager())
                    .addInterceptor(new HeaderInterceptor(MainActivity.UNSPLASH_USER_ID))//id for authorization
                    .build();
        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }

        assert client != null;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IUnsplashApi iUnsplashApi = retrofit.create(IUnsplashApi.class);
        Call<List<ImageItem>> image = iUnsplashApi.getArrayImageItem();

        ArrayList<ImageItem> imageItemArray = new ArrayList<>();
        try {
            imageItemArray = (ArrayList<ImageItem>) image.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageItemArray;
    }

    private X509TrustManager getTrustManager() throws NoSuchAlgorithmException, KeyStoreException {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);

        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

    public Bitmap loadImage(String urlPath) {
        Bitmap bmp = null;
        try {
            URL url = new URL(urlPath);
            bmp = BitmapFactory.decodeStream((InputStream) url.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }
}
