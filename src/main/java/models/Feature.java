package models;


import javax.persistence.*;

/**
 * Created by kosa1010 on 26.11.16.
 */
@Entity(name = "Feature")
public class Feature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_feature;
    @Column(unique = true)
    private String name_feature;

    public Feature() {
    }

    public Feature(String name_feature) {
        this.name_feature = name_feature;
    }

    public long getId_feature() {
        return id_feature;
    }

    public void setId_feature(long id_feature) {
        this.id_feature = id_feature;
    }

    public String getName_feature() {
        return name_feature;
    }

    public void setName_feature(String name_Feature) {
        this.name_feature = name_Feature;
    }
}
