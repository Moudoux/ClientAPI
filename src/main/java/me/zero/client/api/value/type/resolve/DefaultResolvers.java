package me.zero.client.api.value.type.resolve;

import me.zero.client.api.util.ReflectionUtils;
import me.zero.client.api.util.annotation.Label;
import me.zero.client.api.value.annotation.MultiValue;
import me.zero.client.api.value.annotation.NumberValue;
import me.zero.client.api.value.type.BooleanType;
import me.zero.client.api.value.type.MultiType;
import me.zero.client.api.value.type.NumberType;
import me.zero.client.api.value.type.StringType;

/**
 * Contains all default resolvers
 *
 * @author Brady
 * @since 5/22/2017 5:41 PM
 */
public final class DefaultResolvers {

    private DefaultResolvers () {}

    /**
     * Resolves Boolean Types
     */
    public static final TypeResolver<BooleanType> BOOLEAN = (parent, field) -> {
        Label label = field.getAnnotation(Label.class);
        Boolean value = (Boolean) ReflectionUtils.getField(parent, field);
        if (value == null)
            value = false;

        BooleanType type = new BooleanType(label.name(), label.id(), label.description(), parent, field);
        type.setState(value);
        return type;
    };

    /**
     * Resolves String Types
     */
    public static final TypeResolver<StringType> STRING = (parent, field) -> {
        Label label = field.getAnnotation(Label.class);
        return new StringType(label.name(), label.id(), label.description(), parent, field);
    };

    /**
     * Resolves Multi Types
     */
    public static final TypeResolver<MultiType> MULTI = (parent, field) -> {
        Label label = field.getAnnotation(Label.class);
        MultiValue multi = field.getAnnotation(MultiValue.class);
        MultiType type = new MultiType(label.name(), label.id(), label.description(), parent, field, multi.value());
        if (type.getValue() == null)
            type.setValue(multi.value()[0]);
        return type;
    };

    /**
     * Resolves Number Types
     */
    @SuppressWarnings("unchecked")
    public static final TypeResolver<NumberType> NUMBER = (parent, field) -> {
        Label label = field.getAnnotation(Label.class);
        NumberValue num = field.getAnnotation(NumberValue.class);

        NumberType type = null;

        // This is an absolute mess, still finding some other solution
        if (field.getType() == Byte.class || field.getType() == Byte.TYPE) {
            type = new NumberType<Byte>(label.name(), label.id(), label.description(), parent, field, (byte) num.min(), (byte) num.max()) { };
        } else if (field.getType() == Short.class || field.getType() == Short.TYPE) {
            type = new NumberType<Short>(label.name(), label.id(), label.description(), parent, field, (short) num.min(), (short) num.max()) { };
        } else if (field.getType() == Integer.class || field.getType() == Integer.TYPE) {
            type = new NumberType<Integer>(label.name(), label.id(), label.description(), parent, field, (int) num.min(), (int) num.max()) { };
        } else if (field.getType() == Long.class || field.getType() == Long.TYPE) {
            type = new NumberType<Long>(label.name(), label.id(), label.description(), parent, field, (long) num.min(), (long) num.max()) { };
        } else if (field.getType() == Float.class || field.getType() == Float.TYPE) {
            type = new NumberType<Float>(label.name(), label.id(), label.description(), parent, field, (float) num.min(), (float) num.max()) { };
        } else if (field.getType() == Double.class || field.getType() == Double.TYPE) {
            type = new NumberType<Double>(label.name(), label.id(), label.description(), parent, field, num.min(), num.max()) { };
        }

        if (type == null)
            return null;

        if (field.getType().isPrimitive()) {
            if (type.getValue().equals(0))
                type.setValue(type.getMinimum());
        } else {
            if (type.getValue() == null)
                type.setValue(type.getMinimum());
        }

        return type;
    };
}
