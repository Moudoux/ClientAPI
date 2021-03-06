package me.zero.client.api.util;

import me.zero.client.api.exception.ArraySizeException;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

/**
 * Utils that the Client API uses
 *
 * @author Brady
 * @since 1/20/2017 12:00 PM
 */
public final class ClientUtils {

    private ClientUtils() {}

    /**
     * Concatenates an array of generic arrays
     *
     * @param arrays The arrays being concatenated
     * @return The concatenated array
     */
    @SafeVarargs
    public static <T> T[] concat(T[]... arrays){
        if (arrays.length < 2)
            throw new ArraySizeException("At least 2 arrays should be supplied");

        T[] result = arrays[0];
        for (int i = 1; i < arrays.length; i++) {
            T[] newArray = Arrays.copyOf(result, result.length + arrays[i].length);
            System.arraycopy(arrays[i], 0, newArray, result.length, arrays[i].length);
            result = newArray;
        }

        return result;
    }

    /**
     * Checks if a generic list contains any null members.
     *
     * @param members The Members to be Checked
     */
    @SafeVarargs
    public static <T> boolean containsNull(T... members) {
        for (T member : members)
            if (member == null)
                return true;
        return false;
    }

    /**
     * Gets and returns the class that called the method that is calling this method.
     *
     * @return The class found
     */
    public static Class<?> traceSource() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        if (elements.length > 3) {
            try {
                return Class.forName(elements[3].getClassName());
            } catch (ClassNotFoundException e) {}
        }
        return null;
    }

    /**
     * Returns the specified Object from the Array if the object is present in
     * the array. If not, the first object present in the array will be
     * returned if the size of the array is greater than 1, if not, the
     * specified Object will be returned.
     *
     * @param object The value trying to be set
     * @param array The array that may/may not contain the value
     * @return The value found from the array
     */
    public static <T> T objectFrom(T object, T[] array) {
        int index = ArrayUtils.indexOf(array, object);
        if (index != -1) {
            return array[index];
        } else {
            if (array.length > 0) {
                return array[0];
            } else {
                return object;
            }
        }
    }
}
