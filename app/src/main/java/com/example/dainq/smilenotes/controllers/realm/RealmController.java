package com.example.dainq.smilenotes.controllers.realm;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.model.CustomerObject;
import com.example.dainq.smilenotes.model.MeetingObject;
import com.example.dainq.smilenotes.model.NotificationObject;
import com.example.dainq.smilenotes.model.ProductObject;

import java.util.Date;

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

    public void close() {
        if (realm.isInTransaction()) {
            realm.cancelTransaction();
        }

        if (!realm.isClosed()) {
            realm.close();
        }
    }

    /*---------------------CUSTOMER-------------------------*/
    //clear all objects from CustomerObject.class
    public void clearAll() {
        realm.beginTransaction();
        realm.clear(CustomerObject.class);
        realm.commitTransaction();
    }

    //delete customer by Id
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

    //add new customer
    public void addCustomer(CustomerObject object) {
        realm.beginTransaction();
        realm.copyToRealm(object);
        realm.commitTransaction();
    }

    //query a single item with the given id
    public CustomerObject getCustomer(int id) {
        return realm.where(CustomerObject.class).equalTo(Constant.KEY_ID, id).findFirst();
    }

    //query a count item with the given level
    public int getCountCustomer(int type) {
        RealmResults<CustomerObject> results;
        results = queryedCustomers(type);
        return results.size();
    }

    public int getCountCustomer(String date, Date start, Date end) {
        RealmResults<CustomerObject> results;
        results = queryedCustomers(date, start, end);
        return results.size();
    }

    //check ada is exit
    public boolean isExit(String ada) {
        CustomerObject results = realm.where(CustomerObject.class).equalTo("ada", ada).findFirst();
        return results != null;
    }

    //check if CustomerObject.class is empty
    public boolean hasCustomers() {
        return !realm.allObjects(CustomerObject.class).isEmpty();
    }

    //query example
    public RealmResults<CustomerObject> queryedCustomers(int key) {
        if (key == Constant.CUSTOMER_TYPE_NEW) {
            return realm.where(CustomerObject.class)
                    .equalTo(Constant.CUSTOMER_LEVEL, Constant.CUSTOMER_LEVEL_0)
                    .findAll();
        }

        if (key == Constant.CUSTOMER_TYPE_CONSUMER) {
            return realm.where(CustomerObject.class)
                    .equalTo(Constant.CUSTOMER_LEVEL, Constant.CUSTOMER_LEVEL_1)
                    .or()
                    .equalTo(Constant.CUSTOMER_LEVEL, Constant.CUSTOMER_LEVEL_2)
                    .findAll();
        }

        if (key == Constant.CUSTOMER_TYPE_DISTRIBUTION) {
            return realm.where(CustomerObject.class)
                    .equalTo(Constant.CUSTOMER_LEVEL, Constant.CUSTOMER_LEVEL_3)
                    .or()
                    .equalTo(Constant.CUSTOMER_LEVEL, Constant.CUSTOMER_LEVEL_4)
                    .findAll();
        }
        return getCustomers();
    }

    public RealmResults<CustomerObject> queryedCustomers(String date, Date start, Date end) {
        return realm.where(CustomerObject.class)
                .equalTo(Constant.CUSTOMER_LEVEL, Constant.CUSTOMER_LEVEL_0)
                .between(date, start, end)
                .findAll();
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


    /*-----------------------PLAN---------------------*/
    public void addPlan(MeetingObject object) {
        realm.beginTransaction();
        realm.copyToRealm(object);
        realm.commitTransaction();
    }

    public RealmResults<MeetingObject> getMeetings() {
        return realm.where(MeetingObject.class).findAll();
    }

    //query a single item with the given id
    public MeetingObject getMetting(int id) {
        return realm.where(MeetingObject.class)
                .equalTo(Constant.KEY_ID, id)
                .findFirst();
    }

    public RealmResults<MeetingObject> getMeetingOfCustomer(int id) {
        return realm.where(MeetingObject.class)
                .equalTo(Constant.KEY_ID_PLAN, id)
                .findAllSorted(Constant.PLAN_DATE, Sort.DESCENDING);
    }

    public void deleteMeeting(int id) {
        realm.beginTransaction();
        MeetingObject object = getMetting(id);
        object.removeFromRealm();
        realm.commitTransaction();
    }

    //check if CustomerObject.class is empty
    public boolean hasPlans() {
        return !realm.allObjects(MeetingObject.class).isEmpty();
    }

    public void removeAllPlan(final RealmResults<MeetingObject> realmResults) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmResults.clear();
            }
        });
    }


    /*----------------------PRODUCT------------------*/
    public void addProduct(ProductObject object) {
        realm.beginTransaction();
        realm.copyToRealm(object);
        realm.commitTransaction();
    }

    public RealmResults<ProductObject> getProducts(){
        return realm.where(ProductObject.class).findAll();
    }

    private ProductObject getProduct(int id) {
        return realm.where(ProductObject.class).equalTo(Constant.KEY_ID, id).findFirst();
    }

    public RealmResults<ProductObject> getProductOfCustomer(int id) {
        return realm.where(ProductObject.class)
                .equalTo(Constant.KEY_ID_PRODUCT, id)
                .findAllSorted(Constant.PRODUCT_USE_DATE, Sort.ASCENDING);
    }

    public void deleteProduct(int id) {
        realm.beginTransaction();
        ProductObject object = getProduct(id);
        object.removeFromRealm();
        realm.commitTransaction();
    }

    public RealmResults<ProductObject> getProductBetween(Date start, Date end, Sort type) {
        return realm.where(ProductObject.class)
                .between(Constant.PRODUCT_USE_DATE, start, end)
                .findAllSorted(Constant.PRODUCT_USE_DATE, type);
    }

    public void removeAllProduct(final RealmResults<ProductObject> realmResults) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmResults.clear();
            }
        });
    }

    /*-----------------Notification---------------*/

    public void addNotification(NotificationObject object) {
        realm.beginTransaction();
        realm.copyToRealm(object);
        realm.commitTransaction();
    }

    //getVia Id customer
    public RealmResults<NotificationObject> getNotificationOfCustomer(int id) {
        return realm.where(NotificationObject.class)
                .equalTo(Constant.NOTIFICATION_ID_CUSTOMER, id)
                .findAll();
    }

    //getVia Id customer
    public RealmResults<NotificationObject> getNotificationOfMetting(int id) {
        return realm.where(NotificationObject.class)
                .equalTo(Constant.NOTIFICATION_ID_MEETING, id)
                .findAll();
    }

    public NotificationObject getNotificationBirthDay(int idcustomer) {
        return realm.where(NotificationObject.class)
                .equalTo(Constant.NOTIFICATION_ID_CUSTOMER, idcustomer)
                .equalTo(Constant.NOTIFICATION_TYPE_NOTI, Constant.NOTIFICATION_BIRTH_DAY)
                .findFirst();
    }

    public RealmResults<NotificationObject> getListNotificationBirthDay(int idcustomer) {
        return realm.where(NotificationObject.class)
                .equalTo(Constant.NOTIFICATION_ID_CUSTOMER, idcustomer)
                .equalTo(Constant.NOTIFICATION_TYPE_NOTI, Constant.NOTIFICATION_BIRTH_DAY)
                .findAll();
    }

    public NotificationObject getNotificationPlan(int idmeeting) {
        return realm.where(NotificationObject.class)
                .equalTo(Constant.NOTIFICATION_ID_MEETING, idmeeting)
                .equalTo(Constant.NOTIFICATION_TYPE_NOTI, Constant.NOTIFICATION_EVENT)
                .findFirst();
    }

    //getAll until today
    public RealmResults<NotificationObject> getNotificationToday(Date date) {
        return realm.where(NotificationObject.class)
                .lessThanOrEqualTo(Constant.NOTIFICATION_DATEVALUE, date)
                .findAllSorted(Constant.NOTIFICATION_READ, Sort.ASCENDING,
                        Constant.NOTIFICATION_DATEVALUE, Sort.DESCENDING);
    }

    //getAll unread
    public RealmResults<NotificationObject> getNotificationUnread(Date date) {
        return realm.where(NotificationObject.class)
                .equalTo(Constant.NOTIFICATION_READ, false)
                .lessThanOrEqualTo(Constant.NOTIFICATION_DATEVALUE, date)
                .findAll();
    }

    public void updateNotiRead(NotificationObject notification) {
        realm.beginTransaction();
        notification.setIsread(true);
        realm.copyToRealmOrUpdate(notification);
        realm.commitTransaction();
    }

    public void clearAllNotification() {
        realm.beginTransaction();
        realm.clear(NotificationObject.class);
        realm.commitTransaction();
    }

    public void removeAllNotification(final RealmResults<NotificationObject> realmResults) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmResults.clear();
            }
        });
    }
}
