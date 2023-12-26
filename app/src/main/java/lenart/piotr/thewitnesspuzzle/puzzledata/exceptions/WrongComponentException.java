package lenart.piotr.thewitnesspuzzle.puzzledata.exceptions;

public class WrongComponentException extends Exception {
    public <T> WrongComponentException(Object thowableObject, Class<T> requiredClass, Object receivedObject) {
        super("Class " + thowableObject.getClass().getName() + " required " + requiredClass.getName() + " but got " + receivedObject.getClass().getName());
    }
}
