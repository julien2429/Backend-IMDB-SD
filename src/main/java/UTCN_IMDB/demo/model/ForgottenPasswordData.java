package UTCN_IMDB.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "forgottenPasswordData")
public class ForgottenPasswordData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID forgottenPasswordDataId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "forgotten-passwords")
    private User user;

    @Column(name="forgottenPasswordToken", nullable = false)
    private String forgottenPasswordToken;

    @Column(name="forgottenPasswordTokenExpirationDate", nullable = false)
    private Date forgottenPasswordTokenExpirationDate;

}
