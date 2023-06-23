package org.example.models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class User {
    private Integer id;
    private String username;
    private String password;
    private Role role;
}
