    package com.example.demo.Auth;

    import com.example.demo.Exception.DefaultException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.http.HttpStatus;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.AuthenticationException;
    import org.springframework.security.core.context.SecurityContext;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.context.SecurityContextRepository;
    import org.springframework.stereotype.Service;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

    @Service
    public class AuthService {
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final AuthenticationManager authenticationManager;
        private final SecurityContextRepository securityContextRepository;

        public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository){
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
            this.authenticationManager = authenticationManager;
            this.securityContextRepository = securityContextRepository;
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

        public AuthResponse login(AuthRequest request, HttpServletRequest ServletRequest, HttpServletResponse ServletResponse) {
            try {
                Authentication latestResult = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(), request.getPassword()
                        )
                );

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(latestResult);
                SecurityContextHolder.setContext(context);

                securityContextRepository.saveContext(context, ServletRequest, ServletResponse);

            }catch (AuthenticationException e) {
                throw new DefaultException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
            }

            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new DefaultException(HttpStatus.UNAUTHORIZED, "User not found"));

            return new AuthResponse(user.getUsername(), user.getRole());
        }
    }
