package com.example.dainq.smilenotes.controller.realm;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.model.CustomerObject;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Context context) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {
        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {
        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {
        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {
        return instance;
    }

    public Realm getRealm() {
        return realm;
    }

    //Refresh the realm istance
    public void refresh() {
        realm.refresh();
    }

    //clear all objects from CustomerObject.class
    public void clearAll() {
        realm.beginTransaction();
        realm.clear(CustomerObject.class);
        realm.commitTransaction();
    }

    public void deleteCustomer(int id) {
        realm.beginTransaction();
        CustomerObject customerObject = getCustomer(id);
        customerObject.removeFromRealm();
        realm.commitTransaction();
    }

    //find all objects in the CustomerObject.class
    public RealmResults<CustomerObject> getCustomers() {
        return realm.where(CustomerObject.class).findAll();
    }

    //query a single item with the given id
    public CustomerObject getCustomer(int id) {
        return realm.where(CustomerObject.class).equalTo("id", id).findFirst();
    }

    //check if CustomerObject.class is empty
    public boolean hasCustomers() {
        return !realm.allObjects(CustomerObject.class).isEmpty();
    }

    //query example
    public RealmResults<CustomerObject> queryedCustomers(int key) {
        if (key == Constant.CUSTOMER_TYPE_NEW || key == Constant.CUSTOMER_TYPE_NEW_MONTH) {
            return realm.where(CustomerObject.class)
                    .equalTo(Constant.KEY_LEVEL_CUSTOMER, Constant.CUSTOMER_LEVEL_0)
                    .findAll();
        }

        if (key == Constant.CUSTOMER_TYPE_CONSUMER) {
            return realm.where(CustomerObject.class)
                    .equalTo(Constant.KEY_LEVEL_CUSTOMER, Constant.CUSTOMER_LEVEL_1)
                    .or()
                    .equalTo(Constant.KEY_LEVEL_CUSTOMER, Constant.CUSTOMER_LEVEL_2)
                    .findAll();
        }

        if (key == Constant.CUSTOMER_TYPE_DISTRIBUTION) {
            return realm.where(CustomerObject.class)
                    .equalTo(Constant.KEY_LEVEL_CUSTOMER, Constant.CUSTOMER_LEVEL_3)
                    .or()
                    .equalTo(Constant.KEY_LEVEL_CUSTOMER, Constant.CUSTOMER_LEVEL_4)
                    .findAll();
        }
        return realm.where(CustomerObject.class).findAll();
    }

    public RealmResults<CustomerObject> searchCustomers(String query) {
        return realm.where(CustomerObject.class)
                .contains(Constant.CUSTOMER_NAME, query, Case.INSENSITIVE)
                .or()
                .contains(Constant.CUSTOMER_ADA, query, Case.INSENSITIVE)
                .or()
                .contains(Constant.CUSTOMER_PHONE_NUMBER, query)
                .findAll();
    }

    public RealmResults<CustomerObject> sortCustomerByDate(String date) {
        return realm.where(CustomerObject.class)
                .findAllSorted(date, Sort.DESCENDING);
    }
}
