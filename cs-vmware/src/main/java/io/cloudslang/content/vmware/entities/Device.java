/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.entities;

import static java.lang.String.format;

/**
 * Created by Mihai Tusa.
 * 1/22/2016.
 */
public enum Device {
    CPU,
    MEMORY,
    DISK,
    CD,
    NIC;

    public static String getValue(String input) throws Exception {
        for (Device device : Device.values()) {
            if (device.name().equalsIgnoreCase(input)) {
                return device.name().toLowerCase();
            }
        }

        throw new IllegalArgumentException(format("Unrecognized device: [%s]. Valid values are: cpu, memory, disk, cd, nic.", input));
    }
}
