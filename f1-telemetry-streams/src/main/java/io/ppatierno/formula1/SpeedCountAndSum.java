/*
 * Copyright Paolo Patierno.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.ppatierno.formula1;

public class SpeedCountAndSum {
    private Integer count;
    private Integer sum;

    public SpeedCountAndSum(Integer count, Integer sum) {
        this.count = count;
        this.sum = sum;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "SpeedCountAndSum[" +
                "count=" + count +
                ", sum=" + sum +
                ']';
    }
}
