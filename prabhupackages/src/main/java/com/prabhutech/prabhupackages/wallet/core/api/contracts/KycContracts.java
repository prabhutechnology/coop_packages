package com.prabhutech.prabhupackages.wallet.core.api.contracts;

import com.prabhutech.prabhupackages.wallet.core.utils.customviews.DatePickerView;
import com.prabhutech.prabhupackages.wallet.core.utils.customviews.DropdownSelectView;

import java.util.ArrayList;
import java.util.List;

public class KycContracts {
    public static final String NEPALESE = "Nepali";
    public static final String INDIAN = "Indian";

    public static String[] IdentificationTypesNP = {"Citizenship", "Driving License", "Passport"};

    public static List<DropdownSelectView.NameValue> identificationTypesIN() {
        List<DropdownSelectView.NameValue> list = new ArrayList<>();
        list.add(new DropdownSelectView.NameValue("IDCard By Indian Embassy of Nepal", "IDCardByIndianEmbassy"));
        return list;
    }


    public static List<Country> countries() {
        ArrayList<Country> countries = new ArrayList<>();
        countries.add(new Country("Nepal", "\uD83C\uDDF3\uD83C\uDDF5", "NP", "Nepali"));
        countries.add(new Country("India", "\uD83C\uDDEE\uD83C\uDDF3", "IN", "Indian"));

        return countries;
    }

    public static List<DropdownSelectView.NameValue> genders() {
        List<DropdownSelectView.NameValue> genderList = new ArrayList<>();
        genderList.add(new DropdownSelectView.NameValue("Male", "1"));
        genderList.add(new DropdownSelectView.NameValue("Female", "2"));
        genderList.add(new DropdownSelectView.NameValue("Other", "3"));

        return genderList;
    }

    public static String[] dateModes() {
        return new String[]{
                DatePickerView.AD,
                DatePickerView.BS
        };
    }

    public static String[] occupations() {
        return new String[]{
                "Service",
                "Private Sector",
                "Public Sector",
                "Government Sector",
                "Professional",
                "Self Employed",
                "Retired",
                "Housewife",
                "Student",
                "Business",
                "Others"
        };
    }

    public static String[] maritalStatuses() {
        return new String[]{
                "Single",
                "Married",
                "Divorced"
        };
    }

    public static class Country {
        public String Name;
        public String Flag;
        public String Code;
        public String Nationality;

        public Country() {
        }

        public Country(String name, String flag, String code, String nationality) {
            Name = name;
            Flag = flag;
            Code = code;
            Nationality = nationality;
        }

    }
}
