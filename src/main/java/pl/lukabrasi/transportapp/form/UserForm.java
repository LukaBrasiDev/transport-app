package pl.lukabrasi.transportapp.form;

import lombok.Data;

@Data
public class UserForm {
    private String userName;
    private String email;
    private String password;
    private String telephone;
    private Boolean active;
}
