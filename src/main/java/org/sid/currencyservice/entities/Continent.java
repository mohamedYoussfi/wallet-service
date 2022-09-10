package org.sid.currencyservice.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Continent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String continentName;
    @OneToMany(mappedBy = "continent")
    private List<Country> countries;

}
