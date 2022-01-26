package com.prabhutech.prabhupackages.wallet.activities.landingactivity.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.FragmentNotificationBinding;
import com.prabhutech.prabhupackages.wallet.views.CallbackCollection;
import com.prabhutech.prabhupackages.wallet.views.ProgressTrigger;

public class NotificationFragment extends Fragment implements ProgressTrigger {
    private static final String TAG = "NotificationFragment";

    /* Variables */
    private static final String STATE_NO_DATA = "STATE_NO_DATA";
    private static final String STATE_ERROR = "STATE_ERROR";
    private static final String STATE_DATA = "STATE_DATA";

    private FragmentNotificationBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        binding.recyclerViewNotification.setLayoutManager(linearLayoutManager);

        binding.refreshPull.setOnRefreshListener(this::requestAllNotifications);

        binding.alternate.altImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_notifications_gray));

        CallbackCollection noDataViewToggler = CallbackCollection.__();
        noDataViewToggler.addToggler(STATE_DATA, s -> binding.alternate.alternate.setVisibility(View.GONE));
        noDataViewToggler.addToggler(STATE_ERROR, s -> {
            binding.alternate.alternate.setVisibility(View.VISIBLE);
            binding.alternate.altMessage.setText(R.string.no_notification);
        });

        noDataViewToggler.addToggler(STATE_NO_DATA, s -> {
            binding.alternate.alternate.setVisibility(View.VISIBLE);
            binding.alternate.altMessage.setText(R.string.no_notification);
        });

        binding.textViewClearNotification.setVisibility(View.GONE);

        return binding.getRoot();
    }

    private void requestAllNotifications() {
    }

    @Override
    public void setBusy(String reason, boolean isBusy) {
        binding.refreshPull.setRefreshing(isBusy);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}