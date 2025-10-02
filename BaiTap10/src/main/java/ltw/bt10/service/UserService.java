package ltw.bt10.service;

import ltw.bt10.model.User;
import jakarta.validation.Valid;
import java.util.*;

public interface UserService {
    User create(@Valid User u);
    Optional<User> findByEmail(String email);
}