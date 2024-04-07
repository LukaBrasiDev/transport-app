package pl.lukabrasi.transportapp.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lukabrasi.transportapp.auth.forms.LoginForm;
import pl.lukabrasi.transportapp.auth.forms.RegisterForm;
import pl.lukabrasi.transportapp.auth.repositories.UserRepository;
import pl.lukabrasi.transportapp.form.UserForm;
import pl.lukabrasi.transportapp.model.User;
import pl.lukabrasi.transportapp.service.OrderService;

import java.util.Optional;

@Service
public class UserService {

    public LoginResponse registerUser(RegisterForm registerForm) {

        if (!isLoginFree(registerForm.getUserName())) {
            return LoginResponse.BAD_CREDENTIALS;

        }

        User userEntity = new User();
        userEntity.setUserName(registerForm.getUserName());
        userEntity.setPassword(getBCrypt().encode(registerForm.getPassword()));
        userEntity.setEmail(registerForm.getEmail());
        userEntity.setActive(true);
        userEntity.setRole("0");

        userRepository.save(userEntity);
        return LoginResponse.SUCCESS;

    }

    public enum LoginResponse {
        SUCCESS,
        BAD_CREDENTIALS,
        BANNED
    }

    final UserRepository userRepository;
    final UserSession userSession;

    @Autowired
    public UserService(UserRepository userRepository, UserSession userSession) {
        this.userRepository = userRepository;
        this.userSession = userSession;
    }


    private boolean isLoginFree(String login) {
        return !userRepository.existsByUserName(login);
    }

    public LoginResponse login(LoginForm loginForm) {
        Optional<User> userOptional = userRepository.findByUserName(loginForm.getUserName());
        if (!userOptional.isPresent()) {
            return LoginResponse.BAD_CREDENTIALS;
        }

        if (!getBCrypt().matches(loginForm.getPassword(), userOptional.get().getPassword())) {
            return LoginResponse.BAD_CREDENTIALS;
        }

        userSession.setLogin(true);
        userSession.setUserEntity(userOptional.get());
        return LoginResponse.SUCCESS;
    }

    public void logout() {
        userSession.setLogin(false);
        userSession.setUserEntity(null);
    }

    public OrderService.ActionResponse updateUser(Long id, UserForm userForm) {
        Optional<User> optionalUser = userRepository.findById(id);

        optionalUser.get().setEmail(userForm.getEmail());
        optionalUser.get().setTelephone(userForm.getTelephone());
       /* if(userForm.getActive() != null){*/
        optionalUser.get().setActive(userForm.getActive());/*}*/
        if(userForm.getRole() != null) {
            optionalUser.get().setRole(userForm.getRole());
        }
        //optionalUser.get().setPassword(userForm.getPassword());
        if(!userForm.getPassword().trim().isEmpty()){
            optionalUser.get().setPassword(getBCrypt().encode(userForm.getPassword()));
        }
        userRepository.save(optionalUser.get());
        return OrderService.ActionResponse.SUCCESS;
    }

    @Bean
    public BCryptPasswordEncoder getBCrypt() {
        return new BCryptPasswordEncoder();
    }


}
