package ltw.bt10.service.impl;

import ltw.bt10.model.Role;
import ltw.bt10.model.User;
import ltw.bt10.repository.RoleRepository;
import ltw.bt10.repository.UserRepository;
import ltw.bt10.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo; this.roleRepo = roleRepo; this.encoder = encoder;
    }

    @Override @Transactional
    public User create(User u) {
        u.setPassword(encoder.encode(u.getPassword()));
        if (u.getRoles()==null || u.getRoles().isEmpty()) {
            var role = roleRepo.findByName("ROLE_USER").orElseGet(() -> roleRepo.save(Role.builder().name("ROLE_USER").build()));
            u.getRoles().add(role);
        }
        return userRepo.save(u);
    }

    @Override
    public java.util.Optional<User> findByEmail(String email) { return userRepo.findByEmail(email); }
}