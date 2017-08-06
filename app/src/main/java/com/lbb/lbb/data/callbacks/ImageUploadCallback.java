package com.lbb.lbb.data.callbacks;

/**
 * Created by hitesh on 8/6/17.
 */

public interface ImageUploadCallback {

    public void onNext();

    public void onCompleted();

    public void onError();
}
