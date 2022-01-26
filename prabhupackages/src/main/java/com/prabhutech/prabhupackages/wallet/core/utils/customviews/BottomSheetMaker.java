package com.prabhutech.prabhupackages.wallet.core.utils.customviews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.prabhutech.prabhupackages.wallet.core.utils.KeyboardUtils;

public class BottomSheetMaker extends BottomSheetDialogFragment {
    private int layout;
    private View rootView;

    private OnCreateView onCreateView;

    public BottomSheetMaker() {
    }

    public BottomSheetMaker(int layout, OnCreateView onCreateView) {
        this.layout = layout;
        this.onCreateView = onCreateView;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView= inflater.inflate(this.layout, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new KeyboardUtils(getActivity(), view);
        onCreateView.onViewCreated(view);

    }

    @Nullable
    @Override
    public View getView() {
        return rootView;
    }

    public interface OnCreateView {
        void onViewCreated(View view);
    }
}
