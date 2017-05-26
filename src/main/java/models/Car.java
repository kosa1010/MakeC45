package models;

import javax.persistence.*;
import java.util.List;

/**
 * Created by kosa1010 on 13.10.16.
 */
@Entity(name = "Car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_car;
    private String category;
    private String make;
    private String model;
    private String version;
    private double price_raw;
    private int year;
    private int millage;
    private int engine_capacity;
    private String fuel_type;
    private int engine_power;
    private String gearbox;
    private String body_type;
    private String transmission;
    @ManyToMany
    @JoinTable(name = "Car_Feature")
    private List<Feature> features;
    @OneToMany
    @JoinTable(name = "Car_Image")
    private List<Image> gallery;
    private int nr_seats;
    private int door_count;
    private String color;
    private boolean metalic;
    private String country_of_origin;
    private boolean register_in_polish;
    private boolean first_owner;
    //    @Column(name = "noaccidents") //do bazy na postgresie
    private boolean noAccidents;
    private boolean services_in_ASO;
    private boolean used;
    private String vin;

    public Car() {
    }

    public Car(long id_car, String body_type, int door_count, String fuel_type, String make, int year, double price_raw, int engine_capacity) {
        this.id_car = id_car;
        this.make = make;
        this.price_raw = price_raw;
        this.year = year;
        this.engine_capacity = engine_capacity;
        this.fuel_type = fuel_type;
        this.body_type = body_type;
        this.door_count = door_count;
    }

    public Car(String category, String make, String model, String version, double price_raw, int year, int millage, int engine_capacity, String fuel_type, int engine_power, String gearbox, String body_type, String transmission, List<Feature> features, List<Image> gallery, int nr_seats, int door_count, String color, boolean metalic, String country_of_origin, boolean register_in_polish, boolean first_owner, boolean noAccidents, boolean services_in_ASO, boolean used, String vin) {
        this.category = category;
        this.make = make;
        this.model = model;
        this.version = version;
        this.price_raw = price_raw;
        this.year = year;
        this.millage = millage;
        this.engine_capacity = engine_capacity;
        this.fuel_type = fuel_type;
        this.engine_power = engine_power;
        this.gearbox = gearbox;
        this.body_type = body_type;
        this.transmission = transmission;
        this.features = features;
        this.gallery = gallery;
        this.nr_seats = nr_seats;
        this.door_count = door_count;
        this.color = color;
        this.metalic = metalic;
        this.country_of_origin = country_of_origin;
        this.register_in_polish = register_in_polish;
        this.first_owner = first_owner;
        this.noAccidents = noAccidents;
        this.services_in_ASO = services_in_ASO;
        this.used = used;
        this.vin = vin;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public long getId_car() {
        return id_car;
    }

    public void setId_car(long id_car) {
        this.id_car = id_car;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String car_type) {
        this.category = car_type;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String mark) {
        this.make = mark;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public double getPrice_raw() {
        return price_raw;
    }

    public void setPrice_raw(double price) {
        this.price_raw = price;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year_production) {
        this.year = year_production;
    }

    public int getMillage() {
        return millage;
    }

    public void setMillage(int distance) {
        this.millage = distance;
    }

    public int getEngine_capacity() {
        return engine_capacity;
    }

    public void setEngine_capacity(int copacity) {
        this.engine_capacity = copacity;
    }

    public String getFuel_type() {
        return fuel_type;
    }

    public void setFuel_type(String fuel_type) {
        this.fuel_type = fuel_type;
    }

    public int getEngine_power() {
        return engine_power;
    }

    public void setEngine_power(int power) {
        this.engine_power = power;
    }

    public String getGearbox() {
        return gearbox;
    }

    public void setGearbox(String gearbox) {
        this.gearbox = gearbox;
    }

    public String getBody_type() {
        return body_type;
    }

    public void setBody_type(String body_type) {
        this.body_type = body_type;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String driving_gear) {
        this.transmission = driving_gear;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public int getNr_seats() {
        return nr_seats;
    }

    public void setNr_seats(int num_seets) {
        this.nr_seats = num_seets;
    }

    public int getDoor_count() {
        return door_count;
    }

    public void setDoor_count(int num_doors) {
        this.door_count = num_doors;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isMetalic() {
        return metalic;
    }

    public void setMetalic(boolean metalic) {
        this.metalic = metalic;
    }

    public String getCountry_of_origin() {
        return country_of_origin;
    }

    public void setCountry_of_origin(String contry_of_origin) {
        this.country_of_origin = contry_of_origin;
    }

    public boolean isRegister_in_polish() {
        return register_in_polish;
    }

    public void setRegister_in_polish(boolean register_in_polish) {
        this.register_in_polish = register_in_polish;
    }

    public boolean isFirst_owner() {
        return first_owner;
    }

    public void setFirst_owner(boolean first_ovner) {
        this.first_owner = first_ovner;
    }

    public boolean isNoAccidents() {
        return noAccidents;
    }

    public void setNoAccidents(boolean accidents) {
        this.noAccidents = accidents;
    }

    public boolean isServices_in_ASO() {
        return services_in_ASO;
    }

    public void setServices_in_ASO(boolean services_in_ASO) {
        this.services_in_ASO = services_in_ASO;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public List<Image> getGallery() {
        return gallery;
    }

    public void setGallery(List<Image> gallery) {
        this.gallery = gallery;
    }
}
