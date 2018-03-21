package com.dmitry.unsplashphotos.sevices.interfaces;

import com.dmitry.unsplashphotos.entities.ImageItem;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IUnsplashApi {

    @GET("photos/?&per_page=30&page=1&order_by=popular")
    Call<List<ImageItem>> getArrayImageItem();
}
