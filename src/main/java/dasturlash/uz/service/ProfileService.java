package dasturlash.uz.service;

import dasturlash.uz.config.CustomUserDetails;
import dasturlash.uz.dto.AuthRequestDTO;
import dasturlash.uz.dto.AuthResponseDTO;
import dasturlash.uz.dto.ProfileDTO;
import dasturlash.uz.entity.ProfileEntity;
import dasturlash.uz.enums.GeneralStatus;
import dasturlash.uz.exceptions.ProfileNotFoundException;
import dasturlash.uz.reposiroty.ProfileRepository;
import dasturlash.uz.util.JwtUtil;
import dasturlash.uz.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public ProfileDTO registration(ProfileDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByPhoneAndVisibleTrue(dto.getPhone());
        if (optional.isPresent()) {
            return null;
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPhone(dto.getPhone());
        entity.setPassword(MD5Util.getMd5(dto.getPassword()));
        entity.setRole(dto.getRole());

        profileRepository.save(entity);

        dto.setId(entity.getId());
        return dto;
    }

    public AuthResponseDTO authorization(AuthRequestDTO auth) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(auth.getPhone(), auth.getPassword()));

            if (authentication.isAuthenticated()) {
                CustomUserDetails profile = (CustomUserDetails) authentication.getPrincipal();
                AuthResponseDTO response = new AuthResponseDTO();
                response.setName(profile.getName());
                response.setSurname(profile.getSurname());
                response.setPhone(profile.getPhone());
                response.setRole(profile.getRole());
                response.setJwtToken(JwtUtil.encode(profile.getPhone(), profile.getRole().name()));
                return response;
            }
        } catch (BadCredentialsException e) {
            throw new UsernameNotFoundException("Phone or password wrong");
        }
        throw new UsernameNotFoundException("Phone or password wrong");
    }

    // autoriztion qilishning boshqacha usuli.
    public AuthResponseDTO authorizationNotherWay(AuthRequestDTO auth) {
        Optional<ProfileEntity> optional = profileRepository.findByPhoneAndVisibleTrue(auth.getPhone());
        if (optional.isEmpty()) {
            throw new ProfileNotFoundException("Phone or password wrong");
        }
        ProfileEntity profile = optional.get();
        if (!profile.getStatus().equals(GeneralStatus.ACTIVE)) {
            throw new UsernameNotFoundException("Phone or password wrong");
        }
        if (!passwordEncoder.matches(auth.getPassword(), profile.getPassword())) {
            throw new UsernameNotFoundException("Phone or password wrong");
        }
        AuthResponseDTO response = new AuthResponseDTO();
        response.setName(profile.getName());
        response.setSurname(profile.getSurname());
        response.setPhone(profile.getPhone());
        response.setRole(profile.getRole());
        response.setJwtToken(JwtUtil.encode(profile.getPhone(), profile.getRole().name()));
        return response;
    }

}
