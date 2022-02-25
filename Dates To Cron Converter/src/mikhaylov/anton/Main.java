package mikhaylov.anton;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws DatesToCronConvertException {
        DatesToCronConverterImpl datesToCronConverter = new DatesToCronConverterImpl();
        System.out.println(datesToCronConverter.getImplementationInfo());
        List<String> input = new ArrayList<>();

        input.add("2022-01-25T08:00:00");
        input.add("2022-01-25T08:30:00");
        input.add("2022-01-25T09:00:00");
        input.add("2022-01-25T09:30:00");
        input.add("2022-01-26T08:00:00");
        input.add("2022-01-26T08:30:00");
        input.add("2022-01-26T09:00:00");
        input.add("2022-01-26T09:30:00");

        String crone = datesToCronConverter.convert(input);
        System.out.println(crone);

        List<String> input2 = new ArrayList<>();

        input2.add("2022-01-24T19:53:00");
        input2.add("2022-01-24T19:54:00");
        input2.add("2022-01-24T19:55:00");
        input2.add("2022-01-24T19:56:00");
        input2.add("2022-01-24T19:57:00");
        input2.add("2022-01-24T19:58:00");
        input2.add("2022-01-24T19:59:00");
        input2.add("2022-01-24T20:00:00");
        input2.add("2022-01-24T20:01:00");
        input2.add("2022-01-24T20:02:00");

        String crone2 = datesToCronConverter.convert(input2);
        System.out.println(crone2);

    }
}
