package com.first.bulletinboard;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class TestInfoFixture {
    public static class TestInfo {
        private int postId;
        private int userId;
        private int commentId;
        private int likeId;
        private int alarmId;
        private String userName;
        private String password;
        private String title;
        private String body;

        /*
        public static TestInfo get() {
        }
        public String getUserName() {
            User
        }

        public String getTitle() {
        }

        public Object getBody() {
        }*/
    }

}
