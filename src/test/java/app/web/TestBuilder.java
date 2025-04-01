package app.web;

import app.deck.model.Deck;
import app.user.model.RoleEnum;
import app.user.model.User;

import lombok.experimental.UtilityClass;


import java.util.UUID;

@UtilityClass
public class TestBuilder {

    public static User aRandomUser() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .username("User")
                .password("123123")
                .email("moko123@gmail.com")
                .firstName("Moko")
                .lastName("User")
                .role(RoleEnum.USER)
                .isActive(true)
                .deck(new Deck())
                .build();


        return user;
    }
}