package mikhaylov.anton;

import java.util.List;

public interface DatesToCronConverter {
    public final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    String convert(List<String> dates) throws DatesToCronConvertException;
    String getImplementationInfo();
}
