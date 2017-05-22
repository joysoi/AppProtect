package com.nikola.appprotect.services;

final class ActiveApplicationLockSyncObject {

    private static ActiveApplicationLockSyncObject instance;
    private volatile boolean checkCondition;
    private String packageName;

    static ActiveApplicationLockSyncObject getInstance() {
        if (instance == null) {
            synchronized (ActiveApplicationLockSyncObject.class) {
                if (instance == null) {
                    instance = new ActiveApplicationLockSyncObject();
                }
            }
        }
        return instance;
    }

    private ActiveApplicationLockSyncObject() {
        checkCondition = true;
    }

    synchronized void setCheck(boolean check) {
        this.checkCondition = check;
    }

    synchronized void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    synchronized ActiveApplication getCheckAndSameName() {
        return new ActiveApplication(checkCondition, packageName);
    }

    static final class ActiveApplication {

        final boolean check;
        final String samePackageName;

        ActiveApplication(boolean check, String samePackageName) {
            this.check = check;
            this.samePackageName = samePackageName;
        }
    }
}
