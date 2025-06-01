package com.example.wanderfunmobile.core.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtil {
    public static String localTimeToString(LocalTime localTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return localTime.format(formatter);
    }

    public static LocalTime stringToLocalTime(String timeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(timeString, formatter);
    }

    public static String dateToString(Date date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    public static Date stringToDate(String dateString) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.parse(dateString);
    }

    public static String localDateToString(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return localDate.format(formatter);
    }

    public static LocalDate stringToLocalDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateString, formatter);
    }

    public static String localDateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm, dd/MM/yyyy");
        return localDateTime.format(formatter);
    }

    public static LocalDateTime stringToLocalDateTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm, dd/MM/yyyy");
        return LocalDateTime.parse(dateString, formatter);
    }

    public static LocalDate dateToLocalDate(Date date) {
        return date.toInstant()
                .atZone(java.time.ZoneId.of("Asia/Ho_Chi_Minh"))
                .toLocalDate();
    }

    public static Date localDateToDate(LocalDate localDate) {
        return java.util.Date.from(
                localDate.atStartOfDay()
                        .atZone(java.time.ZoneId.of("Asia/Ho_Chi_Minh"))
                        .toInstant()
        );
    }

    public static String timeAgoLocalDateTime(LocalDateTime pastTime) {
        Duration duration = Duration.between(pastTime, LocalDateTime.now());

        long seconds = duration.getSeconds();
        if (seconds < 30) {
            return "Vừa xong";
        }
        else if (seconds < 60) {
            return seconds + " giây trước";
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            return minutes + " phút trước";
        } else if (seconds < 86400) {
            long hours = seconds / 3600;
            return hours + " giờ trước";
        } else if (seconds < 2592000) {
            long days = seconds / 86400;
            return days + " ngày trước";
        } else if (seconds < 31104000) {
            long months = seconds / 2592000;
            return months + " tháng trước";
        } else {
            long years = seconds / 31104000;
            return years + " năm trước";
        }
    }
}
