package com.prabhutech.prabhupackages.wallet.core.api.contracts;

import android.util.Log;

import com.prabhutech.prabhupackages.BuildConfig;

public class UriContracts {
    private static final String TAG = "URICOnTRActs";
    public static final String URI_WALLET = "com.prabhutech.prabhupaywallet";
    public static final String URI_MERCHANT = "com.prabhutech.prabhupaymerchant";
    public static final String URI_TRANSACTION_FORM_CONTRACT;
    public static final String URI_URLSCONTRACTS;
    public static final String URI_API_NAME_GETTER;
    public static final String URI_MOCKRESPONSE;
    public static final String URI_FORM_POLICIES;
    public static final String APP_REPO;

    // repos
    public static final String URI_MOVIES_REPO;
    public static final String URI_FLIGHT_REPO;
    public static final String URI_BUS_REPO;
    public static final String URI_PRODUCTS_REPO;
    public static final String URI_USER_REPO;
    public static final String URI_API_CONTRACT;
    public static final String URI_STARTER_ACT;
    public static final String URI_KYC_REPO;
    public static final String URI_MISC_REPO;
    public static final String URI_TRANSACTION_REPO;
    public static final String URI_HOSPITAL_REPO;
    // TODO: 9/16/19 remove these two, and remove its implementations
    public static final String URI_HISTORY_BILLPAYMENT;
    public static final String URI_HISTORY_FUNDTRANSFER;
    public static final String URI_NOTIFICATION_REPO;
    public static final String URI_VOTING_REPO;
    public static final String URI_EVENT_TICKETING;
    //
    public static final String URI_DEVENT_TICKETING;


    static {
        String uri = BuildConfig.LIBRARY_PACKAGE_NAME;
        uri += ".wallet";
        Log.e(TAG, "static initializer: " + uri);
        URI_API_CONTRACT = uri + ".core.api.contracts.APIContracts";
        URI_TRANSACTION_FORM_CONTRACT = uri + ".contracts.forms.TransactionFormContract";

        URI_URLSCONTRACTS = uri + ".core.api.contracts.UrlsContracts";
        URI_API_NAME_GETTER = uri + ".core.api.ApiNames";
        URI_MOVIES_REPO = uri + ".core.repo.MoviesRepo";
        URI_FLIGHT_REPO = uri + ".core.repo.FlightRepo";
        URI_VOTING_REPO = uri + ".core.repo.VotingRepo";

        URI_BUS_REPO = uri + ".core.repo.BusRepo";
        URI_KYC_REPO = uri + ".core.repo.KycRepo";

        URI_MOCKRESPONSE = uri + ".core.mock.Mocks";

        URI_PRODUCTS_REPO = uri + ".core.repo.ProductsRepo";
        URI_TRANSACTION_REPO = uri + ".core.repo.TransactionRepo";
        URI_USER_REPO = uri + ".core.repo.UserRepo";

        URI_STARTER_ACT = uri + ".StarterActivity";

        URI_HISTORY_BILLPAYMENT = uri + ".core.repo.HistoryRepo";
        URI_HISTORY_FUNDTRANSFER = uri + ".core.repo.HistoryRepo";

        URI_FORM_POLICIES = uri + ".contracts.FormPolicies";

        URI_MISC_REPO = uri + ".core.repo.MiscRepo";
        URI_HOSPITAL_REPO = uri + ".core.repo.HospitalRepo";
        URI_NOTIFICATION_REPO = uri + ".core.repo.NotificationRepo";

        URI_EVENT_TICKETING = uri + ".core.repo.EventTicketRepo";
        URI_DEVENT_TICKETING = uri + ".core.repo.DEventsRepo";
        APP_REPO = uri + ".core.repo.AppRepo";
    }
}
