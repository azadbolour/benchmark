/*
 * Copyright 2017 Azad Bolour
 * Licensed under MIT (https://github.com/azadbolour/benchmark/blob/master/LICENSE)
 */

package com.bolour.benchmark;

import static java.lang.String.format;

/**
 * Type of interaction to simulate.
 */
public class BenchmarkInteractionType {

    public static final String NO_SERVICE = "none";

    public final String service;
    public final String method;
    public final String fullName;

    public BenchmarkInteractionType(String service, String method) {
        super();
        this.service = service;
        this.method = method;
        this.fullName = format("$s.%s", this.service, this.method);
    }

    public static final BenchmarkInteractionType NO_INTERACTION_TYPE =
      new BenchmarkInteractionType(NO_SERVICE, "");

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((method == null) ? 0 : method.hashCode());
        result = prime * result + ((service == null) ? 0 : service.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BenchmarkInteractionType other = (BenchmarkInteractionType) obj;
        if (method == null) {
            if (other.method != null)
                return false;
        } else if (!method.equals(other.method))
            return false;
        if (service == null) {
            if (other.service != null)
                return false;
        } else if (!service.equals(other.service))
            return false;
        return true;
    }

}
