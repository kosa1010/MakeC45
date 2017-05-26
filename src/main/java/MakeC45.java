import models.Car;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by kosa1010 on 23.05.17.
 */
public class MakeC45 {

    public static void main(String[] args) {
        EntityManager entityManager;
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("my_app");
        entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        List<Car> listCars = entityManager.createQuery("SELECT c FROM Car c", Car.class).getResultList();
        String[][] cars = new String[listCars.size()][7];
        for (int k = 0; k < listCars.size(); k++) {
            cars[k][0] = listCars.get(k).getBody_type();
            cars[k][1] = String.valueOf(listCars.get(k).getDoor_count());
            cars[k][2] = listCars.get(k).getFuel_type();
            cars[k][3] = listCars.get(k).getMake();
            cars[k][4] = String.valueOf(listCars.get(k).getYear());
            cars[k][5] = String.valueOf(listCars.get(k).getPrice_raw());
            cars[k][6] = String.valueOf(listCars.get(k).getEngine_capacity());
        }
        entityManager.getTransaction().commit();
        entityManager.close();

        int index = 0;
        int podobne = 0;
        int bardzo_podobne = 0;
        int nie_podobne = 0;
        String[][] informationSystem = concatenateArray(usersPrefers(), cars);

        for (String[] item :
                informationSystem) {
            System.out.print(index + "\t");
            for (String s :
                    item) {
                System.out.print(s + "\t");
            }
            System.out.println();
//            index++;
        }
        try {
            String[][] informationSystem2 = setDecision(informationSystem);
            for (int i = 0; i < informationSystem2.length; i++) {
                if (informationSystem2[i][14] == null) {
                    System.out.println(i);
                    index++;
                } else {
                    if (informationSystem2[i][14] == "bardzo_podobne") {
                        bardzo_podobne++;
//                        for (String s : informationSystem2[i]) {
//                            System.out.print(s + ", ");
//                        }
//                        System.out.println();
                    } else {
                        if (informationSystem2[i][14] == "podobne") {
                            podobne++;
//                            for (String s : informationSystem2[i]) {
//                                System.out.print(s + ", ");
//                            }
//                            System.out.println();
                        } else {
                            nie_podobne++;
                        }
                    }
                }
            }
            System.out.println("Liczba obiektów bez decyzji " + index);
            System.out.println("Liczba obiektów nie podobnych " + nie_podobne);
            System.out.println("Liczba obiektów podobnych " + podobne);
            System.out.println("Liczba obiektów bardzo podobnych " + bardzo_podobne);
            saveAsCSV(informationSystem2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[][] usersPrefers() {
        return new String[][]{
                {"7500", "1999", "5", "Benzyna", "Sedan/Limuzyna", "Honda", "1900"},
                {"10000", "2001", "3", "Benzyna", "Hatchback", "Renault", "1700"},
                {"38000", "2007", "5", "Diesel", "Van (minibus)", "0", "1500"},
                {"20000", "2007", "5", "Diesel", "Kombi", "0", "1500"},
                {"5000", "1998", "5", "Benzyna+LPG", "Hatchback", "0", "1400"},
                {"200000", "2012", "2", "Benzyna", "Sportowy/Coupe", "0", "4650"},
                {"15000", "2005", "3", "Benzyna", "0", "0", "1600"},
                {"70000", "2011", "3", "0", "0", "0", "1600"},
                {"23000", "2010", "4", "Diesel", "0", "0", "1800"},
                {"7500", "2004", "5", "0", "0", "0", "1700"},
                {"12500", "2010", "3", "0", "0", "0", "1600"},
                {"40000", "2010", "4", "0", "Hatchback", "0", "2000"}
        };
    }

    /**
     * Łączenie tablic w jedną z permutacjami zestawień aut z użytkownikami
     *
     * @param users
     * @param cars
     * @return
     */
    public static String[][] concatenateArray(String[][] users, String[][] cars) {
        String[][] arrayToClassifier = new String[users.length * cars.length][users[0].length + cars[0].length + 1];
        for (int i = 0; i < arrayToClassifier.length; i++) {
            for (int j = 0; j < arrayToClassifier[0].length - 1; j++) {
                for (int k = 0; k < users[0].length; k++) {
                    arrayToClassifier[i][j] = users[i / cars.length][k];
                    j++;
                }
                for (int l = 0; l < cars[0].length; l++) {
                    arrayToClassifier[i][j] = cars[i % cars.length][l];
                    j++;
                }
            }
        }
        return arrayToClassifier;
    }

    /**
     * zapisywanie tablicy do CSV
     *
     * @param obj
     * @throws FileNotFoundException
     */

    public static void saveAsCSV(String[][] obj) throws FileNotFoundException {
        String[] headers = new String[]{
                "price_rawK", "yearK", "door_countK", "fuel_typeK",
                "body_typeK", "makeK", "engine_capacityK", "body_type", "door_count",
                "fuel_type", "make", "year", "price_raw", "engine_capacity", "DEC"};
        PrintWriter pw = new PrintWriter(new File("/home/kosa1010/Pulpit/Amgr/test.csv"));
        StringBuilder sb = new StringBuilder();
        for (String s : headers) {
            if (s != "DEC") {
                sb.append(s);
                sb.append(",");
            } else {
                sb.append(s);
                sb.append("\n");
            }
        }
        for (String[] sline : obj) {
            for (int i = 0; i < sline.length; i++) {
                if (i != 18) {
                    sb.append(sline[i]);
                    sb.append(",");
                } else {
                    sb.append(sline[i]);
                    sb.append("\n");
                }
            }
        }
        pw.write(sb.toString());
        pw.close();
        System.out.println("done");
    }

    public static String[][] setDecision(String[][] array) {
//        System.out.println("|" + array[0][6] + "|");
////        System.out.println("|" + array[0][13] + "|");
//        for (int i = 0; i < array.length; i++) {
//            if (!array[i][6].equals(array[i][13]) && !array[i][6].equals("0")) {
//                array[i][18] = "nie_podobne";
//            } else {
//                if (!array[i][7].equals(array[i][11]) && !array[i][7].equals("0")) {
//                    array[i][18] = "nie_podobne";
//                } else {
//                    if (!array[i][8].equals(array[i][14]) && !array[i][8].equals("0")) {
//                        array[i][18] = "nie_podobne";
//                    } else {
//                        if (Integer.valueOf(array[i][4]) > Integer.valueOf(array[i][12]) ||
//                                Integer.valueOf(array[i][12]) > Integer.valueOf(array[i][5])) {
//                            array[i][18] = "nie_podobne";
//                        } else {
//                            if (Integer.valueOf(array[i][2]) > Integer.valueOf(array[i][15]) ||
//                                    Integer.valueOf(array[i][15]) > Integer.valueOf(array[i][3])) {
//                                array[i][18] = "nie_podobne";
//                            } else {
//                                //cena
//                                if (Double.parseDouble(array[i][0]) - 5000 > Double.parseDouble(array[i][16]) ||
//                                        Double.parseDouble(array[i][16]) > Double.parseDouble(array[i][1]) + 5000) {
//                                    array[i][18] = "nie_podobne";
//                                } else {
//                                    if (Double.parseDouble(array[i][0]) > Double.parseDouble(array[i][16]) ||
//                                            Double.parseDouble(array[i][16]) > Double.parseDouble(array[i][1])) {
//                                        array[i][18] = "podobne";
//                                    } else {
//                                        //pojemność
//                                        if (Integer.parseInt(array[i][9]) - 200 > Integer.parseInt(array[i][17]) ||
//                                                Integer.parseInt(array[i][17]) > Integer.parseInt(array[i][10]) + 200) {
//                                            array[i][18] = "nie_podobne";
//                                        } else {
//                                            if (Integer.parseInt(array[i][9]) > Integer.parseInt(array[i][17]) ||
//                                                    Integer.parseInt(array[i][17]) > Integer.parseInt(array[i][10])) {
//                                                array[i][18] = "podobne";
//                                            } else {
//                                                array[i][18] = "bardzo_podobne";
//                                            }
//                                        }
//                                    }
//
//
//                                    if (Integer.parseInt(array[i][9]) - 200 > Integer.parseInt(array[i][17]) ||
//                                            Integer.parseInt(array[i][17]) > Integer.parseInt(array[i][10]) + 200) {
//                                        array[i][18] = "nie_podobne";
//                                    } else {
//                                        if (Integer.parseInt(array[i][9]) > Integer.parseInt(array[i][17]) ||
//                                                Integer.parseInt(array[i][17]) > Integer.parseInt(array[i][10])) {
//                                            array[i][18] = "podobne";
//                                        } else {
//                                            if (Double.parseDouble(array[i][0]) - 5000 > Double.parseDouble(array[i][16]) ||
//                                                    Double.parseDouble(array[i][16]) > Double.parseDouble(array[i][1]) + 5000) {
//                                                array[i][18] = "nie_podobne";
//                                            } else {
//                                                if (Double.parseDouble(array[i][0]) > Double.parseDouble(array[i][16]) ||
//                                                        Double.parseDouble(array[i][16]) > Double.parseDouble(array[i][1])) {
//                                                    array[i][18] = "podobne";
//                                                } else {
//                                                    array[i][18] = "bardzo_podobne";
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
        int index = 0;
        for (int i = 0; i < array.length; i++) {
            if (Double.parseDouble(array[i][0]) + 5000 > Double.parseDouble(array[i][12]) &&
                    Double.parseDouble(array[i][0]) - 5000 < Double.parseDouble(array[i][12])) {
//                System.out.println(i);
                index++;
            }

        }
        System.out.println(index);
        return array;
    }
}
