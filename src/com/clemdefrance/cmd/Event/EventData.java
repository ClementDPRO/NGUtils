/*
 * Decompiled with CFR 0.151.
 */
package com.clemdefrance.cmd.Event;

public class EventData {
    private final String name;
    private final double x;
    private final double y;
    private final double z;
    private final double staffX;
    private final double staffY;
    private final double staffZ;
    private String world;

    public EventData(String name, double x, double y, double z, double staffX, double staffY, double staffZ, String world) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.staffX = staffX;
        this.staffY = staffY;
        this.staffZ = staffZ;
        this.world = world;
    }

    public String getName() {
        return this.name;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public double getStaffX() {
        return this.staffX;
    }

    public double getStaffY() {
        return this.staffY;
    }

    public double getStaffZ() {
        return this.staffZ;
    }

    public String getWorld() {
        return this.world;
    }
}

