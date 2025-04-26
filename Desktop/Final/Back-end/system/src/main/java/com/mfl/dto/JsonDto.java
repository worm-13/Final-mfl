package com.mfl.dto;

import org.json.JSONObject;

public class JsonDto {
    public static String responseLoginJson(UserDto dto, String mesage, int code) {
            JSONObject obj = new JSONObject();
            obj.put("mesage", mesage);
            obj.put("code",code );
            obj.put("data",new JSONObject()
                    .put("phoneNumber", dto.phoneNumber)
                    .put("nickname", dto.nickname)
                    .put("email", dto.email)
                    .put("session", dto.session)
                    .put("wins",dto.wins)
                    .put("score",dto.score)
            );
            return obj.toString();
    }
}
