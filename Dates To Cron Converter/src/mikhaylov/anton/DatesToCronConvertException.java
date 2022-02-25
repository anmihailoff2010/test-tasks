package mikhaylov.anton;

public class DatesToCronConvertException extends Exception {

    String message;
    public DatesToCronConvertException(String message) {
        super(message);
    }
}
