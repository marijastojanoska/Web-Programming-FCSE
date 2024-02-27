package mk.ukim.finki.wp.eshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFullName implements Serializable {
    private String name;
    private String surname;
}
