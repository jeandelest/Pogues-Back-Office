package fr.insee.pogues.configuration.auth.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class User {

    @Getter
    private final String stamp;
    @Getter
    private final String name;

    public User(){
        this("default", "Guest");
    }
}
