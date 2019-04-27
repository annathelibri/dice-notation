package pw.aru.libs.dicenotation.utils;

/**
 * Smart up-casting operation utility with boxed numbers.
 * (Bytes and Shorts are automatically up-casted to Integers)
 */
public class Numbers {
    public static Number divide(Number left, Number right) {
        if (left instanceof Double || right instanceof Double) return left.doubleValue() / right.doubleValue();
        if (left instanceof Float || right instanceof Float) return left.floatValue() / right.floatValue();
        if (left instanceof Long || right instanceof Long) return left.longValue() / right.floatValue();

        return left.intValue() / right.intValue();
    }

    public static Number leftShift(Number left, Number right) {
        long shift = left.longValue() << right.longValue();

        if (left instanceof Integer && right instanceof Integer) return ((int) shift);
        return shift;
    }

    public static Number minus(Number left, Number right) {
        if (left instanceof Double || right instanceof Double) return left.doubleValue() - right.doubleValue();
        if (left instanceof Float || right instanceof Float) return left.floatValue() - right.floatValue();
        if (left instanceof Long || right instanceof Long) return left.longValue() - right.floatValue();

        return left.intValue() - right.intValue();
    }

    public static Number modulus(Number left, Number right) {
        if (left instanceof Double || right instanceof Double) return left.doubleValue() % right.doubleValue();
        if (left instanceof Float || right instanceof Float) return left.floatValue() % right.floatValue();
        if (left instanceof Long || right instanceof Long) return left.longValue() % right.floatValue();

        return left.intValue() / right.intValue();
    }

    public static Number plus(Number left, Number right) {
        if (left instanceof Double || right instanceof Double) return left.doubleValue() + right.doubleValue();
        if (left instanceof Float || right instanceof Float) return left.floatValue() + right.floatValue();
        if (left instanceof Long || right instanceof Long) return left.longValue() + right.floatValue();
        return left.intValue() + right.intValue();
    }

    public static Number power(Number left, Number right) {
        double pow = Math.pow(left.doubleValue(), right.doubleValue());

        if (left instanceof Double || right instanceof Double) return pow;
        if (left instanceof Float || right instanceof Float) return ((float) pow);

        if (((int) pow) == pow) return ((int) pow);
        if (((long) pow) == pow) return ((long) pow);
        return pow;
    }

    public static Number rightShift(Number left, Number right) {
        long shift = left.longValue() >> right.longValue();

        if (left instanceof Integer && right instanceof Integer) return ((int) shift);
        return shift;
    }

    public static Number times(Number left, Number right) {
        if (left instanceof Double || right instanceof Double) return left.doubleValue() * right.doubleValue();
        if (left instanceof Float || right instanceof Float) return left.floatValue() * right.floatValue();
        if (left instanceof Long || right instanceof Long) return left.longValue() * right.floatValue();

        return left.intValue() * right.intValue();
    }

    public static Number unaryMinus(Number target) {
        if (target instanceof Double) return -target.doubleValue();
        if (target instanceof Float) return -target.floatValue();
        if (target instanceof Long) return -target.longValue();
        return -target.intValue();
    }

    public static Number unaryPlus(Number target) {
        if (target instanceof Double) return +target.doubleValue();
        if (target instanceof Float) return +target.floatValue();
        if (target instanceof Long) return +target.longValue();
        return +target.intValue();
    }
}
