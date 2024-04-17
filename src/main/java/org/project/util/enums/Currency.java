package org.project.util.enums;

import lombok.Getter;

@Getter
public enum Currency {
    UA(980),
    EUR(978);

    private final long ccy;

    Currency(long ccy) {
        this.ccy = ccy;
    }
}
