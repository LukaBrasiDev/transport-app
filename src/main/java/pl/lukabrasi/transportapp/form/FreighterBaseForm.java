package pl.lukabrasi.transportapp.form;

import lombok.Data;
import pl.lukabrasi.transportapp.model.User;

@Data
public class FreighterBaseForm {

    private String name;
    private String address;
    private String person;
    private String telephone;
    private String email;
    private User user;
    private String info;
}
