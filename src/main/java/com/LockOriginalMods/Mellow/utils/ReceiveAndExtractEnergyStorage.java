package com.LockOriginalMods.Mellow.utils;

import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;

public class ReceiveAndExtractEnergyStorage implements MellowEnergyStorage {
    protected int energy;
    protected int capacity;
    protected int maxTransfer;

    public ReceiveAndExtractEnergyStorage() {}

    public ReceiveAndExtractEnergyStorage(int energy, int capacity, int maxTransfer) {
        this.energy = energy;
        this.capacity = capacity;
        this.maxTransfer = maxTransfer;
    }

    @Override
    public int getEnergy() {
        return energy;
    }

    @Override
    public void setEnergy(int energy) {
        onChange();
        this.energy = energy;
    }

    @Override
    public void setEnergyWithoutUpdate(int energy) {
        this.energy = energy;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public void setCapacity(int capacity) {
        onChange();
        this.capacity = capacity;
    }

    @Override
    public void setCapacityWithoutUpdate(int capacity) {
        this.capacity = capacity;
    }

    public int getMaxTransfer() {
        return maxTransfer;
    }

    public void setMaxTransfer(int maxTransfer) {
        onChange();
        this.maxTransfer = maxTransfer;
    }

    public void setMaxTransferWithoutUpdate(int maxTransfer) {
        this.maxTransfer = maxTransfer;
    }

    protected void onChange() {}

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if(!canReceive())
            return 0;

        int received = Math.min(capacity - energy, Math.min(this.maxTransfer, maxReceive));

        if(!simulate) {
            onChange();
            energy += received;
        }

        return received;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if(!canExtract())
            return 0;

        int extracted = Math.min(energy, Math.min(this.maxTransfer, maxExtract));

        if(!simulate) {
            onChange();
            energy -= extracted;
        }

        return extracted;
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return capacity;
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    @Override
    public Tag saveNBT() {
        return IntTag.valueOf(energy);
    }

    @Override
    public void loadNBT(Tag tag) {
        if(!(tag instanceof IntTag))
            throw new IllegalArgumentException("Tag must be of type IntTag!");

        energy = ((IntTag)tag).getAsInt();
    }
}