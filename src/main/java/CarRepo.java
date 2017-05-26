import models.Car;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.*;
import weka.core.converters.ArffLoader;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kosa1010 on 23.05.17.
 */
public class CarRepo {

    EntityManager entityManager;

    public List<Car> getCars() {
        String query = "SELECT NEW Car(c.id_car, c.body_type, c.door_count, " +
                "c.fuel_type, c.make, c.year, c.price_raw, c.engine_capacity) FROM Car c";
        TypedQuery<Car> typedQuery = entityManager.createQuery(query, Car.class);
        List<Car> resultList = typedQuery.getResultList();
        if (resultList.isEmpty())
            return null;
        return resultList;
    }

    public List<Car> findAll() {
        EntityManagerFactory factory = Persistence
                .createEntityManagerFactory("my_app");
        entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        List listPersons = entityManager.createQuery(
                "SELECT c FROM Car c").getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();
//        factory.close(); - See more at: http://www.codemiles.com/jpa/get-all-objects-for-an-entity-t6273.html#sthash.5qFCWXMb.dpuf
////        Query query = em.createQuery("SELECT e FROM Professor e");
////        return (Collection<Professor>) query.getResultList();
//        TypedQuery<Car> typedQuery = entityManager.createQuery("SELECT c FROM Car c", Car.class);
//        List<Car> resultList = typedQuery.getResultList();
//        if (resultList.isEmpty())
//            return null;
//        return resultList;
        return listPersons;
    }

