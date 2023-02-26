package com.bakhromjonov.reservation.utils;

import lombok.Data;

public class FormClass {

    @Data
    public static class UserBookingForm {
        private String dateStart;
        private String dateEnd;
        private String description;
        private String roomName;
    }

    @Data
    public static class BookingUpdate {
        private String dateStart;
        private String dateEnd;
        private String description;
    }

    @Data
    public static class DateForm {
        String start;
        String end;
    }

    @Data
    public static class RoomForm {
        Long id;
        String name;
    }

    @Data
    public static class RoleToUserForm {
        private String username;
        private String roleName;
    }
}
