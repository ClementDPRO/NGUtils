/*
 * Decompiled with CFR 0.151.
 */
package com.clemdefrance.cmd.sanction;

public class BanInfo {
    public final boolean banned;
    public final String reason;
    public final long remainingSeconds;
    public final String date;

    public BanInfo(boolean banned, String reason, long remainingSeconds, String date) {
        this.banned = banned;
        this.reason = reason;
        this.remainingSeconds = remainingSeconds;
        this.date = date;
    }
}

