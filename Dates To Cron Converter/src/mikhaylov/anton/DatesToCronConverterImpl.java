package mikhaylov.anton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class DatesToCronConverterImpl implements DatesToCronConverter {

    @Override
    public String convert(List<String> dates) throws DatesToCronConvertException {
        List<Data> datalist = convertToDataArray(dates);

        int[] years = datalist.stream().map(Data::getYear).distinct().mapToInt(x -> x).sorted().toArray();
        int[] months = datalist.stream().map(Data::getMonth).distinct().mapToInt(x -> x).sorted().toArray();
        int[] monthday = datalist.stream().map(Data::getDay).distinct().mapToInt(x -> x).sorted().toArray();
        int[] days = findNamesOfDay(datalist);
        int[] hours = datalist.stream().map(Data::getHour).distinct().mapToInt(x -> x).sorted().toArray();
        int[] minutes = datalist.stream().map(Data::getMinute).distinct().mapToInt(x -> x).sorted().toArray();
        int[] seconds = datalist.stream().map(Data::getSecond).distinct().mapToInt(x -> x).sorted().toArray();

        String year = findYearCrone(years);
        String month = findMonthCrone(months, monthday, hours, minutes, seconds);
        String monthdays = findMonthDays(monthday, hours, minutes, seconds);
        String day = findDayCrone(days, hours, minutes, seconds);

        String hour = findHourCrone(hours, minutes, seconds);
        String minute = findMinuteCrone(minutes, seconds);
        String second = findSecondCrone(seconds);

        return second + " " + minute + " " + hour + " " + monthdays + " " + month + " " + day;
    }

    private int[] findNamesOfDay(List<Data> datalist) {

        int[] days = new int[datalist.size()];

        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < datalist.size(); i++) {
            calendar.set(datalist.get(i).year, datalist.get(i).month - 1, datalist.get(i).day);
            days[i] = calendar.get(Calendar.DAY_OF_WEEK);
        }

        Arrays.sort(days);

        int[] result = findDistictNumbers(days);

        return result;
    }

    public final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private List<Data> convertToDataArray(List<String> dates) throws DatesToCronConvertException {
        List<Data> result = new ArrayList<>();

        for (int i = 0; i < dates.size(); i++) {

            Data newdata = parseData(dates.get(i));
            result.add(newdata);

        }
        return result;
    }

    private Data parseData(String data) throws DatesToCronConvertException {
        Data dataclass = new Data();

        if (data.charAt(4) != '-')
            throw new DatesToCronConvertException("Неправильные символы при вводе данных");
        if (data.charAt(7) != '-')
            throw new DatesToCronConvertException("Неправильные символы при вводе данных");
        if (data.charAt(10) != 'T')
            throw new DatesToCronConvertException("Неправильные символы при вводе данных");
        if (data.charAt(13) != ':')
            throw new DatesToCronConvertException("Неправильные символы при вводе данных");
        if (data.charAt(16) != ':')
            throw new DatesToCronConvertException("Неправильные символы при вводе данных");

        String[] parseinfo = data.split("T");
        String[] calendar = parseinfo[0].split("-");
        String[] time = parseinfo[1].split(":");

        dataclass.setYear(Integer.parseInt(calendar[0]));
        dataclass.setMonth(Integer.parseInt(calendar[1]));
        dataclass.setDay(Integer.parseInt(calendar[2]));

        dataclass.setHour(Integer.parseInt(time[0]));
        dataclass.setMinute(Integer.parseInt(time[1]));
        dataclass.setSecond(Integer.parseInt(time[2]));

        System.out.println(dataclass.toString());

        return dataclass;
    }

    private String findYearCrone(int[] year) {
        if (year.length == 1) {
            return year[0] + "";
        }

        int[] difference = new int[year.length - 1];

        for (int i = 0; i < year.length - 1; i++) {
            difference[i] = year[i + 1] - year[i];
        }

        int[] distinct = findDistictNumbers(difference);

        if (year.length > 1 && distinct.length == 1 && distinct[0] == 1) {
            return "*";
        }

        String defaultresult = "";
        for (int i = 0; i < year.length - 1; i++) {
            defaultresult = defaultresult + year[i] + ",";
        }
        defaultresult = defaultresult + year[year.length - 1];

        return defaultresult;

    }

    private String findMonthCrone(int[] month, int[] monthday, int[] hour, int[] min, int[] sec) {

        if (findMonthDays(monthday, hour, min, sec).equals("*")) {
            return "*";
        }

        if (month.length == 1) {
            return month[0] + "";
        }

        int[] difference = new int[month.length - 1];

        for (int i = 0; i < month.length - 1; i++) {
            difference[i] = month[i + 1] - month[i];
        }

        int[] distinct = findDistictNumbers(difference);

        if (distinct.length == 1 && distinct[0] == 1) {
            return "*";
        }

        if (distinct.length == 1 && distinct[0] == 2) {
            return "*/2";
        }

        if (distinct.length == 1 && distinct[0] == 3) {
            return "*/3";
        }

        if (distinct.length == 1 && distinct[0] == 4) {
            return "*/4";
        }

        if (distinct.length == 1 && distinct[0] == 6) {
            return "*/6";
        }

        String defaultresult = "";
        for (int i = 0; i < month.length - 1; i++) {
            defaultresult = defaultresult + month[i] + ",";
        }
        defaultresult = defaultresult + month[month.length - 1];

        return defaultresult;
    }

    private String findMonthDays(int[] monthdays, int[] hours, int[] min, int[] sec) {

        if (findHourCrone(hours, min, sec).equals("*")) {
            return "*";
        }

        if (monthdays.length == 1) {
            return "*";
        }

        int[] difference = new int[monthdays.length - 1];

        for (int i = 0; i < monthdays.length - 1; i++) {
            difference[i] = monthdays[i + 1] - monthdays[i];
        }

        int[] distinct = findDistictNumbers(difference);

        if (distinct.length == 1 && distinct[0] == 1) {
            return "*";
        }

        StringBuilder defaultresult = new StringBuilder();
        for (int i = 0; i < monthdays.length - 1; i++) {
            defaultresult.append(monthdays[i]).append(",");
        }
        defaultresult.append(monthdays[monthdays.length - 1]);
        return defaultresult.toString();

    }

    private String findDayCrone(int[] day, int[] hour, int[] min, int[] sec) {

        if (day.length == 1) {
            return Data.getDay(day[0]);
        }
        if (findMonthDays(day, hour, min, sec).equals("*")) {
            return "*";
        }

        return "?";

    }

    private String findHourCrone(int[] hour, int[] min, int[] sec) {

        if (hour.length == 1) {
            return hour[0] + "";
        }

        if (findMinuteCrone(min, sec).equals("*")) {
            return "*";
        }

        int[] difference = new int[hour.length - 1];

        for (int i = 0; i < hour.length - 1; i++) {
            difference[i] = hour[i + 1] - hour[i];
        }

        int[] distinct = findDistictNumbers(difference);

        String customresult = "";

        if (hour[0] != 0 && distinct.length == 1 && distinct[0] == 1) {
            customresult = hour[0] + "-" + hour[hour.length - 1];
            return customresult;
        }

        if (distinct.length == 1 && distinct[0] == 1) {
            return "*";
        }

        if (distinct.length == 1 && distinct[0] == 2) {
            return "*/2";
        }

        if (distinct.length == 1 && distinct[0] == 3) {
            return "*/3";
        }

        if (distinct.length == 1 && distinct[0] == 4) {
            return "*/4";
        }

        if (distinct.length == 1 && distinct[0] == 6) {
            return "*/6";
        }

        if (distinct.length == 1 && distinct[0] == 12) {
            return "*/12";
        }

        StringBuilder defaultresult = new StringBuilder();
        for (int i = 0; i < hour.length - 1; i++) {
            defaultresult.append(hour[i]).append(",");
        }
        defaultresult.append(hour[hour.length - 1]);
        return defaultresult.toString();
    }

    private String findMinuteCrone(int[] minute, int[] sec) {

        if (minute.length == 1) {
            return minute[0] + "";
        }

        if (findSecondCrone(sec).equals("*")) {
            return "*";
        }

        int[] difference = new int[minute.length - 1];

        for (int i = 0; i < minute.length - 1; i++) {
            difference[i] = minute[i + 1] - minute[i];
        }

        int[] distinct = findDistictNumbers(difference);

        if (distinct.length == 1 && minute.length > 5) {
            return "*";
        }

        if (distinct.length == 2 && distinct[0] == 1 && minute[minute.length - 1] == 59 && minute[0] == 0) {
            return "*";
        }

        if (distinct.length == 1 && distinct[0] == 1) {
            return "*";
        }

        if (distinct.length == 1 && distinct[0] == 2) {
            return "*/2";
        }

        if (distinct.length == 1 && distinct[0] == 3) {
            return "*/3";
        }

        if (distinct.length == 1 && distinct[0] == 4) {
            return "*/4";
        }

        if (distinct.length == 1 && distinct[0] == 5) {
            return "*/5";
        }

        if (distinct.length == 1 && distinct[0] == 6) {
            return "*/6";
        }

        if (distinct.length == 1 && distinct[0] == 10) {
            return "*/10";
        }

        if (distinct.length == 1 && distinct[0] == 12) {
            return "*/12";
        }

        if (distinct.length == 1 && distinct[0] == 15) {
            return "*/15";
        }

        if (distinct.length == 1 && distinct[0] == 30) {
            return "*/30";
        }

        StringBuilder defaultresult = new StringBuilder();
        for (int i = 0; i < minute.length - 1; i++) {
            defaultresult.append(minute[i]).append(",");
        }
        defaultresult.append(minute[minute.length - 1]);
        return defaultresult.toString();
    }

    private String findSecondCrone(int[] second) {

        if (second.length == 1) {
            return second[0] + "";
        }

        int[] difference = new int[second.length - 1];

        for (int i = 0; i < second.length - 1; i++) {
            difference[i] = second[i + 1] - second[i];
        }

        int[] distinct = findDistictNumbers(difference);

        if (distinct.length == 1 && distinct[0] == 1) {
            return "*";
        }

        if (distinct.length == 1 && distinct[0] == 2) {
            return "*/2";
        }

        if (distinct.length == 1 && distinct[0] == 3) {
            return "*/3";
        }

        if (distinct.length == 1 && distinct[0] == 4) {
            return "*/4";
        }

        if (distinct.length == 1 && distinct[0] == 5) {
            return "*/5";
        }

        if (distinct.length == 1 && distinct[0] == 6) {
            return "*/6";
        }

        if (distinct.length == 1 && distinct[0] == 10) {
            return "*/10";
        }

        if (distinct.length == 1 && distinct[0] == 12) {
            return "*/12";
        }

        if (distinct.length == 1 && distinct[0] == 15) {
            return "*/15";
        }

        if (distinct.length == 1 && distinct[0] == 30) {
            return "*/30";
        }

        StringBuilder defaultresult = new StringBuilder();
        for (int i = 0; i < second.length - 1; i++) {
            defaultresult.append(second[i]).append(",");
        }
        defaultresult.append(second[second.length - 1]);
        return defaultresult.toString();
    }

    private int[] findDistictNumbers(int[] difference) {
        int index = 1;
        int[] distinct = new int[index];
        boolean haveit;
        distinct[0] = difference[0];
        for (int i = 0; i < difference.length; i++) {
            haveit = false;
            for (int j = 0; j < distinct.length; j++) {
                if (distinct[j] == difference[i])
                    haveit = true;
            }
            if (haveit == false) {
                index++;
                int[] temp = new int[index];
                for (int k = 0; k < distinct.length; k++) {
                    temp[k] = distinct[k];
                }
                temp[index - 1] = difference[i];
                distinct = new int[index];
                for (int j = 0; j < temp.length; j++) {
                    distinct[j] = temp[j];
                }
            }
        }
        return distinct;
    }

    @Override
    public String getImplementationInfo() {
        return "Mikhaylov Anton Mikhaylovich " + this.getClass() + " githab link: https://github.com/anmihailoff2010?tab=repositories";
    }
}
