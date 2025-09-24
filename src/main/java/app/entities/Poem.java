package app.entities;

import app.dtos.PoemDTO;
import app.enums.PoemType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Poem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "poem_title")
    private String title;

    @Column(name = "poem_text")
    private String text;
    private String Author;

    @Enumerated(EnumType.STRING)
    @Column(name = "poem_type")
    private PoemType type;

    /*
    public Poem(PoemDTO poemDTO){
        this.id = poemDTO.getId();
    }

     */
}
