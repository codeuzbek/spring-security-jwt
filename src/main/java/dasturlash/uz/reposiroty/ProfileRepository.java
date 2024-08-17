package dasturlash.uz.reposiroty;

import dasturlash.uz.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, String> {
    Optional<ProfileEntity> findByPhoneAndVisibleTrue(String phone);
}
