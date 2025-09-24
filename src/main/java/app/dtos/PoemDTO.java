package app.dtos;

import app.entities.Poem;
import app.enums.PoemType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class PoemDTO {
    private int id;
    private String title;
    private String text;
    private String author;
    private PoemType type;

    public PoemDTO(Poem poem){
        this.id = poem.getId();
        this.title = poem.getTitle();
        this.text = poem.getText();
        this.author = poem.getAuthor();
        this.type = poem.getType();
    }
}
