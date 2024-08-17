package dasturlash.uz.dto;

import dasturlash.uz.enums.ProfileRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDTO {
    private String id;
    private String name;
    private String surname;
    private String phone;
    private String password;
    private ProfileRole role;
}
