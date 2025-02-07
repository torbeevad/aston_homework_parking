package ru.aston;

import ru.aston.dao.SimpleDao;
import ru.aston.dao.TicketDao;
import ru.aston.dao.VehicleDao;
import ru.aston.dao.ParkingSpotDao;
import ru.aston.db.DBInitializer;
import ru.aston.model.ParkingObject;
import ru.aston.model.Ticket;
import ru.aston.model.Vehicle;
import ru.aston.model.ParkingSpot;
import org.h2.tools.Server;
import ru.aston.db.H2DBInitializer;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ParkingApp {

    public static void main (String[] args) throws SQLException {

        DBInitializer initializer = new H2DBInitializer();
        initializer.initialize();

        ParkingSpotDao parkingSpotDao =  new ParkingSpotDao();
        VehicleDao vehicleDao = new VehicleDao();
        TicketDao ticketDao = new TicketDao();

        Server h2WebServer = org.h2.tools.Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
        h2WebServer.start();

        while(true){

            System.out.println("Выберите таблицу: \n"+
                    "1. Пользователи\n"+
                    "2. Парковочные талоны\n"+
                    "3. Парковочные места\n"+
                    "4. Машины\n"+
                    "5. Выход\n");
            Scanner scanner = new Scanner(System.in);
            int k = scanner.nextInt();

            switch (k){
                case 1:{

                    break;
                }

                case 2:{
                    displaySubMenu(ticketDao);
                    break;
                }

                case 3:{
                    displaySubMenu(parkingSpotDao);
                    break;
                }

                case 4:{
                    displaySubMenu(vehicleDao);
                    break;
                }

                case 5: {
                    h2WebServer.stop();
                    return;
                }
                default:{System.out.println("Wrong key");}
            }
        }
    }

    public static <T extends ParkingObject> void displaySubMenu(SimpleDao<T> dao) {
        while(true)  {
            System.out.println("\nВыберите действие: \n" +
                    "1. Добавить запись\n" +
                    "2. Удалить запись\n" +
                    "3. Найти запись по id\n" +
                    "4. Вывести таблицу\n"+
                    "5. Назад\n");
            Scanner scanner = new Scanner(System.in);
            int j = scanner.nextInt();

            switch (j){

                case 1:{
                    addRecord(dao);
                    break;
                }

                case 2:{
                    deleteRecordById(dao);
                    break;
                }

                case 3:{
                    findRecordById(dao);
                    break;
                }

                case 4:{
                    listRecords(dao);
                    break;
                }

                case 5:{
                    return;
                }
                default:{System.out.println("Wrong key");}
            }
        }
    }

    private static <T extends ParkingObject> void addRecord(SimpleDao<T> dao){
        String daoName = dao.getClass().getSimpleName();

        switch (daoName){
            case "1":{break;}
            case "TicketDao": {addNewTicket((TicketDao) dao);break;}
            case "ParkingSpotDao":{addNewParkingSpot((ParkingSpotDao) dao);break;}
            case "VehicleDao":{addNewVehicle((VehicleDao) dao);break;}
        }
    }

    private static void addNewTicket(TicketDao dao) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("userID: ");
        int userId = scanner.nextInt();
        System.out.println("carID: ");
        int carId = scanner.nextInt();
        System.out.println("parkSpotId: ");
        int parkSpotId = scanner.nextInt();
        System.out.println("Часов парковки: ");
        int parkingTimeInHours = scanner.nextInt();

        Ticket record = new Ticket();
        record.setUserId(userId);
        record.setCarId(carId);
        record.setParkSpotId(parkSpotId);
        record.setParkingTimeInHours(parkingTimeInHours);

        dao.save(record);
        System.out.println("Ticket added successfully!");
    }

    private static void addNewVehicle(VehicleDao dao) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите номерной знак: \n" );
        String plate = scanner.next();
        System.out.println("Введите модель машины: \n" );
        String model = scanner.next();
        System.out.println("Введите год производства: \n" );
        String year = scanner.next();

        Vehicle record = new Vehicle();
        record.setPlate(plate);
        record.setModel(model);
        record.setRelease_year(year);

        dao.save(record);
        System.out.println("Vehicle added successfully!");
    }

    private static void addNewParkingSpot(ParkingSpotDao dao) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter spot number: ");
        int spotNumber = scanner.nextInt();

        ParkingSpot spot = new ParkingSpot();
        spot.setSpotNumber(spotNumber);
        spot.setAvailable(true);

        dao.save(spot);
        System.out.println("Parking spot added successfully!");
    }

    private static <T extends ParkingObject> void listRecords(SimpleDao<T> dao) {
        List<T> records = dao.findAll();
        String objName = getObjectName(dao.getClass());

        if (records.isEmpty()) {
            System.out.println("No "+ objName +"s found.");
        } else {
            records.stream().map(T::toString).forEach(System.out::println);
        }
    }

    private static <T> void findRecordById(SimpleDao<T> dao) {
        Scanner scanner = new Scanner(System.in);
        String objName = getObjectName(dao.getClass());
        System.out.print("Enter "+objName+" id: ");

        int recordId = scanner.nextInt();
        Optional<T> optRecord = dao.findById(recordId);

        optRecord.ifPresentOrElse(
                item -> System.out.println(item.toString()),
                () -> System.out.println("No record found.")
        );
    }

    private static <T> void deleteRecordById(SimpleDao<T> dao) {
        Scanner scanner = new Scanner(System.in);
        String objName = getObjectName(dao.getClass());
        System.out.print("Enter "+objName+" id: ");

        int recordId = scanner.nextInt();
        dao.deleteById(recordId);
    }

    private static String getObjectName(Class<?> cls) {
        String className = cls.getSimpleName();
        return className.substring(0,className.length()-3);
    }
}
