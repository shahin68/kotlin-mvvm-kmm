// Generated by view binder compiler. Do not edit!
package com.applehealth.androidApp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.viewbinding.ViewBinding;
import com.applehealth.androidApp.R;
import com.applehealth.androidApp.views.switches.SwitchButton;
import com.applehealth.androidApp.views.volumeKnob.VolumeButton;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class MainFragmentBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Guideline guideline;

  @NonNull
  public final Guideline guideline2;

  @NonNull
  public final Button mainBtnOpenGallery;

  @NonNull
  public final SwitchButton mainIcCam;

  @NonNull
  public final SwitchButton mainIcMic;

  @NonNull
  public final SwitchButton mainIcPage;

  @NonNull
  public final SwitchButton mainIcRec;

  @NonNull
  public final ConstraintLayout mainView;

  @NonNull
  public final VolumeButton mainVolumeKnob;

  private MainFragmentBinding(@NonNull ConstraintLayout rootView, @NonNull Guideline guideline,
      @NonNull Guideline guideline2, @NonNull Button mainBtnOpenGallery,
      @NonNull SwitchButton mainIcCam, @NonNull SwitchButton mainIcMic,
      @NonNull SwitchButton mainIcPage, @NonNull SwitchButton mainIcRec,
      @NonNull ConstraintLayout mainView, @NonNull VolumeButton mainVolumeKnob) {
    this.rootView = rootView;
    this.guideline = guideline;
    this.guideline2 = guideline2;
    this.mainBtnOpenGallery = mainBtnOpenGallery;
    this.mainIcCam = mainIcCam;
    this.mainIcMic = mainIcMic;
    this.mainIcPage = mainIcPage;
    this.mainIcRec = mainIcRec;
    this.mainView = mainView;
    this.mainVolumeKnob = mainVolumeKnob;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static MainFragmentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static MainFragmentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.main_fragment, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static MainFragmentBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.guideline;
      Guideline guideline = rootView.findViewById(id);
      if (guideline == null) {
        break missingId;
      }

      id = R.id.guideline2;
      Guideline guideline2 = rootView.findViewById(id);
      if (guideline2 == null) {
        break missingId;
      }

      id = R.id.main_btn_open_gallery;
      Button mainBtnOpenGallery = rootView.findViewById(id);
      if (mainBtnOpenGallery == null) {
        break missingId;
      }

      id = R.id.main_ic_cam;
      SwitchButton mainIcCam = rootView.findViewById(id);
      if (mainIcCam == null) {
        break missingId;
      }

      id = R.id.main_ic_mic;
      SwitchButton mainIcMic = rootView.findViewById(id);
      if (mainIcMic == null) {
        break missingId;
      }

      id = R.id.main_ic_page;
      SwitchButton mainIcPage = rootView.findViewById(id);
      if (mainIcPage == null) {
        break missingId;
      }

      id = R.id.main_ic_rec;
      SwitchButton mainIcRec = rootView.findViewById(id);
      if (mainIcRec == null) {
        break missingId;
      }

      ConstraintLayout mainView = (ConstraintLayout) rootView;

      id = R.id.main_volume_knob;
      VolumeButton mainVolumeKnob = rootView.findViewById(id);
      if (mainVolumeKnob == null) {
        break missingId;
      }

      return new MainFragmentBinding((ConstraintLayout) rootView, guideline, guideline2,
          mainBtnOpenGallery, mainIcCam, mainIcMic, mainIcPage, mainIcRec, mainView,
          mainVolumeKnob);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}