    package com.example.demo.Auth;

    import com.example.demo.Exception.DefaultException;
    import org.springframework.http.HttpStatus;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.AuthenticationException;
    import org.springframework.security.core.context.SecurityContextHolder;
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
                throw new DefaultException(HttpStatus.CONFLICT, "Username already exists");
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
            try {
                Authentication latestResult = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(), request.getPassword()
                        )
                );

                Authentication previousResult = SecurityContextHolder.getContext().getAuthentication();

                if (previousResult != null && previousResult.isAuthenticated()) {
                    latestResult = latestResult.toBuilder()
                            .authorities((a) -> a.addAll(previousResult.getAuthorities()))
                            .build();
                }

                System.out.println(latestResult);

            }catch (AuthenticationException e) {
                throw new DefaultException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
            }



            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new DefaultException(HttpStatus.UNAUTHORIZED, "User not found"));

            return new AuthResponse(user.getUsername(), user.getRole());
        }
    }
