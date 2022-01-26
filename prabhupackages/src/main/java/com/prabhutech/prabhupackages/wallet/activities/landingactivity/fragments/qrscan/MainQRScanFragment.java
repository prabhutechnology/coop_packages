package com.prabhutech.prabhupackages.wallet.activities.landingactivity.fragments.qrscan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.prabhutech.prabhupackages.wallet.utils.ImageUtils;
import com.prabhutech.prabhupackages.R;
import com.prabhutech.prabhupackages.databinding.FragmentMainQRScanBinding;

import java.io.IOException;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainQRScanFragment extends Fragment implements ZXingScannerView.ResultHandler {
    private static final String TAG = "MainQRScanFragment";

    private FragmentMainQRScanBinding binding;

    public static final int PICK_IMAGE = 1088;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_q_r_scan, container, false);

        binding.switchFlash.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            binding.scannerView.setFlash(compoundButton.isChecked());
        });

        binding.gallerySelect.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE) {
            try {
                if (data == null) {
                    return;
                }
                Uri uri = data.getData();
                String contents = null;
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), uri);
                bitmap = ImageUtils.scaleDownLargeBitmap(bitmap);
                int[] intArray = new int[bitmap.getWidth() * bitmap.getHeight()];
                bitmap.getPixels(intArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
                ;
                //
                LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), intArray);
                BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
                //
                Reader reader = new MultiFormatReader();
                Result result = reader.decode(binaryBitmap);
                handleResult(result);

            } catch (IOException | FormatException | ChecksumException | NotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getContext(), "Back", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void handleResult(Result rawResult) {
// Do something with the result here
        Log.v(TAG, rawResult.getText()); // Prints scan results
        Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        // If you would like to resume scanning, call this method below:
        binding.scannerView.resumeCameraPreview(this);
        String barcode = rawResult.getText();

        /*
        JsonObject req = new JsonObject();
        req.addProperty("qrCodeId", barcode);
        if (QRScanActivity.qrTunnel.equals(SendMoneyActivity.QR_TUNNEL_DASH)) {
            checkQrCodeValidity(barcode);
            return;
        } else {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", barcode);
            ((QRScanActivity) context).setResult(Activity.RESULT_OK, returnIntent);
            ((QRScanActivity) context).finish();
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();

        binding.scannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        binding.scannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.scannerView.stopCamera();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}