/*
 * (c) Copyright 2016 Brite:Bill Ltd.
 *
 * 7 Grand Canal Street Lower, Dublin 2, Ireland
 * info@britebill.com
 * +353 1 661 9426
 */
package advent2020.day7;

import lombok.Data;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */

class InnerBagRule {
    int count;
    String bag;

    public InnerBagRule(int count, String bag) {
        this.count = count;
        this.bag = bag;
    }

    public int getCount() {
        return count;
    }

    public String getBag() {
        return bag;
    }
}
