    package com.example.demo.Auth;

    import com.example.demo.Exception.DefaultException;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

    @Service
    public class AuthService {
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final AuthenticationManager authenticationManager;

        public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
            this.authenticationManager = authenticationManager;
        }

        public AuthResponse register(AuthRequest request) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new DefaultException("Username already exists");
            }

            User user = new User(
                    request.getUsername(),
                    passwordEncoder.encode(request.getPassword()),
                    "ROLE_USER"
            );

            userRepository.save(user);

            return new AuthResponse(user.getUsername(), user.getRole());
        }

        public AuthResponse login(AuthRequest request) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                    )
            );

            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new DefaultException("User not found"));

            return new AuthResponse(user.getUsername(), user.getRole());
        }
    }
