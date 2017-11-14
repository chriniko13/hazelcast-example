package hazelcast.example.chapter3.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@ToString
@EqualsAndHashCode
public class Holder implements Serializable {

    private String title;
    private String description;
}
