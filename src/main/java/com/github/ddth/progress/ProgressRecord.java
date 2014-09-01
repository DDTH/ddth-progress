package com.github.ddth.progress;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * A progress-tracking record.
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since 0.1.0
 */
public class ProgressRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private long timestampStart = System.currentTimeMillis();
    private long timestampLastUpdate = System.currentTimeMillis();
    private double percent = 0.0;
    private long amountData = 0;
    private Object extraData;

    public String id() {
        return id;
    }

    public ProgressRecord id(String value) {
        this.id = value;
        return this;
    }

    public long timestampStart() {
        return timestampStart;
    }

    public long timestampLastUpdate() {
        return timestampLastUpdate;
    }

    public Object extraData() {
        return extraData;
    }

    public ProgressRecord extraData(Object value) {
        this.extraData = value;
        return this;
    }

    /**
     * How many percent has been completed?
     * 
     * <p>
     * Note: {@code 1.0d} is {@code 100.0%}!
     * </p>
     * 
     * @return
     */
    public double percent() {
        return percent;
    }

    public ProgressRecord percent(double percent) {
        this.timestampLastUpdate = System.currentTimeMillis();

        this.percent = percent;
        return this;
    }

    /**
     * (Optional) Amount of data has been processed so far.
     * 
     * @return
     */
    public long amountData() {
        return amountData;
    }

    public ProgressRecord amountData(long value) {
        this.timestampLastUpdate = System.currentTimeMillis();

        this.amountData = value;
        return this;
    }

    public ProgressRecord add(double percentToAdd) {
        this.timestampLastUpdate = System.currentTimeMillis();

        percent += percentToAdd;
        return this;
    }

    public ProgressRecord add(double percentToAdd, long amountDataToAdd) {
        this.timestampLastUpdate = System.currentTimeMillis();

        percent += percentToAdd;
        amountData += amountDataToAdd;
        return this;
    }

    public boolean isCompleted() {
        return percent >= 1.0d;
    }

    /*----------------------------------------------------------------------*/
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        ToStringBuilder tsb = new ToStringBuilder(this);
        tsb.append("id", this.id).append("Start", this.timestampStart)
                .append("Last Update", this.timestampLastUpdate).append("Percent", this.percent)
                .append("Amount", this.amountData);
        return tsb.build();
    }
}
