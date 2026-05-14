/*
 * Decompiled with CFR 0.151.
 */
package com.clemdefrance.sanction;

import com.clemdefrance.sanction.Type;
import java.util.UUID;

public class Sanction {
    private final int id;
    private final UUID uuid;
    private final Type type;
    private final String reason;
    private final String moderator;
    private final String date;
    private final long time;

    public Sanction(int id, UUID uuid, Type type, String reason, String moderator, String date, long time) {
        this.id = id;
        this.uuid = uuid;
        this.type = type;
        this.reason = reason;
        this.moderator = moderator;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return this.id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Type getType() {
        return this.type;
    }

    public String getReason() {
        return this.reason;
    }

    public String getModerator() {
        return this.moderator;
    }

    public String getDate() {
        return this.date;
    }

    public long getTime() {
        return this.time;
    }
}

