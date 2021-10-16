package apap.tugas.bobaxixixi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
@Entity
@Table(name = "bobaTea")
public class BobaTeaModel implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBoba;

    @NotNull
    @Size(max=255)
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private Integer price;

    @NotNull
    @Column(nullable = false)
    private Integer size;

    @NotNull
    @Column(nullable = false)
    private Integer iceLevel;

    @NotNull
    @Column(nullable = false)
    private Integer sugarLevel;

    //Relasi dengan Store_boba model
    @OneToMany(mappedBy = "boba")
    List<StoreBobaModel> listStore;

    //Relasi dengan topping
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_topping", referencedColumnName = "idTopping", nullable = true)
    private ToppingModel topping;

}
