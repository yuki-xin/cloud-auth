package xin.yuki.auth.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import xin.yuki.auth.entity.UserEntity;
import xin.yuki.auth.repository.UserRepository;

@Slf4j
public class UserDetailsServiceImpl implements UserDetailsManager {


    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceImpl(final UserRepository userRepository, final AuthenticationManager authenticationManager, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public void createUser(final UserDetails userDetails) {
        final UserEntity entity = ((UserEntity) userDetails);
        entity.setPassword(this.passwordEncoder.encode(userDetails.getPassword()));
        this.userRepository.save(entity);
    }

    @Override
    public void updateUser(final UserDetails userDetails) {
        this.userRepository.save(((UserEntity) userDetails));
    }

    @Override
    public void deleteUser(final String username) {
        this.userRepository.deleteByUsername(username);
    }

    @Override
    public void changePassword(final String oldPassword, final String newPassword) {
        final Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            throw new AccessDeniedException("Can't change password as no Authentication object found in context for current user.");
        } else {
            final String username = currentUser.getName();
            if (this.authenticationManager != null) {
                log.debug("Reauthenticating user '" + username + "' for password change request.");
                this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
            } else {
                log.debug("No authentication manager set. Password won't be re-checked.");
            }

            log.debug("Changing password for user '" + username + "'");
            final UserEntity user = this.userRepository.findByUsername(username);
            user.setPassword(this.passwordEncoder.encode(newPassword));
            this.userRepository.save(user);

            SecurityContextHolder.getContext().setAuthentication(this.createNewAuthentication(currentUser, this.passwordEncoder.encode(newPassword)));
        }
    }

    @Override
    public boolean userExists(final String username) {
        return this.userRepository.existsByUsername(username);
    }

    private Authentication createNewAuthentication(final Authentication currentAuth, final String newPassword) {
        final UserDetails user = this.loadUserByUsername(currentAuth.getName());
        final UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(user, newPassword, user.getAuthorities());
        newAuthentication.setDetails(currentAuth.getDetails());
        return newAuthentication;
    }

}
