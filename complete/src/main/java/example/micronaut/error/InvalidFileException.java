package example.micronaut.error;

public class InvalidFileException extends RuntimeException{
    public InvalidFileException(String msg){
        super(msg);
    }
}
