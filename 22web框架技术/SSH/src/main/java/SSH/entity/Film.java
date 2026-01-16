package SSH.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    @Id
    Long id;
    String name;
    String director;
    String type;
    String language;
    Double score;
    String url;
    String description;
    String video;
}
