package apap.tugas.bobaxixixi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
@Entity
@Table(name = "storeBoba")
public class StoreBobaModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStoreBoba;

    @NotNull
    @Size(max=12)
    @Column(nullable = false, unique = true)
    private String productionCode;

    //Relasi dengan Store
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_store", referencedColumnName = "idStore", nullable = false)
    private StoreModel store;

    //Relasi dengan Boba
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_boba", referencedColumnName = "idBoba", nullable = false)
    private BobaTeaModel boba;
}
