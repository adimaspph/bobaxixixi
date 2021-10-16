package apap.tugas.bobaxixixi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.util.buf.Ascii;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
@Entity
@Table(name = "store")
public class StoreModel implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStore;

    @NotNull
    @Size(max=255)
    @Column(nullable = false)
    private String name;

    @NotNull
    @Size(max=255)
    @Column(nullable = false)
    private String address;

    @NotNull
    @Size(max=10)
    @Column(nullable = false, unique = true)
    private String storeCode;

    @NotNull
    @Column(nullable = false)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime openHour;

    @NotNull
    @Column(nullable = false)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime closeHour;

    //Relasi dengan ManagerModel
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_manager", referencedColumnName = "idManager", nullable = false)
    private ManagerModel manager;

    //Relasi dengan store_boba model
    @OneToMany(mappedBy = "store")
    List<StoreBobaModel> listBoba;
}
