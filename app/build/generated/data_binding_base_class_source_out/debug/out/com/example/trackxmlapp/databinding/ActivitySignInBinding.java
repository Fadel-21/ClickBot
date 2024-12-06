// Generated by view binder compiler. Do not edit!
package com.example.trackxmlapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.trackxmlapp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivitySignInBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button btnSignIn;

  @NonNull
  public final AppCompatEditText etEmailSignIn;

  @NonNull
  public final AppCompatEditText etPasswordSignIn;

  @NonNull
  public final LinearLayout main;

  @NonNull
  public final Toolbar toolbarSignInActivity;

  @NonNull
  public final TextView tvTitle;

  private ActivitySignInBinding(@NonNull LinearLayout rootView, @NonNull Button btnSignIn,
      @NonNull AppCompatEditText etEmailSignIn, @NonNull AppCompatEditText etPasswordSignIn,
      @NonNull LinearLayout main, @NonNull Toolbar toolbarSignInActivity,
      @NonNull TextView tvTitle) {
    this.rootView = rootView;
    this.btnSignIn = btnSignIn;
    this.etEmailSignIn = etEmailSignIn;
    this.etPasswordSignIn = etPasswordSignIn;
    this.main = main;
    this.toolbarSignInActivity = toolbarSignInActivity;
    this.tvTitle = tvTitle;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySignInBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySignInBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_sign_in, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySignInBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_sign_in;
      Button btnSignIn = ViewBindings.findChildViewById(rootView, id);
      if (btnSignIn == null) {
        break missingId;
      }

      id = R.id.et_email_sign_in;
      AppCompatEditText etEmailSignIn = ViewBindings.findChildViewById(rootView, id);
      if (etEmailSignIn == null) {
        break missingId;
      }

      id = R.id.et_password_sign_in;
      AppCompatEditText etPasswordSignIn = ViewBindings.findChildViewById(rootView, id);
      if (etPasswordSignIn == null) {
        break missingId;
      }

      LinearLayout main = (LinearLayout) rootView;

      id = R.id.toolbar_sign_in_activity;
      Toolbar toolbarSignInActivity = ViewBindings.findChildViewById(rootView, id);
      if (toolbarSignInActivity == null) {
        break missingId;
      }

      id = R.id.tv_title;
      TextView tvTitle = ViewBindings.findChildViewById(rootView, id);
      if (tvTitle == null) {
        break missingId;
      }

      return new ActivitySignInBinding((LinearLayout) rootView, btnSignIn, etEmailSignIn,
          etPasswordSignIn, main, toolbarSignInActivity, tvTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
