package com.prabhutech.prabhupackages.wallet.kyc;

import static com.prabhutech.prabhupackages.wallet.core.api.utils.JsonUtils.safeBoolean;
import static com.prabhutech.prabhupackages.wallet.core.api.utils.JsonUtils.safeString;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.prabhutech.prabhupackages.wallet.core.api.contracts.KycContracts;
import com.prabhutech.prabhupackages.wallet.core.utils.FileUtils;

import java.io.File;
import java.io.Serializable;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class KycDetails implements Serializable {
    public String Nationality;

    public String FirstName;
    public String MiddleName;
    public String LastName;
    public String DobEng;
    public String DobNep;
    public String Gender;
    public String Occupation;
    public String FatherName;
    public String MotherName;
    public String GrandFatherName;
    public String GrandMotherName;
    public String MaritalStatus;
    public String Spouse;

    public String PContactState;
    public String PContactDistrict;
    public String PLocalBody;
    public String PWardNo;
    public String PAddress;
    public String TContactState;
    public String TContactDistrict;
    public String TLocalBody;
    public String TWardNo;
    public String TAddress;
    public boolean isTempSameAsPerm;
    public String Email;

    public String IdentificationType;
    public String IdentificationNo;
    public String IdIssuedFrom;
    public String IdIssuedDateEng;
    public String IdIssuedDateNep;
    public String IdFrontImgBlob;
    public String IdBackImgBlob;
    public String CustomerImgBlob;
    public String DisabilityVerificationDocUrl;

    public Uri IdFrontImg;
    public Uri IdBackImg;
    public Uri CustomerImg;

    public String IdFrontImgUrl = "";
    public String IdBackImgUrl = "";
    public String CustomerImgUrl = "";

    public Uri DisabilityVerificationDoc;

    public boolean IsDisabled;
    public String DisabilityType;


    /**
     * Generate Json object to be sent as request
     *
     * @param model Kyc model object
     * @return
     */
    public static JsonObject mapModelToJson(KycDetails model) {
        JsonObject obj = new JsonObject();
        obj.addProperty("FirstName", model.FirstName);

        return obj;
    }

    public static KycDetails mapJsonToModel(JsonObject jsondata) {
        KycDetails details = new KycDetails();
        details.Nationality = safeString(jsondata.get("nationality"), KycContracts.NEPALESE);

        details.FirstName = safeString(jsondata.get("firstName"), "");
        details.MiddleName = safeString(jsondata.get("middleName"), "");
        details.LastName = safeString(jsondata.get("lastName"), "");
        details.DobEng = safeString(jsondata.get("dobEng"), "");
        details.DobNep = safeString(jsondata.get("dobNep"), "");
        details.Gender = safeString(jsondata.get("gender"), "");
        details.Occupation = safeString(jsondata.get("occupation"), "");
        details.FatherName = safeString(jsondata.get("fatherName"), "");
        details.MotherName = safeString(jsondata.get("motherName"), "");
        details.GrandFatherName = safeString(jsondata.get("grandFatherName"), "");
        details.GrandMotherName = safeString(jsondata.get("grandMotherName"), "");
        details.MaritalStatus = safeString(jsondata.get("maritalStatus"), "");
        details.Spouse = safeString(jsondata.get("spouse"), "");

        details.PContactState = safeString(jsondata.get("pContactStateCode"), "");
        details.PContactDistrict = safeString(jsondata.get("pContactDistrictCode"), "");
        details.PLocalBody = safeString(jsondata.get("pLocalBodyCode"), "");
        details.PWardNo = safeString(jsondata.get("pWardNo"), "");
        details.PAddress = safeString(jsondata.get("pAddress"), "");
        details.TContactState = safeString(jsondata.get("tContactStateCode"), "");
        details.TContactDistrict = safeString(jsondata.get("tContactDistrictCode"), "");
        details.TLocalBody = safeString(jsondata.get("tLocalBodyCode"), "");
        details.TWardNo = safeString(jsondata.get("tWardNo"), "");
        details.TAddress = safeString(jsondata.get("tAddress"), "");
        details.Email = safeString(jsondata.get("email"), "");

        details.IdentificationType = safeString(jsondata.get("identificationType"), "");
        details.IdentificationNo = safeString(jsondata.get("identificationNo"), "");
        details.IdIssuedFrom = safeString(jsondata.get("idIssuedFromCode"), "");
        details.IdIssuedDateEng = safeString(jsondata.get("idIssuedDateEng"), "");
        details.IdIssuedDateNep = safeString(jsondata.get("idIssuedDateNep"), "");
        details.IdFrontImgBlob = safeString(jsondata.get("idFrontImg"), "");
        details.IdBackImgBlob = safeString(jsondata.get("idBackImg"), "");
        details.CustomerImgBlob = safeString(jsondata.get("customerImg"), "");

        details.IsDisabled = safeBoolean(jsondata.get("isDisabled"), false);
        details.DisabilityType = safeString(jsondata.get("disabilityType"), "");

        details.IdFrontImg = null;
        details.IdBackImg = null;
        details.CustomerImg = null;

        details.IdFrontImgUrl = safeString(jsondata.get("idFrontImg"), "");
        details.IdBackImgUrl = safeString(jsondata.get("idBackImg"), "");
        details.CustomerImgUrl = safeString(jsondata.get("customerImg"), "");
        details.DisabilityVerificationDocUrl = safeString(jsondata.get("disabilityVerificationDoc"), "");

        return details;
    }

    public Map<String, RequestBody> getMapped() {
        Map<String, RequestBody> map = new HashMap<>();
//        map.put("KycOtp", createPartFromString(KycOtp));
        map.put("Email", createPartFromString(Email));
        map.put("FirstName", createPartFromString(FirstName));
        map.put("MiddleName", createPartFromString(MiddleName));
        map.put("LastName", createPartFromString(LastName));
        map.put("DobEng", createPartFromString(DobEng));
        map.put("DobNep", createPartFromString(DobNep));
        map.put("Gender", createPartFromString(Gender));
        map.put("Occupation", createPartFromString(Occupation));
        map.put("FatherName", createPartFromString(FatherName));
        map.put("MotherName", createPartFromString(MotherName));
        map.put("GrandFatherName", createPartFromString(GrandFatherName));
        map.put("GrandMotherName", createPartFromString(GrandMotherName));
        map.put("IdentificationType", createPartFromString(IdentificationType));
        map.put("IdentificationNo", createPartFromString(IdentificationNo));
        map.put("IdIssuedFrom", createPartFromString(IdIssuedFrom));
        map.put("IdIssuedDateEng", createPartFromString(IdIssuedDateEng));
        map.put("IdIssuedDateNep", createPartFromString(IdIssuedDateNep));
        map.put("IdFrontImgBlob", createPartFromString(IdFrontImgBlob));
        map.put("IdBackImgBlob", createPartFromString(IdBackImgBlob));
        map.put("CustomerImgBlob", createPartFromString(CustomerImgBlob));
        map.put("PContactState", createPartFromString(PContactState));
        map.put("PContactDistrict", createPartFromString(PContactDistrict));
        map.put("PLocalBody", createPartFromString(PLocalBody));
        map.put("PWardNo", createPartFromString(PWardNo));
        map.put("PAddress", createPartFromString(PAddress));
        map.put("TContactState", createPartFromString(TContactState));
        map.put("TContactDistrict", createPartFromString(TContactDistrict));
        map.put("TLocalBody", createPartFromString(TLocalBody));
        map.put("TWardNo", createPartFromString(TWardNo));
        map.put("TAddress", createPartFromString(TAddress));
        map.put("MaritalStatus", createPartFromString(MaritalStatus));
        map.put("Spouse", createPartFromString(Spouse));
        map.put("Nationality", createPartFromString(Nationality));
        map.put("IsDisabled", createPartFromString(IsDisabled? "True": "False"));
        map.put("DisabilityType", createPartFromString(DisabilityType));
        return map;
    }

    public List<MultipartBody.Part> getFileParts(Context context) {
        List<MultipartBody.Part> files = new ArrayList<>();
        if (CustomerImg != null) {
            files.add(prepareFilePart(context, "CustomerImg", CustomerImg));
        }
        if (IdFrontImg != null) {
            files.add(prepareFilePart(context, "IdFrontImg", IdFrontImg));
        }
        if (IdBackImg != null) {
            files.add(prepareFilePart(context, "IdBackImg", IdBackImg));
        }

        if (DisabilityVerificationDoc != null) {
            files.add(prepareFilePart(context, "DisabilityVerificationDoc", DisabilityVerificationDoc));
        }

        return files;
    }

    public static RequestBody createPartFromString(String descriptionString) {
        descriptionString = descriptionString != null ? descriptionString : "";
        return RequestBody.create(
                MultipartBody.FORM, descriptionString);
    }


    private static MultipartBody.Part prepareFilePart(@NonNull Context context, @NonNull String partName, @NonNull Uri fileUri) {

        try {
            File file = FileUtils.getFile(context, fileUri);
            String contentType = context.getContentResolver().getType(fileUri);
            // create RequestBody instance from file
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            RequestBody requestFile = RequestBody.create(
                    MediaType.parse(mimeType), file);

            // MultipartBody.Part is used to send also the actual file name
            return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
