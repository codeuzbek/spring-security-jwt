package dasturlash.uz.dto;

import dasturlash.uz.enums.ProfileRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDTO {
    private String name;
    private String surname;
    private String phone;
    private ProfileRole role;
    private String jwtToken;
}
