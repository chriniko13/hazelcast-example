package hazelcast.example.chapter2.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode

public class Student implements Serializable {

    private String firstname;
    private String initials;
    private String surname;

}