    public String[] getCarSuggestions(String[][] customerPref) {
        List<Car> allCars = getCars();
        String[][] obj = makeObjects(customerPref, allCars);
        try {
            Instances train = loadData("/home/kosa1010/Pulpit/Amgr/klasyBardziejRowno1.arff");
            train.setClass(train.attribute(train.numAttributes() - 1));

//            String[] options = Utils.splitOptions("-U -M 10");
            String[] options = Utils.splitOptions("-C 0.51 -M 1");

            J48 tree = new J48();
            tree.setOptions(options); //Ustawienie opcji
            tree.buildClassifier(train);  // Tworzenie klasyfikatora (drzewa)
            System.out.println("PropositionController.getCarSuggestions");
            Instances instances = arrayToInstances(obj);

            J48 c34;
//            InputStream input = getClass().getResourceAsStream("/home/kosa1010/IdeaProjects/smartmoto2/src/main/resources/static/classifiers/c45.model");
//            Classifier cls = (Classifier) SerializationHelper.read(input);
            Classifier cls = (Classifier) SerializationHelper.read("/home/kosa1010/IdeaProjects/smartmoto2/src/main/resources/static/classifiers/old2.model");
            c34 = (J48) cls;
            Instances dataAfterClassifier = classifyObjects(instances, c34);

            String[] carsClass = new String[dataAfterClassifier.numInstances()];
            for (int i = 0; i < dataAfterClassifier.numInstances(); i++) {
                carsClass[i] = dataAfterClassifier.instance(i).toString(dataAfterClassifier.numAttributes() - 1);
            }
            return carsClass;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Klasyfikacja obiektów
     *
     * @param unclassifiedData
     * @param tree
     * @throws Exception
     */
    public Instances classifyObjects(Instances unclassifiedData, J48 tree) throws Exception {
        unclassifiedData.setClassIndex(unclassifiedData.numAttributes() - 1);
        int index = 0;
        for (int i = 0; i < unclassifiedData.numInstances(); i++) {
            double decision;
            try {
                decision = tree.classifyInstance(unclassifiedData.instance(i));
                System.out.println(unclassifiedData.instance(54));
            } catch (Exception e) {
                index++;
                decision = 0.0;
            }
            //Ustawienie wartosci decyzji w obiekcie
            unclassifiedData.instance(i).setClassValue(decision);
            System.out.println("\t\t" + (i + 1) + " DEC=" + unclassifiedData.instance(i)
                    .toString(unclassifiedData.numAttributes() - 1));
        }
        System.out.println(unclassifiedData.instance(0));
        System.out.println("liczba wykrzaczeń " + index);
        return unclassifiedData;
    }

    /**
     * Tworzy tablicę z preferencjami uzytkownika i zestawem samochodów dostępnym w systemie
     *
     * @param customerPref (tablica preferencji użytkownika)
     * @param allCars      (tablica z informacjami na temat aut)
     * @return
     */
    private String[][] makeObjects(String[][] customerPref, List<Car> allCars) {
        String[][] objects = new String[allCars.size() * customerPref.length][customerPref.length + 7];
        System.out.println("wyniary: " + objects.length + "" + objects[0].length);
        for (int i = 0; i < objects.length; i++) {
            for (int j = 0; j <= customerPref[0].length; j++) {
                if (j < customerPref[0].length) {
                    objects[i][j] = customerPref[i][j];
                } else {
                    objects[i][j] = allCars.get(i).getBody_type();
                    objects[i][j + 1] = String.valueOf(allCars.get(i).getDoor_count());
                    objects[i][j + 2] = allCars.get(i).getFuel_type();
                    objects[i][j + 3] = allCars.get(i).getMake();
                    objects[i][j + 4] = String.valueOf(allCars.get(i).getYear());
                    objects[i][j + 5] = String.valueOf(allCars.get(i).getPrice_raw());
                    objects[i][j + 6] = String.valueOf(allCars.get(i).getEngine_capacity());
                }
            }
        }
        return objects;
    }

    /**
     * Zamiana tablicy stringów na obiekt będący systemem informacyjnym
     *
     * @param data (tablica z preferencjami użytkownika)
     */
    public Instances arrayToInstances(String[][] data) {
        System.out.println("PropositionController.arrayToInstances");
        Instances dataInstances = makeInstances();
//        for (String s : data[0]) {
//            System.out.print(s + ", ");
//        }
        System.out.println();
        DenseInstance denseInstance = new DenseInstance(dataInstances.numAttributes());
        for (int i = 0; i < data.length; i++) {
            dataInstances.add(denseInstance);
        }

        for (int i = 0; i < dataInstances.numInstances(); i++) {
            Instance instance = dataInstances.instance(i); //Pobranie obiektu o podanym numerze
            for (int k = 0; k < dataInstances.numAttributes() - 1; k++) {
                if (dataInstances.attribute(k).isNumeric()) {
                    instance.setValue(k, data[i][k].contains(".") ? Double.valueOf(data[i][k]) : Integer.valueOf(data[i][k]));
                } else {
                    instance.setValue(k, data[i][k] == "" ? "0" : data[i][k]);
                }
            }
        }
        System.out.println(dataInstances.toString());
        return dataInstances;
    }

    /**
     * Utworzenie tablicy instancji wypełnionej pustymi obiektami o odpowiedniej strukturze atrybutów
     * i ich typów z wykożystaniem tablicy trójwymiarowej
     *
     * @return Instances (przygotowana tablica instancji do wypełnienia jej znanymi danymi
     */
    public Instances makeInstances() {
        List<?> lst = getAllBodyType();
        String[] bodyTypes = new String[lst.size()];
        String[] bodyTypes0 = new String[lst.size() + 1];
        bodyTypes0[0] = "0";
        for (int i = 0; i < lst.size(); i++) {
            bodyTypes[i] = (String) lst.get(i);
            bodyTypes0[i + 1] = (String) lst.get(i);
        }
        lst.clear();
        lst = getAllFuelType();
        String[] fuelTypes = new String[lst.size()];
        String[] fuelTypes0 = new String[lst.size() + 1];
        fuelTypes0[0] = "0";
        for (int i = 0; i < lst.size(); i++) {
            fuelTypes[i] = (String) lst.get(i);
            fuelTypes0[i + 1] = (String) lst.get(i);
        }
        String[] allMakes = getAllMakes();
        String[] allMakes0 = new String[allMakes.length + 1];
        for (int i = 0; i < allMakes.length; i++) {
            allMakes0[i] = allMakes[i];
        }
        allMakes0[allMakes0.length - 1] = "0";
        //Beda utworzone trzy atrybuty:
        // 1. price_raw_from        Numeryczny (o wartosciach liczbowych),
        // 2. price_raw_to          Numeryczny (o wartosciach liczbowych),
        // 3. year_from             Numeryczny (o wartosciach liczbowych),
        // 4. year_to               Numeryczny (o wartosciach liczbowych),
        // 5. door_count_from       Numeryczny (o wartosciach liczbowych),
        // 6. door_count_to         Numeryczny (o wartosciach liczbowych),
        // 7. fuel_typeK            Tekstowy (o wartosciach typu String),
        // 8. body_typeK            Tekstowy (o wartosciach typu String),
        // 9. makeK                 Tekstowy (o wartosciach typu String),
        // 10. engine_capacity_from Numeryczny (o wartosciach liczbowych),
        // 11. engine_capacity_to   Numeryczny (o wartosciach liczbowych),
        // 12. body_type            Tekstowy (o wartosciach typu String),
        // 13. door_count           Numeryczny (o wartosciach liczbowych),
        // 14. fuel_type            Tekstowy (o wartosciach typu String),
        // 15. make                 Tekstowy (o wartosciach typu String),
        // 16. year                 Numeryczny (o wartosciach liczbowych),
        // 17. price_raw            Numeryczny (o wartosciach liczbowych),
        // 18. engine_capacity      Numeryczny (o wartosciach liczbowych),
        // 19. DEC                  Symboliczny (wartosci: nie_podobne, podobne i bardzo_podobne)
        String[][][] attribNameAndTypeAndPosibleValues = new String[][][]{
                {{"price_raw_from"}, {"price_raw_to"}, {"year_from"}, {"year_to"}, {"door_count_from"}, {"door_count_to"},
                        {"fuel_typeK"}, {"body_typeK"}, {"makeK"}, {"engine_capacity_from"}, {"engine_capacity_to"},
                        {"body_type"}, {"door_count"}, {"fuel_type"}, {"make"}, {"year"}, {"price_raw"}, {"engine_capacity"},
                        {"DEC"}},
                {{"Numeryczny"}, {"Numeryczny"}, {"Numeryczny"}, {"Numeryczny"}, {"Numeryczny"}, {"Numeryczny"},
                        {"Symboliczny"}, {"Symboliczny"}, {"Symboliczny"}, {"Numeryczny"}, {"Numeryczny"}, {"Symboliczny"},
                        {"Numeryczny"}, {"Symboliczny"}, {"Symboliczny"}, {"Numeryczny"}, {"Numeryczny"}, {"Numeryczny"},
                        {"Symboliczny"}},
                {{}, {}, {}, {}, {}, {}, fuelTypes0, bodyTypes0, allMakes0, {}, {}, bodyTypes, {}, fuelTypes, allMakes, {},
                        {}, {}, {"nie_podobne", "podobne", "bardzo_podobne"}}};

        ArrayList<Attribute> attributes = new ArrayList();
        for (int i = 0; i < 19; i++) {
            if (attribNameAndTypeAndPosibleValues[1][i][0] == "Symboliczny") {
//                System.out.println(attribNameAndTypeAndPosibleValues[0][i][0] + " " +
//                        attribNameAndTypeAndPosibleValues[1][i][0]);
                ArrayList<String> labs = new ArrayList();
                for (int k = 0; k < attribNameAndTypeAndPosibleValues[2][i].length; k++) {
                    if (attribNameAndTypeAndPosibleValues[2][i][k] != null) {
                        labs.add(attribNameAndTypeAndPosibleValues[2][i][k]);
                    }
                }
                Attribute attr = new Attribute(attribNameAndTypeAndPosibleValues[0][i][0], labs);
                attributes.add(attr);
            } else {
                if (attribNameAndTypeAndPosibleValues[1][i][0] == "Numeryczny") {
                    Attribute attr = new Attribute(attribNameAndTypeAndPosibleValues[0][i][0]);
                    attributes.add(attr);
                }
            }
        }
        Instances dataInstances = new Instances("New Array", attributes, 0);
        return dataInstances;
    }

    /**
     * Ładowanie pliku *.arff
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static Instances loadData(String fileName) throws IOException {
        ArffLoader loader = new ArffLoader(); //Utworzenie obiektu czytajacego dane z formatu ARFF
        loader.setFile(new File(fileName)); //Ustawienie pliku do odczytania
        return loader.getDataSet(); //Odczytanie danych z pliku
    }


    public List<String> getAllFuelType() {
        TypedQuery<String> query = entityManager.createQuery("select distinct(c.fuel_type) from Car c " +
                "where c.fuel_type is not null", String.class);
        List<String> resultList = query.getResultList();
        if (resultList.isEmpty())
            return null;
        return resultList;
    }

    public List<String> getAllBodyType() {
        TypedQuery<String> query = entityManager.createQuery("select distinct(c.body_type) from Car c " +
                "where c.body_type is not null", String.class);
        List<String> resultList = query.getResultList();
        if (resultList.isEmpty())
            return null;
        return resultList;
    }

    public static String[] getAllMakes() {
        return new String[]{"Acura", "Aixam", "Alfa Romeo", "Aro", "Aston Martin", "Audi",
                "Bentley", "BMW", "Brilliance", "Buick", "Cadillac", "Chatenet", "Chevrolet", "Chrysler",
                "Citroën", "Dacia", "Daewoo", "Daihatsu", "DFSK", "DKW", "Dodge", "Eagle", "Ferrari",
                "Fiat", "Ford", "Gaz", "GMC", "Gonow", "Grecav", "Honda", "Hummer", "Hyundai", "Infiniti",
                "Isuzu", "Iveco", "Jaguar", "Jeep", "Kia", "Lada", "Lamborghini", "Lancia", "Land Rover",
                "Lexus", "Ligier", "Lincoln", "Lotus", "LTI", "Mahindra", "Maserati", "Mazda", "Mercedes-Benz",
                "Mercury", "MG", "Microcar", "Mini", "Mitsubishi", "Moskwicz", "Nissan", "NSU", "Nysa",
                "Oldsmobile", "Opel", "Peugeot", "Piaggio", "Plymouth", "Polonez", "Pontiac", "Porsche",
                "Renault", "Rolls-Royce", "Rover", "Saab", "Santana", "Seat", "Shuanghuan", "Škoda", "Smart",
                "SsangYong", "Subaru", "Suzuki", "Syrena", "Talbot", "Tarpan", "Tata", "Tesla", "Toyota",
                "Trabant", "Triumph", "Uaz", "Vauxhall", "Volkswagen", "Volvo", "Warszawa", "Wartburg",
                "Wołga", "Yugo", "Zaporożec", "Zastava", "Żuk", "Inny"};
    }
}
