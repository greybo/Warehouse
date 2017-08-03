package com.stock.entity;

/**
 * Created by m on 23.07.2017.
 */

public class Meat {

    private String id;
    private long dateComing;
    private double weigthComing;
    private double priceComing;
    private long dateOutput;
    private double weigthOutput;
    private double priceOutput;
    private double profit;
    private boolean sync;

    public Meat() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDateComing() {
        return dateComing;
    }

    public void setDateComing(long dateComing) {
        this.dateComing = dateComing;
    }

    public double getWeigthComing() {
        return weigthComing;
    }

    public void setWeigthComing(double weigthComing) {
        this.weigthComing = weigthComing;
    }

    public double getPriceComing() {
        return priceComing;
    }

    public void setPriceComing(double priceComing) {
        this.priceComing = priceComing;
    }

    public long getDateOutput() {
        return dateOutput;
    }

    public void setDateOutput(long dateOutput) {
        this.dateOutput = dateOutput;
    }

    public double getWeigthOutput() {
        return weigthOutput;
    }

    public void setWeigthOutput(double weigthOutput) {
        this.weigthOutput = weigthOutput;
    }

    public double getPriceOutput() {
        return priceOutput;
    }

    public void setPriceOutput(double priceOutput) {
        this.priceOutput = priceOutput;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    @Override
    public String toString() {
        return "Meat{" +
                "id=" + id +
                ", dateComing=" + dateComing +
                ", weigthComing=" + weigthComing +
                ", priceComing=" + priceComing +
                ", dateOutput=" + dateOutput +
                ", weigthOutput=" + weigthOutput +
                ", priceOutput=" + priceOutput +
                ", profit=" + profit +
                ", sync=" + sync +
                '}';
    }
}
