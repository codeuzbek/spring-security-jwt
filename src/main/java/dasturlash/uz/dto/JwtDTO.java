package dasturlash.uz.dto;

public class JwtDTO {
    private String phone;
    private String role;

    public JwtDTO(String phone, String role) {
        this.phone = phone;
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }
}
