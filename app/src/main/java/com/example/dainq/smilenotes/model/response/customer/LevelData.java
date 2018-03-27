package com.example.dainq.smilenotes.model.response.customer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LevelData {
    @SerializedName("potential")
    @Expose
    private int potential;

    @SerializedName("potentialMonth")
    @Expose
    private int potentialMonth;

    @SerializedName("consumer")
    @Expose
    private int consumer;

    @SerializedName("distributor")
    @Expose
    private int distributor;

    public int getPotential() {
        return potential;
    }

    public void setPotential(int potential) {
        this.potential = potential;
    }

    public int getPotentialMonth() {
        return potentialMonth;
    }

    public void setPotentialMonth(int potentialMonth) {
        this.potentialMonth = potentialMonth;
    }

    public int getConsumer() {
        return consumer;
    }

    public void setConsumer(int consumer) {
        this.consumer = consumer;
    }

    public int getDistributor() {
        return distributor;
    }

    public void setDistributor(int distributor) {
        this.distributor = distributor;
    }
}
