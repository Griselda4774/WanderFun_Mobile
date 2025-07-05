package com.example.wanderfunmobile.core.util;

import com.example.wanderfunmobile.domain.model.addresses.Address;

public class StringUtil {
    public static String formatAddressToString(Address address) {
        StringBuilder addressBuilder = new StringBuilder();

        if (address.getStreet() != null && !address.getStreet().isEmpty()) {
            addressBuilder.append(address.getStreet());
        }

        if (address.getWard() != null && address.getWard().getFullName() != null && !address.getWard().getFullName().isEmpty()) {
            if (addressBuilder.length() > 0) addressBuilder.append(", ");
            addressBuilder.append(address.getWard().getFullName());
        }

        if (address.getDistrict() != null && address.getDistrict().getFullName() != null && !address.getDistrict().getFullName().isEmpty()) {
            if (addressBuilder.length() > 0) addressBuilder.append(", ");
            addressBuilder.append(address.getDistrict().getFullName());
        }

        if (address.getProvince() != null && address.getProvince().getFullName() != null && !address.getProvince().getFullName().isEmpty()) {
            if (addressBuilder.length() > 0) addressBuilder.append(", ");
            addressBuilder.append(address.getProvince().getFullName());
        }

        return addressBuilder.toString();
    }

    public static String formatAddressToStringNoStreet(Address address) {
        StringBuilder addressBuilder = new StringBuilder();

        if (address.getWard() != null && address.getWard().getFullName() != null && !address.getWard().getFullName().isEmpty()) {
            addressBuilder.append(address.getWard().getFullName());
        }

        if (address.getDistrict() != null && address.getDistrict().getFullName() != null && !address.getDistrict().getFullName().isEmpty()) {
            if (addressBuilder.length() > 0) addressBuilder.append(", ");
            addressBuilder.append(address.getDistrict().getFullName());
        }

        if (address.getProvince() != null && address.getProvince().getFullName() != null && !address.getProvince().getFullName().isEmpty()) {
            if (addressBuilder.length() > 0) addressBuilder.append(", ");
            addressBuilder.append(address.getProvince().getFullName());
        }

        return addressBuilder.toString();
    }
}
