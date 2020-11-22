package pl.lukabrasi.transportapp.auth.forms;

import lombok.Data;

@Data
public class RegisterForm {
    private String userName;
    private String password;
    private String email;
}
