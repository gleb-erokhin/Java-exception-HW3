import java.io.*;
import java.nio.file.FileSystemException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        try {
            makeRecord();
            System.out.println("success");
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void makeRecord() throws Exception{
        System.out.println("Введите фамилию, имя, отчество, дату рождения (в формате dd.mm.yyyy), номер телефона (число без разделителей) и пол(символ латиницей f или m), разделенные пробелом");

        String text;
        try(BufferedReader bf = new BufferedReader(new InputStreamReader(System.in))) {
            text = bf.readLine();
        }catch (IOException e){
            throw new ConsoleWorkException("Произошла ошибка при работе с консолью");
        }

        String[] array = text.split(" ");
        if (array.length != 6){
            throw new SplitDataException("Введено неверное количество параметров, ожидается = 6, " + "получено = " + array.length);
        }

        String surname = array[0];
        String name = array[1];
        String patronymic = array[2];

        SimpleDateFormat format = new SimpleDateFormat("dd.mm.yyyy");
        Date birthdate;
        try {
            birthdate = format.parse(array[3]);
            if(array[3].length() != 10){
                throw new FormatParseException("Неверный формат даты рождения, ожидается dd.mm.yyyy, получено = " + array[3]);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        int phone;
        try {
            phone = Integer.parseInt(array[4]);
        }catch (NumberFormatException e){
            throw new NumberPhoneException("Неверный формат телефона, ожидается 1234567, получено = " + array[4]);
        }

        String sex = array[5];
        if (!sex.toLowerCase().equals("m") && !sex.toLowerCase().equals("f")){
            throw new RuntimeException("Неверно введен пол, ожидается 'f' или 'm', получено = " + sex);
        }

        String fileName = surname.toLowerCase() + ".txt";
        File file = new File(fileName);
        try (FileWriter fileWriter = new FileWriter(file, true)){
            if (file.length() > 0){
                fileWriter.write('\n');
            }
            fileWriter.write(String.format("%s %s %s %s %s %s", surname, name, patronymic, format.format(birthdate), phone, sex));
        }catch (IOException e){
            throw new FileSystemException("Возникла ошибка при работе с файлом");
        }
    }
}